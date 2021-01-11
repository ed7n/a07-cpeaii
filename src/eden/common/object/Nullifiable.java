package eden.common.object;

import eden.common.excep.NullifiedObjectAccessException;

/**
 * A {@code Nullifiable} can be nullified to prevent further operations on it.
 * <p>
 * Classes implementing this interface must be able to modify their object
 * fields to hide or obscure them. For semantic consistency, nullified objects
 * should not be and not able to be reused.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public interface Nullifiable {

//~~INTERFACE METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Ensures that this {@code Nullifiable} is not nullified.
   *
   * @throws NullifiedObjectAccessException If this {@code Nullifiable} is
   * nullified.
   */
  default Nullifiable requireNonNullified() {
    if (isObjectNullified()) {
      Object o = this;
      throw new NullifiedObjectAccessException(o.toString());
    }
    return this;
  }

  /**
   * Nullifies this {@code Nullifiable}, preventing further operations on it.
   * This is usually done by zeroing all its fields.
   */
  void nullifyObject();

  /** Returns whether this {@code Nullifiable} is nullified. */
  boolean isObjectNullified();
}
