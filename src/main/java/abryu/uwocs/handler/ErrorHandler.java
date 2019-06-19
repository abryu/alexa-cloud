package abryu.uwocs.handler;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

public class ErrorHandler implements ExceptionHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, Throwable throwable) {
    return true;
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, Throwable throwable) {
    System.out.println("Error Handler " + handlerInput.toString());
    System.out.println("Error Handler " + throwable.toString());
    final String speechOutput = "Error happened ; Please check Cloud Watch Logs";
    return handlerInput.getResponseBuilder().withSpeech(speechOutput).withReprompt(speechOutput).build();
  }
}
