package eden.common.excep;

/**
 * Thrown when an application accesses a nullified object. This is an equivalent
 * to Java's {@code NullPointerException}.
 *
 * @author Brendon
 * @version u0r0, under construction.
 * @see eden.common.object.Nullifiable
 */
public class NullifiedObjectAccessException extends EDENRuntimeException {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Description of the problem. */
  private static final String PROBLEM
      = "The application was accessing a nullified object. This is usually an "
      + "application-side error.";

  /** Remedy to the problem. */
  private static final String REMEDY = "Make a new instance of this object.";

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a {@code NullifiedObjectAccessException}. */
  public NullifiedObjectAccessException() {
    this(null, null);
  }

  /**
   * Makes a {@code NullifiedObjectAccessException} with the given object name.
   */
  public NullifiedObjectAccessException(String object) {
    this(object, null);
  }

  /**
   * Makes a {@code NullifiedObjectAccessException} with the given object name
   * and cause.
   */
  public NullifiedObjectAccessException(String object, Throwable cause) {
    super(object, PROBLEM, REMEDY, cause);
  }
}
