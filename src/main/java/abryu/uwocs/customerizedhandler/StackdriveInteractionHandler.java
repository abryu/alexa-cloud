package abryu.uwocs.customerizedhandler;

import abryu.uwocs.AlexaConstants;
import abryu.uwocs.Configuration;
import abryu.uwocs.ResourcesManipulation;
import abryu.uwocs.helpers.AwsUtils;
import abryu.uwocs.listinstances.AwsListInstances_Impl;
import abryu.uwocs.notification.MailgunUtils;
import abryu.uwocs.notification.TwilioUtils;
import abryu.uwocs.stackdriver.GcpMetrics;
import abryu.uwocs.stackdriver.GcpStackdriverMonitoring;
import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class StackdriveInteractionHandler implements RequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput) {
    return handlerInput.matches(intentName("StackdriveInteraction"));
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
              .withSimpleCard("CloudMonitor", "Please provide your project name for monitoring resource")
              .withSpeech("Please provide your project name for monitoring resource")
              .withReprompt("Please provide your project name for monitoring resource")
              .withShouldEndSession(false)
              .build();
    }

    if (attributes.get("level").equals("1")) {

      attributes.put("level", "2");
      attributes.put("alias", slots.get(AlexaConstants.STACKDRIVE_SLOT).getValue());

      String availableResource = "Please provide your resource name, available resource options are" + " ";
      for (String key : GcpMetrics.getMetrics().keySet())
        availableResource += key + ", ";

      attributesManager.setSessionAttributes(attributes);
      return handlerInput.getResponseBuilder()
              .withSimpleCard("CloudMonitor", availableResource)
              .withSpeech(availableResource)
              .withReprompt(availableResource)
              .withShouldEndSession(false)
              .build();

    }

    if (attributes.get("level").equals("2")) {

      attributes.put("level", "3");

      attributes.put("resource", slots.get(AlexaConstants.STACKDRIVE_RESOURCE).getValue());

      AwsUtils aws = new AwsUtils(attributes.get("alias").toString());

      Configuration configuration = aws.getConfigObject();

      ResourcesManipulation stackdrive = null;

      String resource = slots.get("resource").getValue().toLowerCase().replaceAll("\\s+", "");

      System.out.println("Resource is  " + resource);

      if (configuration.getType().equals("GCP")) {
        stackdrive = new GcpStackdriverMonitoring(aws, resource);
      } else {
        stackdrive = new AwsListInstances_Impl();
      }

      stackdrive.makeRequest();

      if (stackdrive.requestSuccessful()) {
        attributes.put("result", stackdrive.getResult());
        attributesManager.setSessionAttributes(attributes);
      } else {
        return handlerInput.getResponseBuilder()
                .withSimpleCard("CloudMonitor", "Error occurred in requesting GCP")
                .withSpeech("Error occurred in requesting GCP")
                .withReprompt("Error occurred in requesting GCP")
                .withShouldEndSession(true)
                .build();
      }

      return handlerInput.getResponseBuilder()
              .withSimpleCard("CloudMonitor", "Please provide your notification method")
              .withSpeech("Please provide your notification method")
              .withReprompt("Please provide your notification method")
              .withShouldEndSession(false)
              .build();
    }

    if (attributes.get("level").equals("3")) {

      String userNotification = slots.get(AlexaConstants.STACKDRIVE_NOTIFICATION).getValue();

      System.out.println("got notification  " + userNotification);

      if (userNotification.equals("text")) {
        new TwilioUtils().send("Alexa Cloud", attributes.get("result").toString());
      } else {
        new MailgunUtils().send("Alexa Cloud", attributes.get("result").toString());
      }

      return handlerInput.getResponseBuilder().withSpeech("Please check").withShouldEndSession(true).build();

    }

    return handlerInput.getResponseBuilder()
            .withSimpleCard("CloudMonitor", "Error occurred in requesting GCP")
            .withSpeech("Error occurred in requesting GCP")
            .withReprompt("Error occurred in requesting GCP")
            .withShouldEndSession(true)
            .build();
  }
}
