package abryu.uwocs.stackdriver;

import java.text.Format;
import java.util.HashMap;

public class GcpMetrics {


  public static HashMap<String, MetricEntry> getMetrics() {

    HashMap<String, MetricEntry> map = new HashMap<>();

    map.put("function_execution_count",
            new MetricEntry("cloudfunctions.googleapis.com/function/execution_count", ""));

    map.put("dataflow_pd",
            new MetricEntry("dataflow.googleapis.com/job/total_pd_usage_time", ""));

    map.put("serviceruntime_api_request_latencies",
            new MetricEntry("serviceruntime.googleapis.com/api/request_latencies", ""));

    map.put("storage_api_request_count",
            new MetricEntry("storage.googleapis.com/api/request_count", ""));

    map.put("cloud_sql_cpu_usage",
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
