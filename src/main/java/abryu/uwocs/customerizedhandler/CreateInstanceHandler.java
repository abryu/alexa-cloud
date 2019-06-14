package abryu.uwocs.customerizedhandler;

import abryu.uwocs.AlexaConstants;
import abryu.uwocs.createinstances.GcpCreateInstance_Impl;
import abryu.uwocs.helpers.AwsUtils;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class CreateInstanceHandler implements RequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput) {
    return handlerInput.matches(intentName("CreateInstance"));
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput) {

    IntentRequest intentRequest = (IntentRequest) handlerInput.getRequestEnvelope().getRequest();

    Intent intent = intentRequest.getIntent();

    Map<String, Slot> slots = intent.getSlots();

    String speechText = "null", repromptText = "null";

    Slot projectNameSlot = slots.get(AlexaConstants.CREATEINSTANCE_SLOT);
    Slot instanceTemplate = slots.get(AlexaConstants.CREATEINSTANCE_TEMPLATE);

    if (projectNameSlot == null || instanceTemplate == null) {
      speechText = "Missing project name or instance template";
      repromptText = "Please provide your project name and template";
    } else {

      AwsUtils aws = new AwsUtils(projectNameSlot.getValue());

      GcpCreateInstance_Impl gcpCreateInstance = new GcpCreateInstance_Impl(aws, instanceTemplate.getValue());

      speechText = gcpCreateInstance.manipulateResources();

    }


    return handlerInput.getResponseBuilder()
            .withSimpleCard("ColorSession", speechText)
            .withSpeech(speechText)
            .withReprompt(repromptText)
            .withShouldEndSession(false)
            .build();
  }

}
