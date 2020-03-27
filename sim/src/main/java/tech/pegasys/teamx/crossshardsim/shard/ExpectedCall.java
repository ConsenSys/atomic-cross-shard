package tech.pegasys.teamx.crossshardsim.shard;

public class ExpectedCall {
  public int shardId;
  public int address;
  public int value;
  public int param;
  public boolean lock;
  public ExpectedCall[] subCalls;

  public ExpectedCall(int shardId, int address, int value, int param, boolean lock) {
    this.shardId = shardId;
    this.address = address;
    this.value = value;
    this.param = param;
    this.lock = lock;
  }

  public ExpectedCall(int shardId, int address, int value, int param, boolean lock, ExpectedCall[] subordinateCalls) {
    this(shardId, address, value, param, lock);
    this.subCalls = subordinateCalls;
  }


}
