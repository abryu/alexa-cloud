package abryu.uwocs.notification;

import abryu.uwocs.Notification;
import abryu.uwocs.ProjectConfigConstants;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioUtils implements Notification {
  @Override
  public void send(String subject, String body) {

    Twilio.init(ProjectConfigConstants.TWILIO_ACCOUNT_SID, ProjectConfigConstants.TWILIO_AUTH);


    Message message = Message.creator(new PhoneNumber(ProjectConfigConstants.TWILIO_RECEIVER), new PhoneNumber(ProjectConfigConstants.TWILIO_SENDER), body).create();

    System.out.println(message.getSid());

  }
}
