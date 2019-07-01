package abryu.uwocs.stackdriver;

import java.text.Format;
import java.util.HashMap;

public class GcpMetrics {


  public static HashMap<String, MetricEntry> getMetrics() {

    HashMap<String, MetricEntry> map = new HashMap<>();

    map.put("function",
            new MetricEntry("cloudfunctions.googleapis.com/function/execution_count", ""));

    map.put("dataflow",
            new MetricEntry("dataflow.googleapis.com/job/total_pd_usage_time", ""));

    map.put("service",
            new MetricEntry("serviceruntime.googleapis.com/api/request_latencies", ""));

    map.put("storage",
            new MetricEntry("storage.googleapis.com/api/request_count", ""));

    map.put("sql",
            new MetricEntry("cloudsql.googleapis.com/database/cpu/usage_time", ""));

    return map;

  }

  public static class MetricEntry {

    public String getEndpoint() {
      return endpoint;
    }

    public String getFormat() {
      return format;
    }

    private String endpoint;
    private String format;

    public MetricEntry(String endpoint, String format) {

      this.endpoint = endpoint;
      this.format = format;

    }


  }


}
