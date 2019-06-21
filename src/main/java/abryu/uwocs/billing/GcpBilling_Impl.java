package abryu.uwocs.billing;

import abryu.uwocs.Configuration;
import abryu.uwocs.Notification;
import abryu.uwocs.ResourcesManipulation;
import abryu.uwocs.helpers.AwsUtils;
import abryu.uwocs.helpers.GcpUtils;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.cloudbilling.Cloudbilling;
import com.google.api.services.cloudbilling.model.ProjectBillingInfo;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GcpBilling_Impl implements ResourcesManipulation {

  private Configuration configuration;
  private AwsUtils awsUtils;

  private boolean billingEnabled = false;

  public GcpBilling_Impl(AwsUtils awsUtils) {
    this.awsUtils = awsUtils;
    this.configuration = awsUtils.getConfigObject();
  }

  @Override
  public void makeRequest() {

    HttpTransport httpTransport = null;
    try {
      httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    } catch (GeneralSecurityException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    Cloudbilling cloudbillingService = new Cloudbilling.Builder(httpTransport, jsonFactory, GcpUtils.getCredentials(awsUtils))
            .setApplicationName("Google Cloud Platform Sample")
            .build();

    String name = "projects/" + configuration.getId();

    Cloudbilling.Projects.GetBillingInfo request = null;
    try {
      request = cloudbillingService.projects().getBillingInfo(name);
      ProjectBillingInfo response = request.execute();
      billingEnabled = response.getBillingEnabled();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public String getResult() {
    if (billingEnabled)
      return String.format("project %s billing has been enabled", configuration.getTrigger());
    return String.format("project %s billing has been disabled", configuration.getTrigger());
  }

  @Override
  public String manipulateResources() {
    makeRequest();
    return getResult();
  }

  @Override
  public boolean requestSuccessful() {
    return false;
  }

  @Override
  public String getResult(Notification notification) {
    return null;
  }

}
