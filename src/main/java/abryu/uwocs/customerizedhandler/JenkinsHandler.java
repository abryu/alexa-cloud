package abryu.uwocs.customerizedhandler;


import abryu.uwocs.AlexaConstants;
import abryu.uwocs.Configuration;
import abryu.uwocs.ResourcesManipulation;
import abryu.uwocs.helpers.AwsUtils;
import abryu.uwocs.jenkins.Jenkins;
import abryu.uwocs.listinstances.AwsListInstances_Impl;
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

public class JenkinsHandler implements RequestHandler {
  @Override
  public boolean canHandle(HandlerInput handlerInput) {
    return handlerInput.matches(intentName("Jenkins"));
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput) {
    IntentRequest intentRequest = (IntentRequest) handlerInput.getRequestEnvelope().getRequest();

    Intent intent = intentRequest.getIntent();

    Map<String, Slot> slots = intent.getSlots();

    String speechText = "null", repromptText = "null";

    Slot projectNameSlot = slots.get(AlexaConstants.JENKINS_SLOT);

    if (projectNameSlot == null) {

      speechText = "Missing project name slot";
      repromptText = "Please provide your project name";

    } else if (slots.get(AlexaConstants.JENKINS_ACTION).getValue().equals("build") && slots.get(AlexaConstants.JENKINS_ITEM) == null) {

      speechText = "Missing build item slot";
      repromptText = "Please provide your build item";

    } else {

      AwsUtils aws = new AwsUtils(projectNameSlot.getValue());

      String resource = (slots.get(AlexaConstants.JENKINS_ITEM) == null ? null : slots.get(AlexaConstants.JENKINS_ITEM).getValue());

      ResourcesManipulation jenkins = new Jenkins(aws, slots.get(AlexaConstants.JENKINS_ACTION).getValue(), resource);

      speechText = jenkins.manipulateResources();

    }

    return handlerInput.getResponseBuilder()
            .withSimpleCard("ColorSession", speechText)
            .withSpeech(speechText)
            .withReprompt(repromptText)
            .withShouldEndSession(true)
            .build();
  }
}
