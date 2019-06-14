import abryu.uwocs.helpers.AwsUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TestLoadConfiguration {

  @Test
  public void testS3() {

    AwsUtils aws = new AwsUtils("cloud");

    System.out.println(aws.getConfigObject().getId());

  }
}
