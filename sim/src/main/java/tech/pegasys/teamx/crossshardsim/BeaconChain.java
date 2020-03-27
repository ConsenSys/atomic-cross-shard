package tech.pegasys.teamx.crossshardsim;

import java.util.ArrayList;

public class BeaconChain {
  public ArrayList<BeaconBlock> beaconChain = new ArrayList<>();


  int blockNumber = 0;

  public Shard[] shards;

  public BeaconChain(int numShards, int numTransactionsPerShardBlock) {
    this.shards = new Shard[numShards];
    for (int i = 0; i < numShards; i++) {
      this.shards[i] = new Shard(i, numTransactionsPerShardBlock, this);
    }
  }


  public void mineBlock() {
    CrossLink[] crossLinks = new CrossLink[this.shards.length];
    for (int i = 0; i < this.shards.length; i++) {
      crossLinks[i] = this.shards[i].mineBlock();
    }
    this.beaconChain.add(new BeaconBlock(this.blockNumber++, crossLinks));
  }
}
