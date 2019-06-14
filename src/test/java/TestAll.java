import abryu.uwocs.ResourcesManipulation;
import abryu.uwocs.billing.GcpBilling_Impl;
import abryu.uwocs.createinstances.GcpCreateInstance_Impl;
import abryu.uwocs.helpers.AwsUtils;
import abryu.uwocs.jenkins.Jenkins;
import abryu.uwocs.stackdriver.GcpStackdriverMonitoring;
import org.junit.Ignore;
import org.junit.Test;

public class TestAll {

  @Ignore
  public void testJenkinsList() {

    AwsUtils aws = new AwsUtils("integration");

    ResourcesManipulation resourcesManipulation = new Jenkins(aws, "list", "function_execution_count");

    resourcesManipulation.makeRequest();

    System.out.println(resourcesManipulation.getResult());

  }

  @Ignore
  public void testJenkinsBuild() {

    AwsUtils aws = new AwsUtils("integration");

    ResourcesManipulation resourcesManipulation = new Jenkins(aws, "build", "sample-job2");

    resourcesManipulation.makeRequest();

    System.out.println(resourcesManipulation.getResult());

  }

  @Ignore
  public void testJenkinsCheck() {

    AwsUtils aws = new AwsUtils("integration");

    ResourcesManipulation resourcesManipulation = new Jenkins(aws, "check", "sample-job2");

    resourcesManipulation.makeRequest();

    System.out.println(resourcesManipulation.getResult());

  }

  @Test
  public void testJenkinsNodes() {

    AwsUtils aws = new AwsUtils("integration");

    ResourcesManipulation resourcesManipulation = new Jenkins(aws, "executors", "sample-job2");

    resourcesManipulation.makeRequest();

    System.out.println(resourcesManipulation.getResult());

  }

  @Ignore
  public void testJenkinsQueue() {

    AwsUtils aws = new AwsUtils("integration");

    ResourcesManipulation resourcesManipulation = new Jenkins(aws, "queue", "sample-job");

    resourcesManipulation.makeRequest();

    System.out.println(resourcesManipulation.getResult());

  }


  @Ignore
  public void testGcpStackdriveInstance() {

    AwsUtils aws = new AwsUtils("cloud");

    ResourcesManipulation resourcesManipulation = new GcpStackdriverMonitoring(aws, "function_execution_count");

    resourcesManipulation.makeRequest();

  }

  @Ignore
  public void testBilling() {

    AwsUtils aws = new AwsUtils("cloud");

    GcpBilling_Impl gcp = new GcpBilling_Impl(aws);

    gcp.makeRequest();

    System.out.println(gcp.getResult());

  }


  @Ignore
  public void testCreateInstance() {


    AwsUtils aws = new AwsUtils("cloud");

    GcpCreateInstance_Impl gcpDataflow = new GcpCreateInstance_Impl(aws, "web");

    gcpDataflow.makeRequest();

    System.out.println(gcpDataflow.getResult());
  }
}
