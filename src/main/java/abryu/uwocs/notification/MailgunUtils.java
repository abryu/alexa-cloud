package abryu.uwocs.notification;

import abryu.uwocs.Notification;
import abryu.uwocs.ProjectConfigConstants;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class MailgunUtils implements Notification {

  @Override
  public void send(String subject, String body) {
    HttpResponse<JsonNode> request = null;
    try {
      request = Unirest.post("https://api.mailgun.net/v3/" + ProjectConfigConstants.MAILGUN_DOMAIN + "/messages")
              .basicAuth("api", ProjectConfigConstants.MAILGUN_API_KEY)
              .field("from", "Alexa Cloud <USER@YOURDOMAIN.COM>")
              .field("to", ProjectConfigConstants.MAILGUN_RECEIVER)
              .field("subject", subject)
              .field("text", body)
              .asJson();
    } catch (UnirestException e) {
      e.printStackTrace();
    }

    System.out.println(request.getBody());
  }
}
