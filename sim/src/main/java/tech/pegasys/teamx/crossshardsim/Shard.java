package tech.pegasys.teamx.crossshardsim;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

public class Shard {

  Queue<Transaction> transactionPool = new ArrayDeque<>();
  public ArrayList<ShardBlock> shardChain = new ArrayList<>();

  Map<Integer, Contract> contracts = new TreeMap<>();

  int numTransactionsPerBlock;
  int blockNumber = 0;
  int shardId;
  BeaconChain beaconChain;


  public Shard(int shardId, int numTransactionsPerBlock, BeaconChain beaconChain) {
    this.shardId = shardId;
    this.numTransactionsPerBlock = numTransactionsPerBlock;
    this.beaconChain = beaconChain;
  }


  public TransactionReceipt submitTransaction(Transaction transaction) {
    TransactionReceipt receipt = new TransactionReceipt();
    transaction.addReceipt(receipt);
    this.transactionPool.add(transaction);
    return receipt;
  }

  public CrossLink mineBlock() {
    ShardBlock block = new ShardBlock(this.blockNumber++);

    int numTransactionsAdded = 0;
    while (numTransactionsAdded < this.numTransactionsPerBlock) {
      Transaction transaction = this.transactionPool.poll();
      if (transaction == null) {
        // No more transactions in the transaction pool
        break;
      }
      transaction.execute(this, this.beaconChain);
      block.add(transaction);
    }
    this.shardChain.add(block);

    return calculateCrossLink();
  }

  private CrossLink calculateCrossLink() {
    return null;
  }


  public boolean deployContract(int address, boolean lockable) {
    Contract con = this.contracts.get(address);
    if (con != null) {
      // Contract already exists.
      return false;
    }
    this.contracts.put(address, new Contract(address, lockable));
    return true;
  }

  public boolean executeContractFunction(int address, int value, int payload, boolean lock) {
    System.out.println(" Executing function: Shard: " + this.shardId + ", Contract: " + address);
    Contract con = this.contracts.get(address);
    if (con == null) {
      // Contract doesn't exists.
      return false;
    }
    return con.executeContractFunction(value, payload, lock);
  }

}
