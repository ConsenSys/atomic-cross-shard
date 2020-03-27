package tech.pegasys.teamx.crossshardsim;

public class AppLibrary {
  BeaconChain beaconChain;


  public AppLibrary(BeaconChain beaconChain) {
    this.beaconChain = beaconChain;
  }

  public void executeSingleShardTransaction(int shardId, Transaction transaction) {
    this.beaconChain.shards[shardId].submitTransaction(transaction);
  }

  public boolean notDoneYet() {
    return true;
  }

  public void processNextBlock() {
  }

}
