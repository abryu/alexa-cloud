import abryu.uwocs.ResourcesManipulation;
import abryu.uwocs.billing.GcpBilling_Impl;
import abryu.uwocs.createinstances.GcpCreateInstance_Impl;
import abryu.uwocs.helpers.AwsUtils;
import abryu.uwocs.listinstances.GcpListInstances_Impl;
import abryu.uwocs.stackdriver.GcpStackdriverMonitoring;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestGCP {

  AwsUtils aws = new AwsUtils("cloud");

  @Ignore
  public void testCreateInstance() {
    ResourcesManipulation resourcesManipulation = new GcpCreateInstance_Impl(aws,"web");
    String response = resourcesManipulation.manipulateResources();
    assertNotNull(response);
    assertTrue(response.contains("Request succeed ; Please check the web console for details"));
  }

  @Test
  public void testListInstances() {
    ResourcesManipulation resourcesManipulation = new GcpListInstances_Impl(aws);
    String response = resourcesManipulation.manipulateResources();
    assertNotNull(response);
    assertTrue(response.contains("You have "));
  }

   @Test
  public void testGcpStackdriveMonitoring() {
    ResourcesManipulation resourcesManipulation = new GcpStackdriverMonitoring(aws,"function_execution_count");
    String response = resourcesManipulation.manipulateResources();
    assertNotNull(response);
    assertTrue(response.contains("Request has been made"));
  }

  @Test
  public void testGcpBilling() {
    ResourcesManipulation resourcesManipulation = new GcpBilling_Impl(aws);
    String response = resourcesManipulation.manipulateResources();
    assertNotNull(response);
    assertTrue(response.contains("billing has been"));
  }
}
