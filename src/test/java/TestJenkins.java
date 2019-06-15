import abryu.uwocs.ResourcesManipulation;
import abryu.uwocs.helpers.AwsUtils;
import abryu.uwocs.jenkins.Jenkins;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestJenkins {

  private AwsUtils aws = new AwsUtils("integration");
  private String test_item = "web";

  @Test
  public void testListItems() {

    ResourcesManipulation resourcesManipulation = new Jenkins(aws, "list", "item doesn't matter");

    String response  = resourcesManipulation.manipulateResources();

    assertNotNull(response);
    assertTrue(response.contains("You have"));

  }

  @Ignore
  public void testBuildItem() {

    ResourcesManipulation resourcesManipulation = new Jenkins(aws, "build", test_item);
    String response  = resourcesManipulation.manipulateResources();
    assertNotNull(response);
    assertTrue(response.contains("has been triggered ; please check the queue"));

  }

  @Test
  public void testListQueue() {

    ResourcesManipulation resourcesManipulation = new Jenkins(aws, "queue", "item doesn't matter");

    String response  = resourcesManipulation.manipulateResources();

    assertNotNull(response);
    assertTrue(response.contains("jobs in the queue"));

  }

  @Test
  public void testExecutors() {

    ResourcesManipulation resourcesManipulation = new Jenkins(aws, "executors", "item doesn't matter");

    String response  = resourcesManipulation.manipulateResources();

    assertNotNull(response);
    assertTrue(response.contains("At this moment, You totally have "));

  }
}
