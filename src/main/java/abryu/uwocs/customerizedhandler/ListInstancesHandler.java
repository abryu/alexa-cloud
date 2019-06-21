package abryu.uwocs.customerizedhandler;

import abryu.uwocs.AlexaConstants;
import abryu.uwocs.Configuration;
import abryu.uwocs.ResourcesManipulation;
import abryu.uwocs.helpers.AwsUtils;
import abryu.uwocs.listinstances.AwsListInstances_Impl;
import abryu.uwocs.listinstances.GcpListInstances_Impl;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

public class ListInstancesHandler implements RequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput) {
    return handlerInput.matches(intentName("ListInstances"));
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput) {

    IntentRequest intentRequest = (IntentRequest) handlerInput.getRequestEnvelope().getRequest();

    Intent intent = intentRequest.getIntent();

    Map<String, Slot> slots = intent.getSlots();

    String speechText = "null", repromptText = "null";

    Slot projectNameSlot = slots.get(AlexaConstants.LIST_INSTANCES_SLOT);

    if (projectNameSlot == null) {
      speechText = "Missing project name slot";
      repromptText = "Please provide your project name";
    } else {

      AwsUtils aws = new AwsUtils(projectNameSlot.getValue());

      Configuration configuration = aws.getConfigObject();

      ResourcesManipulation listInstances = null;

      if (configuration.getType().equals("GCP")) {

        listInstances = new GcpListInstances_Impl(aws);

      } else {

        listInstances = new AwsListInstances_Impl();

      }

      speechText = listInstances.manipulateResources();

    }

    return handlerInput.getResponseBuilder()
            .withSimpleCard("CloudMonitor", speechText)
            .withSpeech(speechText)
            .withReprompt(repromptText)
            .withShouldEndSession(true)
            .build();
  }
}
