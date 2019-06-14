package abryu.uwocs.helpers;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.cloud.monitoring.v3.MetricServiceSettings;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

public class GcpUtils {

  public static GoogleCredential getCredentials(AwsUtils awsUtils) {

    GoogleCredential credential = null;

    try {
      credential = GoogleCredential.fromStream(awsUtils.getCredentialsInputStream());
      if (credential.createScopedRequired()) {
        credential =
                credential.createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {

      return credential;

    }

  }

  public static Compute createComputeService(AwsUtils awsUtils) throws IOException, GeneralSecurityException {

    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    return new Compute.Builder(httpTransport, jsonFactory, GcpUtils.getCredentials(awsUtils))
            .setApplicationName("Google-ComputeSample/0.1")
            .build();
  }

  private static Credentials getAuthCredentials(AwsUtils awsUtils) {
    try {
      GoogleCredentials creds =
              awsUtils.getCredentialsInputStream() == null
                      ? GoogleCredentials.getApplicationDefault()
                      : GoogleCredentials.fromStream(awsUtils.getCredentialsInputStream());
      if (creds.createScopedRequired()) {
        creds = creds.createScoped("https://www.googleapis.com/auth/cloud-platform");
      }
      return creds;
    } catch (IOException e) {
      String message = "Failed to init auth credentials: " + e.getMessage();
      try {
        throw new IOException(message, e);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    return null;
  }


  public static MetricServiceClient createMetricServiceClient(AwsUtils awsUtils) {

    MetricServiceClient metricServiceClient = null;

    try {
      metricServiceClient =
              MetricServiceClient.create(
                      MetricServiceSettings.newBuilder()
                              .setCredentialsProvider(FixedCredentialsProvider.create(getAuthCredentials(awsUtils)))
                              .build());
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    return metricServiceClient;

  }
}
