package tech.pegasys.teamx.crossshardsim.shard;

import java.util.ArrayList;

public class ShardBlock {
  int blockNumber;
  public ArrayList<Transaction> transactions = new ArrayList<>();

  public ShardBlock(int blockNumber) {
    this.blockNumber = blockNumber;
  }

  public void add(Transaction transaction) {
    this.transactions.add(transaction);
  }



}
