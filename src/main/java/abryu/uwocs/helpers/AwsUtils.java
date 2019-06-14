package abryu.uwocs.helpers;

import abryu.uwocs.Configuration;
import abryu.uwocs.ProjectConfigConstants;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.*;
import java.util.Properties;

public class AwsUtils {

  private String alias;
  private AmazonS3 s3client;

  public AwsUtils(String alias) {

    this.alias = alias;
    this.s3client = AmazonS3ClientBuilder.standard()
            //.withCredentials(new ProfileCredentialsProvider())\
            .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
            .withRegion(ProjectConfigConstants.PROJECT_REGION)
            .build();

  }


  public Configuration getConfigObject() {

    String configFilePath = ProjectConfigConstants.S3_CONFIGURATIONS + alias.toLowerCase() + ".properties";

    if (checkFileExists(configFilePath) == false)
      return null;

    S3Object configFileObject = s3client.getObject(new GetObjectRequest(ProjectConfigConstants.S3_BUCKET_NAME, configFilePath));

    return convertToConfigurationObject(configFileObject.getObjectContent());

  }

  private boolean checkFileExists(String fileName) {

    return s3client.doesObjectExist(ProjectConfigConstants.S3_BUCKET_NAME, fileName);

  }

  public InputStream getCredentialsInputStream() {

    String credentialFilePath = ProjectConfigConstants.S3_CREDENTIALS + alias.toLowerCase() + ".json";

    if (checkFileExists(credentialFilePath) == false)
      return null;

    S3Object credFileObject = s3client.getObject(new GetObjectRequest(ProjectConfigConstants.S3_BUCKET_NAME, credentialFilePath));

    return credFileObject.getObjectContent();
  }

  private Configuration convertToConfigurationObject(InputStream inputStream) {

    Properties props = new Properties();

    try {
      props.load(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Configuration conf = new Configuration(props.getProperty("type"),
            props.getProperty("trigger"),
            props.getProperty("id"),
            props.getProperty("credential"),
            props.getProperty("username"),
            props.getProperty("password"),
            props.getProperty("zone"));

    return conf;

  }

}
