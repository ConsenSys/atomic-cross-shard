package tech.pegasys.teamx.crossshardsim;

public class Transaction {
  public enum Type {
    NORMAL_SINGLE_CHAIN,
    DEPLOY_NON_LOCKABLE,
    DEPLOY_LOCKABLE,
    START,
    SEGMENT,
    ROOT,
    SIGNALLING,
    CLEAN

  }

  Type type;
  int contractAddress;
  int value;
  int payload;

  TransactionReceipt receipt;

  public Transaction(Type type, int contractAddress, int value, int payload) {
    this.type = type;
    this.contractAddress = contractAddress;
    this.value = value;
    this.payload = payload;
  }


  public void addReceipt(TransactionReceipt txr) {
    this.receipt = txr;
  }

  public void execute(Shard shard, BeaconChain beaconChain) {
    boolean success = false;
    switch (this.type) {
      case NORMAL_SINGLE_CHAIN:
        break;
      case DEPLOY_NON_LOCKABLE:
        success = shard.deployContract(this.contractAddress, false);
        if (success) {
          success = shard.executeContractFunction(this.contractAddress, this.value, this.payload, false);
        }
        break;
      case DEPLOY_LOCKABLE:
        break;
      case START:
        break;
      case SEGMENT:
        break;
      case ROOT:
        break;
      case SIGNALLING:
        break;
      case CLEAN:
        break;
    }

    if (success) {
      this.receipt.setStatus(TransactionReceipt.Status.SUCCESS);
    }
    else {
      this.receipt.setStatus(TransactionReceipt.Status.FAILURE);
    }
  }


}
