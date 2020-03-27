package tech.pegasys.teamx.crossshardsim.app;

import tech.pegasys.teamx.crossshardsim.beacon.BeaconChain;
import tech.pegasys.teamx.crossshardsim.shard.ExpectedCall;
import tech.pegasys.teamx.crossshardsim.shard.Transaction;

public class Application {
  AppLibrary appLib;

  public Application(BeaconChain beaconChain) {
    this.appLib = new AppLibrary(beaconChain);
  }

  public void start(int scenario) {
    Transaction transaction;
    switch (scenario) {
      case 0:
        // Submit a single shard transaction on shard 0.
        transaction = new Transaction(Transaction.Type.DEPLOY_NON_LOCKABLE, 0, 0, 1, 2);
        this.appLib.executeSingleShardTransaction(transaction);

        break;

      case 1:
        // Deploy contracts
        transaction = new Transaction(Transaction.Type.DEPLOY_LOCKABLE, 1, 2, 1, 2);
        this.appLib.executeSingleShardTransaction(transaction);
        transaction = new Transaction(Transaction.Type.DEPLOY_LOCKABLE, 2, 3, 1, 2);
        this.appLib.executeSingleShardTransaction(transaction);

        // Execute cross shard transaction.
        // Have a contract on shard 1 do a call to a contract on shard 2.
        ExpectedCall tSeg1 = new ExpectedCall(2, 3, 3, 1, true);
        ExpectedCall tRoot = new ExpectedCall( 1, 2, 1, 5, true, new ExpectedCall[]{tSeg1});
        this.appLib.executeCrossShardTransaction(tRoot);

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
