package tech.pegasys.teamx.crossshardsim.util;


// This is not a cryptographic hash. It should provide a way of combining the numbers though.
public class SimpleHash {
  static int LARGE_PRIME = 1000003;

  public static int hash(int v1) {
    return (v1 * 7) % LARGE_PRIME;
  }

  public static int hash(int v1, int v2) {
    return (v1 * 7 + v2 * 13) % LARGE_PRIME;
  }
  public static int hash(int v1, int v2, int v3) {
    return (v1 * 7 + v2 * 13 + v3 * 17) % LARGE_PRIME;
  }
  public static int hash(int v1, int v2, int v3, int v4) {
    return (v1 * 7 + v2 * 13 + v3 * 17 + v4*19) % LARGE_PRIME;
  }

  public static int hash(int v1, int v2, int v3, int v4, int v5) {
    return (v1 * 7 + v2 * 13 + v3 * 17 + v4*19 + v5*23) % LARGE_PRIME;
  }
}
