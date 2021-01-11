package eden.common.object;

import eden.common.excep.DeadObjectAccessException;

/**
 * A {@code Dieable} is marked dead if something abnormal happens to it.
 * <p>
 * Classes implementing this interface usually operates on external resources
 * that may result in abnormalities. When such are detected, they must mark
 * their objects dead, preventing further operations for safety.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public interface Dieable {

//~~INTERFACE METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Ensures that this {@code Dieable} is not dead.
   *
   * @throws DeadObjectAccessException if this {@code Dieable} is dead.
   */
  default Dieable requireNonDead() throws DeadObjectAccessException {
    if (isObjectDead()) {
      Object o = this;
      throw new DeadObjectAccessException(o.toString());
    }
    return this;
  }

  /** Returns the cause of this {@code Dieable's} death. */
  Throwable getObjectDeathCause();

  /** Returns whether this {@code Dieable} is dead. */
  default boolean isObjectDead() {
    return getObjectDeathCause() != null;
  }

  //private void killObject();
}
