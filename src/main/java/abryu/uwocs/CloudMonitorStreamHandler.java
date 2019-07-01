package abryu.uwocs;

import abryu.uwocs.customerizedhandler.*;
import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import abryu.uwocs.handler.*;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.exception.handler.GenericExceptionHandler;

import java.util.Optional;

public class CloudMonitorStreamHandler extends SkillStreamHandler {

  private static Skill getSkill() {
    return Skills.standard()
            .addRequestHandlers(
                    new CreateInstanceInteraction(),
                    new StackdriveInteractionHandler(),
                    new JenkinsHandler(),
                    new StackdriveHandler(),
                    new CreateInstanceHandler(),
                    new BillingHandler(),
                    new ListInstancesHandler(),
                    new LaunchRequestHandler(),
                    new CancelandStopIntentHandler(),
                    new SessionEndedRequestHandler(),
                    new HelpIntentHandler(),
                    new FallbackIntentHandler())
            .withSkillId(ProjectConfigConstants.ALEXA_SKILL_ID)
            .build();
  }

  public CloudMonitorStreamHandler() {
    super(getSkill());
  }

}
