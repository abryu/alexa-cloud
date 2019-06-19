package abryu.uwocs.customerizedhandler;

import abryu.uwocs.AlexaConstants;
import abryu.uwocs.Configuration;
import abryu.uwocs.ResourcesManipulation;
import abryu.uwocs.helpers.AwsUtils;
import abryu.uwocs.listinstances.AwsListInstances_Impl;
import abryu.uwocs.listinstances.GcpListInstances_Impl;
import abryu.uwocs.stackdriver.GcpStackdriverMonitoring;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class StackdriveHandler implements RequestHandler {
  @Override
  public boolean canHandle(HandlerInput handlerInput) {
    return handlerInput.matches(intentName("Stackdrive"));
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput) {

    IntentRequest intentRequest = (IntentRequest) handlerInput.getRequestEnvelope().getRequest();

    Intent intent = intentRequest.getIntent();

    Map<String, Slot> slots = intent.getSlots();

    System.out.println("string fy " + intent.getSlots().toString());

    String speechText = "null", repromptText = "null";

    Slot projectNameSlot = slots.get(AlexaConstants.STACKDRIVE_SLOT);
    Slot resourceSlot = slots.get(AlexaConstants.STACKDRIVE_RESOURCE);

    if (projectNameSlot == null) {

      speechText = "Missing project name slot";

      repromptText = "Please provide your project name";

    } else if (resourceSlot == null) {

      speechText = "Missing resource slot";

      repromptText = "Please provide your resource";

    } else {

      AwsUtils aws = new AwsUtils(projectNameSlot.getValue());

      Configuration configuration = aws.getConfigObject();

      ResourcesManipulation stackdrive = null;

      String resource = resourceSlot.getValue().toLowerCase().replaceAll("\\s+", "");

      System.out.println("Resource is  " + resource);

      if (configuration.getType().equals("GCP")) {

        stackdrive = new GcpStackdriverMonitoring(aws, resource);

      } else {

        stackdrive = new AwsListInstances_Impl();

      }

      speechText = stackdrive.manipulateResources();

    }

    return handlerInput.getResponseBuilder()
            .withSimpleCard("ColorSession", speechText)
            .withSpeech(speechText)
            .withReprompt(repromptText)
            .withShouldEndSession(true)
            .build();
  }
}
