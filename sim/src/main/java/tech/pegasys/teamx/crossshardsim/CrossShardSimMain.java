package tech.pegasys.teamx.crossshardsim;

public class CrossShardSimMain {

  public static void main(String[] args) throws Exception {
    System.out.println("Start");


    BeaconChain beaconChain = new BeaconChain(5, 1);

    beaconChain.mineBlock();
    beaconChain.mineBlock();

    Application app = new Application(beaconChain);
    app.start(0);

    int numBlocks = 0;
    int maxBlocks = 20;

    while (app.notDoneYet() && (numBlocks++ < maxBlocks)) {
      System.out.println("Beacon Block Number: " + beaconChain.blockNumber);
      beaconChain.mineBlock();
      app.processNextBlock();
    }
    System.out.println("End");
  }
}
