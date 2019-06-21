package abryu.uwocs.handler;

import abryu.uwocs.AlexaConstants;
import abryu.uwocs.ProjectConfigConstants;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.google.api.services.compute.model.Project;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class LaunchRequestHandler implements RequestHandler {
  @Override
  public boolean canHandle(HandlerInput input) {
    return input.matches(requestType(LaunchRequest.class));
  }

  @Override
  public Optional<Response> handle(HandlerInput input) {
    String speechText = AlexaConstants.LAUNCH_REQUEST_WELCOME_STRING;
    String repromptText = "Please tell me your favorite color by saying, my favorite color is red";
    return input.getResponseBuilder()
            .withSimpleCard("CloudMonitor", speechText)
            .withSpeech(speechText)
            .withReprompt(repromptText)
            .build();
  }
}