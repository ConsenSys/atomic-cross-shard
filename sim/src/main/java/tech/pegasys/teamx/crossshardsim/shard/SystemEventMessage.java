package tech.pegasys.teamx.crossshardsim.shard;

import tech.pegasys.teamx.crossshardsim.util.SimpleHash;

public class SystemEventMessage {
  enum Type {
    START
  }

  public Type type;

  public int shardId;
  int contractAddress;
  int value;
  int payload;
  ExpectedCall[] expectedCalls;

  public SystemEventMessage(Transaction transaction) {
    this.shardId = transaction.shardId;
    this.contractAddress = transaction.contractAddress;
    this.value = transaction.value;
    this.payload = transaction.payload;

    switch (transaction.type) {
      case START:
        this.type = Type.START;
        this.expectedCalls = transaction.expectedCalls;
        break;
    }



  }



  public int calculateRoot() {
    return SimpleHash.hash(this.shardId, this.contractAddress, this.value, this.payload);
    // TODO am ignoring expected calls.
  }

}
