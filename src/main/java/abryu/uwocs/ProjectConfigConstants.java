package abryu.uwocs;

public class ProjectConfigConstants {

  public static final String ALEXA_SKILL_ID = System.getenv("ALEXA_SKILL_ID");

  public static final String PROJECT_REGION = "us-east-1";

  public static final String S3_BUCKET_NAME = System.getenv("S3_BUCKET_NAME");

  public static final String S3_CREDENTIALS = "credentials/";

  public static final String S3_CONFIGURATIONS = "configurations/";

  public static final String MAILGUN_DOMAIN = System.getenv("MAILGUN_DOMAIN");

  public static final String MAILGUN_API_KEY = System.getenv("MAILGUN_API_KEY");

  public static final String MAILGUN_RECEIVER = System.getenv("MAILGUN_RECEIVER");

  public static final String TWILIO_RECEIVER = System.getenv("TWILIO_RECEIVER");

  public static final String TWILIO_ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");

  public static final String TWILIO_AUTH = System.getenv("TWILIO_AUTH");

  public static final String TWILIO_SENDER = System.getenv("TWILIO_SENDER");

  public static final int STACKDRIVE_MINUTE_INTERVAL = 60;
}
