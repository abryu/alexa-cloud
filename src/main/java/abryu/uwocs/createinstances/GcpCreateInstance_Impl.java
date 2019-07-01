package abryu.uwocs.createinstances;

import abryu.uwocs.Configuration;
import abryu.uwocs.Notification;
import abryu.uwocs.ResourcesManipulation;
import abryu.uwocs.helpers.AwsUtils;
import abryu.uwocs.helpers.GcpUtils;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.Instance;
import com.google.api.services.compute.model.InstanceTemplate;
import com.google.api.services.compute.model.InstanceTemplateList;
import com.google.api.services.compute.model.Operation;
import org.apache.beam.repackaged.beam_sdks_java_core.net.bytebuddy.utility.RandomString;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GcpCreateInstance_Impl implements ResourcesManipulation, CreateInstance {

  private AwsUtils awsUtils;
  private Configuration configuration;
  private String templateName;
  private boolean result;

  public GcpCreateInstance_Impl(AwsUtils awsUtils) {
    this.awsUtils = awsUtils;
    this.configuration = awsUtils.getConfigObject();
  }

  public GcpCreateInstance_Impl(AwsUtils awsUtils, String templateName) {
    this.awsUtils = awsUtils;
    this.templateName = templateName;
    this.configuration = awsUtils.getConfigObject();
  }

  @Override
  public String getAvailableTemplates() {

    String templates = "";

    try {

      InstanceTemplateList response;

      Compute computeService = GcpUtils.createComputeService(awsUtils);

      Compute.InstanceTemplates.List request = computeService.instanceTemplates().list(configuration.getId());

      do {
        response = request.execute();
        if (response.getItems() == null) {
          continue;
        }
        for (InstanceTemplate instanceTemplate : response.getItems()) {
          templates += instanceTemplate.getName() + " ";
        }
        request.setPageToken(response.getNextPageToken());
      } while (response.getNextPageToken() != null);
    } catch (GeneralSecurityException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return templates;
  }

  @Override
  public void makeRequest() {

    try {

      Compute computeService = GcpUtils.createComputeService(awsUtils);

      Instance requestBody = new Instance();

      String instanceName = (templateName + "-" + RandomString.make(6)).toLowerCase();
      requestBody.setName(instanceName);

      Compute.Instances.Insert request = computeService.instances().insert(configuration.getId(), configuration.getZone(), requestBody).setSourceInstanceTemplate(makeTemplatePath());

      Operation response = request.execute();

      System.out.println(response);

      if (response.getStatus().equals("RUNNING"))
        result = true;

    } catch (IOException e) {
      e.printStackTrace();
    } catch (GeneralSecurityException e) {
      e.printStackTrace();
    }

  }

  @Override
  public String getResult() {
    if (result == false)
      return "Error in creating instance";
    return "Request succeed ; Please check the web console for details";
  }

  private String makeTemplatePath() {
    return String.format("https://www.googleapis.com/compute/v1/projects/%s/global/instanceTemplates/%s", configuration.getId(), templateName);
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
