package eden.cpeaii;

import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {

  /** "A random number generator isolated to the current thread" */
  private final static ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

  public static int random(int min, int max) {
    return min == max ? min : RANDOM.nextInt(min, max);
  }

  public static int random(int min, int max, boolean allowZero) {
    if (min != max) {
      if (!allowZero) {
        int out;
        do
          out = RANDOM.nextInt(min, max);
        while (out == 0);
        return out;
      }
      return RANDOM.nextInt(min, max);
    }
    return min;
  }

  public static long random(long min, long max) {
    return min == max ? min : RANDOM.nextLong(min, max);
  }

  public static long random(long min, long max, boolean allowZero) {
    if (min != max) {
      if (!allowZero) {
        long out;
        do
          out = RANDOM.nextLong(min, max);
        while (out == 0);
        return out;
      }
      return RANDOM.nextLong(min, max);
    }
    return min;
  }

  public static double random(double min, double max) {
    return min == max ? min : RANDOM.nextDouble(min, max);
  }

  public static double random(double min, double max, boolean allowZero) {
    if (min != max) {
      if (!allowZero) {
        double out;
        do
          out = RANDOM.nextDouble(min, max);
        while (out == 0.0);
        return out;
      }
      return RANDOM.nextDouble(min, max);
    }
    return min;
  }
}
