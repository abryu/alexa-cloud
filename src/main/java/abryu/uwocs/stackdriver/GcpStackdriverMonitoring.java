package abryu.uwocs.stackdriver;

import abryu.uwocs.Configuration;
import abryu.uwocs.ResourcesManipulation;
import abryu.uwocs.helpers.AwsUtils;
import abryu.uwocs.helpers.GcpUtils;
import abryu.uwocs.helpers.MailgunUtils;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.monitoring.v3.*;
import com.google.protobuf.util.Timestamps;

import java.util.HashMap;


public class GcpStackdriverMonitoring implements ResourcesManipulation {

  private AwsUtils awsUtils;
  private Configuration configuration;
  private String resourceType;
  private HashMap<String, GcpMetrics.MetricEntry> map;
  private String result;

  public GcpStackdriverMonitoring(AwsUtils awsUtils, String resourceType) {
    this.awsUtils = awsUtils;
    this.configuration = awsUtils.getConfigObject();
    this.resourceType = resourceType;
    this.map = GcpMetrics.getMetrics();
  }


  @Override
  public void makeRequest() {

    // Instantiates a client
    MetricServiceClient metricServiceClient = null;
    metricServiceClient = GcpUtils.createMetricServiceClient(awsUtils);
    String projectId = configuration.getId();
    ProjectName name = ProjectName.of(projectId);


    long startMillis = System.currentTimeMillis() - ((60 * 360) * 1000);

    TimeInterval interval = TimeInterval.newBuilder()
            .setStartTime(Timestamps.fromMillis(startMillis))
            .setEndTime(Timestamps.fromMillis(System.currentTimeMillis()))
            .build();

    System.out.println("Resource type " + resourceType);

    ListTimeSeriesRequest.Builder requestBuilder = ListTimeSeriesRequest.newBuilder()
            .setName(name.toString())
            .setFilter(String.format("metric.type=\"%s\"", map.get(resourceType).getEndpoint()))
            .setInterval(interval);

    //https://cloud.google.com/monitoring/api/ref_v3/rest/v3/projects.timeSeries/list

    //https://cloud.google.com/monitoring/api/metrics_gcp#gcp-cloudfunctions

    ListTimeSeriesRequest request = requestBuilder.build();

    MetricServiceClient.ListTimeSeriesPagedResponse response = metricServiceClient.listTimeSeries(request);

    StringBuilder sb = new StringBuilder();

    sb.append("Got timeseries: \n");

    for (TimeSeries ts : response.iterateAll()) {
      sb.append(ts);
    }

    result = sb.toString();

  }

  @Override
  public String getResult() {

    System.out.println("sending " + result.toString());

    new Thread(() -> {
      MailgunUtils.sendEmail(resourceType, result);
    }).start();

    return "Request has been made, Please check your email";
  }

  @Override
  public String manipulateResources() {
    makeRequest();
    return getResult();
  }
}
