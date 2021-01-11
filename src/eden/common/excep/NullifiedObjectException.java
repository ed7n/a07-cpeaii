package eden.common.excep;

/**
 * Placeholder death cause for {@code Dieables} that have been nullified.
 *
 * @author Brendon
 * @version u0r0, under construction.
 * @see eden.common.object.Dieable
 * @see eden.common.object.Nullifiable
 */
public class NullifiedObjectException extends EDENRuntimeException {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Null instance. */
  public static final NullifiedObjectException NUL
      = new NullifiedObjectException();

  /** Description of the problem. */
  private static final String PROBLEM = "This object has been nullified.";

  /** Remedy/ies to the problem. */
  private static final String REMEDY = "Make a new instance of this object.";

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes a {@code NullifiedObjectException}. */
  public NullifiedObjectException() {
    this(null, null);
  }

  /** Makes a {@code NullifiedObjectException} with the given object name. */
  public NullifiedObjectException(String object) {
    this(object, null);
  }

  /**
   * Makes a {@code NullifiedObjectException} with the given object name and
   * cause.
   */
  public NullifiedObjectException(String object, Throwable cause) {
    super(object, PROBLEM, REMEDY, cause);
  }
}
