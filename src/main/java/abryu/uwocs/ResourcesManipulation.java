package abryu.uwocs;

public interface ResourcesManipulation {

  void makeRequest();

  String getResult();

  String manipulateResources();

  boolean requestSuccessful();

  String getResult(Notification notification);

}
