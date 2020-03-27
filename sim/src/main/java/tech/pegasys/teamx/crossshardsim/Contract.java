package tech.pegasys.teamx.crossshardsim;

public class Contract {
  boolean lockable;
  boolean locked;
  int address;

  int provisionalValue;
  int provisionalState;

  int value;
  int state;

  public Contract(int address, boolean lockable) {
    this.address = address;
    this.lockable = lockable;
  }

  public boolean executeContractFunction(int value, int payload, boolean lock) {
    if (this.locked) {
      // Can't execute a locked contract.
      return false;
    }

    if (value == 0 && payload == 0) {
      // There is no state update - don't worry about locking.
      return true;
    }

    int tempValue = this.value + value;
    int tempState = this.state + payload;

    if (lock) {
      // Transaction Segment
      if (this.lockable) {
        // Locking a lockable contract.
        this.locked = true;
        this.provisionalValue = tempValue;
        this.provisionalState = tempState;
      }
      else {
        // Attempt to lock a non-lockable contract.
        return false;
      }
    }
    else {
      // Normal single chain transaction.
      this.value = tempValue;
      this.state = tempState;
    }
    return true;
  }




}
