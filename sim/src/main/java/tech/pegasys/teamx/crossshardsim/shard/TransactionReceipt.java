package tech.pegasys.teamx.crossshardsim.shard;

public class TransactionReceipt {
  enum Status {
    NOT_MINED,
    SUCCESS,
    FAILURE
  }

  Status status = Status.NOT_MINED;
  SystemEventMessage event;


  public void setStatus(Status s) {
    this.status = s;
  }

  public Status getStatus() {
    return this.status;
  }

  public void addSystemEvent(SystemEventMessage event) {
    if (this.event != null) {
      throw new Error("only one event supported at the moment");
    }
    this.event = event;
  }

  public int calculateRoot() {
    if (this.event == null) {
      return 1;
    }
    return this.event.calculateRoot();
  }

}
