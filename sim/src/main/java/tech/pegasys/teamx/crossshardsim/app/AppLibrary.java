package tech.pegasys.teamx.crossshardsim.app;

import tech.pegasys.teamx.crossshardsim.beacon.BeaconChain;
import tech.pegasys.teamx.crossshardsim.shard.ExpectedCall;
import tech.pegasys.teamx.crossshardsim.shard.Transaction;

// App library can only send one transaction at a time at the moment.
public class AppLibrary {
  enum States {
    SEGMENTS,
    ROOT,
    SIGNALLING,
    CLEAN,
    NONE
  }

  States nextState;
  boolean transactionDone = true;
  int callDepthFromLeaves = 0;
  ExpectedCall rootCall;

  BeaconChain beaconChain;


  public AppLibrary(BeaconChain beaconChain) {
    this.beaconChain = beaconChain;
  }

  public void executeSingleShardTransaction(Transaction transaction) {
    submitTransaction(transaction);
  }

  public void executeCrossShardTransaction(ExpectedCall rootCall) {
    Transaction startTransaction = new Transaction(Transaction.Type.START, rootCall.shardId, rootCall.address, rootCall.value, rootCall.param, rootCall.subCalls);
    submitTransaction(startTransaction);
    this.nextState = States.SEGMENTS;
    this.transactionDone = false;
    this.callDepthFromLeaves = 0;
    this.rootCall = rootCall;
  }
  private void submitTransaction(Transaction transaction) {
    this.beaconChain.shards[transaction.shardId].submitTransaction(transaction);
  }


  public boolean notDoneYet() {
    return !this.transactionDone;
  }

  public void processNextBlock() {
    switch (this.nextState) {
      case NONE:
        return;
      case SEGMENTS:
        // We need to dispatch the leaf transactions, and then the transactions that call the leaves,
        // and then the transactions that call the transactions that call the leaves, etc, until we are
        // at the root transaction.

        // TODO For the moment, lets just handle leaf and root.
        // TODO: need to pass in proof that this segment is part of the call tree described in the START
        ExpectedCall[] calls = this.rootCall.subCalls;
        if (calls != null) {
          for (ExpectedCall call: calls) {
            Transaction seg = new Transaction(Transaction.Type.SEGMENT, call);
            submitTransaction(seg);
          }
        }
        this.nextState = States.ROOT;
        break;
      case ROOT:
        Transaction root = new Transaction(Transaction.Type.ROOT, this.rootCall);
        submitTransaction(root);
        this.nextState = States.SIGNALLING;
        break;
      case SIGNALLING:
        // TODO for the moment skip this.
        this.nextState = States.CLEAN;
        break;
      case CLEAN:
        // TODO for the moment skip this.
        this.nextState = States.NONE;
        break;
      default:
        throw new Error("unknown next state");
    }
  }

}
