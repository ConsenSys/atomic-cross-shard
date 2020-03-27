package tech.pegasys.teamx.crossshardsim;

public class BeaconBlock {
  int blockNumber;
  CrossLink[] crossLinks;

  public BeaconBlock(int blockNumber, CrossLink[] links) {
    this.blockNumber = blockNumber;
    this.crossLinks = links;
  }

  public CrossLink getCrossLink(int shardId) {
    return this.crossLinks[shardId];
  }


}
