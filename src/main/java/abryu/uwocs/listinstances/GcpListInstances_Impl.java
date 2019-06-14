package abryu.uwocs.listinstances;

import abryu.uwocs.Configuration;
import abryu.uwocs.ResourcesManipulation;
import abryu.uwocs.helpers.AwsUtils;
import abryu.uwocs.helpers.GcpUtils;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.Instance;
import com.google.api.services.compute.model.InstanceList;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class GcpListInstances_Impl implements ResourcesManipulation {


  private Configuration configuration;
  private AwsUtils awsUtils;

  private List<String> instances = new ArrayList<>();

  public GcpListInstances_Impl(AwsUtils awsUtils) {
    this.awsUtils = awsUtils;
    this.configuration = awsUtils.getConfigObject();
  }

  @Override
  public void makeRequest() {

    try {
      Compute computeService = GcpUtils.createComputeService(awsUtils);

      Compute.Instances.List request = computeService.instances().list(configuration.getId(), configuration.getZone());

      InstanceList response;

      do {
        response = request.execute();
        if (response.getItems() == null) {
          continue;
        }
        for (Instance instance : response.getItems()) {
          instances.add(instance.getName());
        }
        request.setPageToken(response.getNextPageToken());
      } while (response.getNextPageToken() != null);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (GeneralSecurityException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getResult() {

    String responseToUser = String.format("You have %d instances in zone %s", instances.size(), configuration.getZone());

    if (instances.size() == 0)
      return responseToUser;

    responseToUser = responseToUser + " ; they are ";

    for (String ins : instances) {
      responseToUser += ins + " ";
    }

    return responseToUser;
  }

  @Override
  public String manipulateResources() {
    makeRequest();
    return getResult();
  }


}
