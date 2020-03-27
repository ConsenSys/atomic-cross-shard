package tech.pegasys.teamx.crossshardsim.shard;

public class TransactionReceipt {
  enum Status {
    NOT_MINED,
    SUCCESS,
    FAILURE
  }

  Status status = Status.NOT_MINED;


  public void setStatus(Status s) {
    this.status = s;
  }

  public Status getStatus() {
    return this.status;
  }


}
