package abryu.uwocs;

public class ProjectConfigConstants {

  public static final String ALEXA_SKILL_ID = System.getenv("ALEXA_SKILL_ID");

  public static final String PROJECT_REGION = "us-east-1";

  public static final String S3_BUCKET_NAME = System.getenv("S3_BUCKET_NAME");

  public static final String S3_ENDPOINT = "s3://" + S3_BUCKET_NAME + "/";

  public static final String S3_CREDENTIALS = "credentials/";

  public static final String S3_CONFIGURATIONS = "configurations/";

  public static final String MAILGUN_DOMAIN = System.getenv("MAILGUN_DOMAIN");

  public static final String MAILGUN_API_KEY = System.getenv("MAILGUN_API_KEY");

  public static final String MAILGUN_RECEIVER = System.getenv("MAILGUN_RECEIVER");
}
