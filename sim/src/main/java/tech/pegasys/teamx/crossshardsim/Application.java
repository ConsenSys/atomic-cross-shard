package tech.pegasys.teamx.crossshardsim;

public class Application {
  AppLibrary appLib;

  public Application(BeaconChain beaconChain) {
    this.appLib = new AppLibrary(beaconChain);
  }

  public void start(int scenario) {
    switch (scenario) {
      case 0:
        // Submit a single shard transaction on shard 0.
        Transaction transaction = new Transaction(Transaction.Type.DEPLOY_NON_LOCKABLE, 0, 1, 2);
        this.appLib.executeSingleShardTransaction(0, transaction);

        break;
    }
  }


  public boolean notDoneYet() {
    return this.appLib.notDoneYet();
  }


  public void processNextBlock() {
    this.appLib.processNextBlock();
  }



}
