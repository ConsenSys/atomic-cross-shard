package tech.pegasys.teamx.crossshardsim.shard;

import tech.pegasys.teamx.crossshardsim.beacon.BeaconChain;

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
  public int shardId;
  int contractAddress;
  int value;
  int payload;
  ExpectedCall[] expectedCalls;

  TransactionReceipt receipt;


  public Transaction(Type type, int shard, int contractAddress, int value, int payload) {
    this.type = type;
    this.shardId = shard;
    this.contractAddress = contractAddress;
    this.value = value;
    this.payload = payload;
  }

  public Transaction(Type type, ExpectedCall call) {
    this(type, call.shardId, call.address, call.value, call.param);
    if (!(type == Type.SEGMENT || type == Type.ROOT)) {
      throw new Error("only segments or roots can be derived from an expected call");
    }
  }


  public Transaction(Type type, int shard, int contractAddress, int value, int payload, ExpectedCall[] expectedCalls) {
    this(type, shard, contractAddress, value, payload);
    if (type != Type.START) {
      throw new Error("only start can have expected calls");
    }
    this.expectedCalls = expectedCalls;
  }


  public void addReceipt(TransactionReceipt txr) {
    this.receipt = txr;
  }

  public void execute(Shard shard, BeaconChain beaconChain) {
    System.out.println("Executing transaction: Shard: " + shard.shardId + ", Contract: " + this.contractAddress + ", Transaction Type: " + this.type);
    boolean success = false;
    if (shard.shardId == this.shardId) {
      switch (this.type) {
        case NORMAL_SINGLE_CHAIN:
          success = shard.executeContractFunction(this.contractAddress, this.value, this.payload, false);
          break;
        case DEPLOY_NON_LOCKABLE:
          success = shard.deployContract(this.contractAddress, false);
          if (success) {
            success = shard.executeContractFunction(this.contractAddress, this.value, this.payload, false);
          }
          break;
        case DEPLOY_LOCKABLE:
          success = shard.deployContract(this.contractAddress, true);
          if (success) {
            success = shard.executeContractFunction(this.contractAddress, this.value, this.payload, false);
          }
          break;
        case START:
          break;
        case SEGMENT:
          success = shard.executeContractFunction(this.contractAddress, this.value, this.payload, true);
          break;
        case ROOT:
          success = shard.executeContractFunction(this.contractAddress, this.value, this.payload, true);
          break;
        case SIGNALLING:
          break;
        case CLEAN:
          break;
      }
    }

    if (success) {
      this.receipt.setStatus(TransactionReceipt.Status.SUCCESS);
    }
    else {
      this.receipt.setStatus(TransactionReceipt.Status.FAILURE);
    }
  }


}
