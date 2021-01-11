package eden.common.excep;

/**
 * Thrown when an application accesses a dead object. Such must be prevented for
 * safety.
 *
 * @author Brendon
 * @version u0r0, under construction.
 * @see eden.common.object.Dieable
 */
public class DeadObjectAccessException extends EDENRuntimeException {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Description of the problem. */
  private static final String PROBLEM
      = "The application was accessing a dead object. This is usually caused "
      + "by an abnormal state with an external resource.";

  /** Remedy to the problem. */
  private static final String REMEDY
      = "Unless revival is possible, make a new instance of it.";

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a {@code DeadObjectAccessException}. */
  public DeadObjectAccessException() {
    this(null, null);
  }

  /** Makes a {@code DeadObjectAccessException} with the given object name. */
  public DeadObjectAccessException(String object) {
    this(object, null);
  }

  /**
   * Makes a {@code DeadObjectAccessException} with the given object name and
   * cause.
   */
  public DeadObjectAccessException(String object, Throwable cause) {
    super(object, PROBLEM, REMEDY, cause);
  }
}
