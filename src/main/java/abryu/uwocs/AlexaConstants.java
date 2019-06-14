package abryu.uwocs;

public class AlexaConstants {

  //https://093764640533.signin.aws.amazon.com/console

  //mvn org.apache.maven.plugins:maven-assembly-plugin:2.6:assembly -DdescriptorId=jar-with-dependencies package

  public static final String LAUNCH_REQUEST_WELCOME_STRING = "Welcome";

  public static final String HELP_INTENT_STRING = "Help";

  public static final String CANCEL_AND_STOP_INTENT = "Cancel";

  public static final String FALLBACK_INTENT = "Fallback";


  //List Instances
  public static final String LIST_INSTANCES_SLOT = "alias";


  // Billing
  public static final String BILLING_SLOT = "alias";


  // CreateInstance
  public static final String CREATEINSTANCE_SLOT = "alias";
  public static final String CREATEINSTANCE_TEMPLATE = "template";

  // Stackdrive
  public static final String STACKDRIVE_SLOT = "alias";
  public static final String STACKDRIVE_RESOURCE = "resource";

  // Jenkins
  public static final String JENKINS_SLOT = "alias";
  public static final String JENKINS_ACTION = "action";
  public static final String JENKINS_ITEM = "item";


}
