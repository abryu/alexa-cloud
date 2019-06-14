package abryu.uwocs;

import abryu.uwocs.customerizedhandler.*;
import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import abryu.uwocs.handler.*;

public class CloudMonitorStreamHandler extends SkillStreamHandler {

  private static Skill getSkill() {
    return Skills.standard()
            .addRequestHandlers(
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
            // Add your skill id below
            .withSkillId(ProjectConfigConstants.ALEXA_SKILL_ID)
            .build();
  }

  public CloudMonitorStreamHandler() {
    super(getSkill());
  }


}
