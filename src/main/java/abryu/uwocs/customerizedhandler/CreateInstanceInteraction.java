package abryu.uwocs.customerizedhandler;

import abryu.uwocs.AlexaConstants;
import abryu.uwocs.createinstances.GcpCreateInstance_Impl;
import abryu.uwocs.helpers.AwsUtils;
import abryu.uwocs.stackdriver.GcpMetrics;
import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.twilio.rest.accounts.v1.credential.Aws;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class CreateInstanceInteraction implements RequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput) {
    return handlerInput.matches(intentName("CreateInstanceInteraction"));
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput) {

    IntentRequest intentRequest = (IntentRequest) handlerInput.getRequestEnvelope().getRequest();

    Intent intent = intentRequest.getIntent();

    Map<String, Slot> slots = intent.getSlots();

    AttributesManager attributesManager = handlerInput.getAttributesManager();
    Map<String, Object> attributes = attributesManager.getSessionAttributes();

    if (attributes.containsKey("level") == false) {
      attributes.put("level", "1");
      attributesManager.setSessionAttributes(attributes);
      return handlerInput.getResponseBuilder()
              .withSimpleCard("CloudMonitor", "Please provide your project name for creating instance")
              .withSpeech("Please provide your project name for creating instance")
              .withReprompt("Please provide your project name for creating instance")
              .withShouldEndSession(false)
              .build();
    }

    if (attributes.get("level").equals("1")) {

      attributes.put("level", "2");
      attributes.put("alias", slots.get(AlexaConstants.CREATEINSTANCE_SLOT).getValue());
      attributesManager.setSessionAttributes(attributes);

      AwsUtils aws = new AwsUtils(slots.get(AlexaConstants.CREATEINSTANCE_SLOT).getValue().toString());

      GcpCreateInstance_Impl gcpCreateInstance = new GcpCreateInstance_Impl(aws);

      String templates = "Please provide your template name, available template options are" + " " + gcpCreateInstance.getAvailableTemplates();

      return handlerInput.getResponseBuilder()
              .withSimpleCard("CloudMonitor", templates)
              .withSpeech(templates)
              .withReprompt(templates)
              .withShouldEndSession(false)
              .build();
    }

    if (attributes.get("level").equals("2")) {

      attributes.put("level", "3");

      AwsUtils aws = new AwsUtils(attributes.get("alias").toString());

      GcpCreateInstance_Impl gcpCreateInstance = new GcpCreateInstance_Impl(aws, slots.get(AlexaConstants.CREATEINSTANCE_TEMPLATE).getValue());

      attributesManager.setSessionAttributes(attributes);

      String result = gcpCreateInstance.manipulateResources();

      return handlerInput.getResponseBuilder()
              .withSimpleCard("CloudMonitor", result)
              .withSpeech(result)
              .withReprompt(result)
              .withShouldEndSession(true)
              .build();
    }


    return handlerInput.getResponseBuilder()
            .withSimpleCard("CloudMonitor", "error occurred")
            .withSpeech("error occurred")
            .withReprompt("error occurred")
            .withShouldEndSession(true)
            .build();
  }
}
