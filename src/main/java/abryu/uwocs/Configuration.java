package abryu.uwocs;

public class Configuration {

  @Override
  public String toString() {
    return String.format("%s,%s,%s,%s,%s,%s,%s", this.type, this.id, this.trigger, this.credential, this.username, this.password, this.zone);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Configuration(String type, String trigger, String id, String credential, String username, String password, String zone) {
    this.type = type;
    this.trigger = trigger;
    this.id = id;
    this.credential = credential;
    this.username = username;
    this.password = password;
    this.zone = zone;
  }


  public String getTrigger() {
    return trigger;
  }

  public void setTrigger(String trigger) {
    this.trigger = trigger;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCredential() {
    return credential;
  }

  public void setCredential(String credential) {
    this.credential = credential;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  private String type;
  private String trigger;
  private String id;
  private String credential;
  private String username;
  private String password;

  public String getZone() {
    return zone;
  }

  public void setZone(String zone) {
    this.zone = zone;
  }

  private String zone;
}
