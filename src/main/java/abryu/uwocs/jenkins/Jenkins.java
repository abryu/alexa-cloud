package abryu.uwocs.jenkins;

import abryu.uwocs.Configuration;
import abryu.uwocs.ResourcesManipulation;
import abryu.uwocs.helpers.AwsUtils;
import com.cdancy.jenkins.rest.JenkinsClient;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class Jenkins implements ResourcesManipulation {

  private AwsUtils awsUtils;
  private Configuration configuration;
  private String action;
  private String item;
  private JenkinsServer jenkinsServer;
  private String result;

  public Jenkins(AwsUtils awsUtils, String action, String item) {
    this.awsUtils = awsUtils;
    this.configuration = awsUtils.getConfigObject();
    try {
      //this.jenkinsServer = new JenkinsServer(new URI("http://18.224.15.63:8080"), configuration.getUsername(), configuration.getPassword());
      this.jenkinsServer = new JenkinsServer(new URI(configuration.getId()), configuration.getUsername(), configuration.getPassword());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    this.action = action;
    this.item = item;
  }


  @Override
  public void makeRequest() {

    if (action.equals("list")) {

      listItems();

    } else if (action.equals("build")) {

      System.out.println("Build item is " + item);

      buildItem(item);

    } else if (action.equals("queue")) {

      listQueue();

    } else if (action.equals("check")) {

      getLastItemBuildResult(item);

    } else if (action.equals("executors")) {

      checkExecutors();

    }
//https://github.com/jenkinsci/java-client-api/blob/master/jenkins-client/src/test/java/com/offbytwo/jenkins/integration/BuildJobTestReports.java
  }

  private void checkExecutors() {


    StringBuilder sb = new StringBuilder();
    try {

      ComputerSet set = jenkinsServer.getComputerSet();
      int busy = set.getBusyExecutors();
      int total = set.getTotalExecutors();
      result = String.format("At this moment, You totally have %d executors, %d of them are busy", total, busy);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void getLastItemBuildResult(String itemName) {

    StringBuilder sb = new StringBuilder();

    try {

      sb.append("Last Build for " + itemName + " " + jenkinsServer.getJob(itemName).getLastBuild().getNumber());

      result = sb.toString();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void listQueue() {

    try {

      com.offbytwo.jenkins.model.Queue q = jenkinsServer.getQueue();

      StringBuilder sb = new StringBuilder();

      sb.append("there are " + q.getItems().size() + " jobs in the queue ");

      if (q.getItems().size() != 0) {
        sb.append("they are ");
        for (QueueItem item : q.getItems()) {
          sb.append(item.getTask().getName());
        }
      }

      result = sb.toString();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void buildItem(String jobName) {

    /*

    JenkinsClient client =
            new JenkinsClient.Builder()
                    .endPoint(configuration.getId())
                    .credentials(configuration.getUsername()+":"+configuration.getPassword()).build();

    if (client.api().jobsApi().build(null,jobName).errors().size() == 0)
      result = jobName + " has been triggered ; please check the queue";
    else
      result = jobName + " failed to start";

*/

    try {
      jenkinsServer.getJob(jobName).build();
      result = jobName + " has been triggered ; please check the queue";
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void listItems() {

    StringBuilder sb = new StringBuilder();

    try {

      Map<String, Job> jobs = jenkinsServer.getJobs();

      sb.append("You have " + jobs.size() + " items in this Jenkins server ");

      if (jobs.size() == 0) {
        result = sb.toString();
      } else {
        sb.append("they are ");
      }

      for (Job job : jobs.values()) {
        sb.append(job.getName() + " ");
      }


    } catch (IOException e) {

      e.printStackTrace();

    } finally {

      result = sb.toString();

    }


  }

  @Override
  public String getResult() {
    System.out.println(result);
    return result;
  }

  @Override
  public String manipulateResources() {
    makeRequest();
    return getResult();
  }

}
