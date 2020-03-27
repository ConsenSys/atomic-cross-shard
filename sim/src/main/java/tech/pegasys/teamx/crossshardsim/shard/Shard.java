package tech.pegasys.teamx.crossshardsim.shard;

import tech.pegasys.teamx.crossshardsim.beacon.BeaconChain;
import tech.pegasys.teamx.crossshardsim.beacon.CrossLink;
import tech.pegasys.teamx.crossshardsim.util.SimpleHash;

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

  int oldCrossLink = 1;


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

    return calculateCrossLink(block);
  }

  private CrossLink calculateCrossLink(ShardBlock block) {
    // TODO don't include state root into the cross link, otherwise historical information needs to be kept.
    int stateRoot = 1;
    int transRoot1 = 1;
    int transRoot2 = 1;
    int transactionRoot;
    switch (block.transactions.size()) {
      case 0:
        break;
      case 1:
        transRoot1 = block.transactions.get(0).calculateRoot();
        break;
      case 2:
        transRoot1 = block.transactions.get(0).calculateRoot();
        transRoot2 = block.transactions.get(1).calculateRoot();
        break;
      default:
        throw new Error("more than two transactions per block not currently supported");
    }
    transactionRoot = SimpleHash.hash(transRoot1, transRoot2);

    return new CrossLink(transactionRoot);
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
    Contract con = this.contracts.get(address);
    if (con == null) {
      // Contract doesn't exists.
      return false;
    }
    return con.executeContractFunction(value, payload, lock);
  }

  @Override
  public String toString() {
    String info = "Shard Id: " + this.shardId + ", Block Number: " + this.blockNumber;
    for (Contract contract: this.contracts.values()) {
      info = info + ", [" + contract.toString() + "]";
    }
    return info;
  }
}
