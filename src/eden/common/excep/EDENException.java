package eden.common.excep;

import static eden.common.shared.Constants.LINE_WIDTH;

/**
 * An extension to Java's {@code Exception} for a greater brevity in error
 * reporting. It follows a subject-problem-remedy structure, each of which can
 * be obtained with their respective accessor methods.
 * <p>
 * For simplicity, its {@code Exception} parent omits the remedy message.
 * <p>
 * This class also consists of utility methods used in {@code
 * EDENRuntimeException} and possibly others in the {@link eden.common.excep}
 * package.
 *
 * @author Brendon
 * @version u0r0, under construction.
 * @see eden.common.excep
 */
public class EDENException extends Exception {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Filler for non-specified subjects. */
  static final String NULL_SUBJECT = "Unspecified Subject.";

  /** Filler for non-specified problems. */
  static final String NULL_PROBLEM = "Unspecified problem.";

  /** Filler for non-specified remedies. */
  static final String NULL_REMEDY = "No suggested remedy.";

//~~OBJECT CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** The subject that has the problem. */
  private final String subject;

  /** The problem that the subject has. */
  private final String problem;

  /** Suggested remedy to the problem. */
  private final String remedy;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes an {@code EDENException} with the given subject and problem. */
  public EDENException(String subject, String problem) {
    this(subject, problem, null, null);
  }

  /**
   * Makes an {@code EDENException} with the given subject, problem, and remedy.
   */
  public EDENException(String subject, String problem, String remedy) {
    this(subject, problem, remedy, null);
  }

  /**
   * Makes an {@code EDENException} with the given subject, problem, and cause.
   */
  public EDENException(String subject, String problem, Throwable cause) {
    this(subject, problem, null, cause);
  }

  /**
   * Makes an {@code EDENException} with the given subject, problem, remedy, and
   * cause.
   */
  public EDENException(
      String subject, String problem, String remedy, Throwable cause) {
    super(makeMessage(subject, problem, remedy), cause);
    this.subject
        = subject == null || subject.isEmpty() ? NULL_SUBJECT : subject;
    this.problem
        = problem == null || problem.isEmpty() ? NULL_PROBLEM : problem;
    this.remedy
        = remedy == null || remedy.isEmpty() ? NULL_REMEDY : remedy;
  }

  /** To prevent uninitialized instantiations of this class. */
  protected EDENException() {
    this.subject = null;
    this.problem = null;
    this.remedy = null;
  }

//~~CLASS METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Returns a string describing this {@code EDENException}. For simplicity, the
   * remedy message is omitted.
   */
  static String makeMessage(String subject, String problem, String remedy) {
    StringBuilder builder = new StringBuilder(LINE_WIDTH);
    if (subject != null)
      builder.append(subject).append(": ");
    if (problem != null)
      builder.append(problem);
    return builder.toString();
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Returns the subject that has the problem. */
  public String getSubject() {
    return this.subject;
  }

  /** Returns the problem that the subject has. */
  public String getProblem() {
    return this.problem;
  }

  /** Returns the suggested remedy to the problem. */
  public String getRemedy() {
    return this.remedy;
  }
}
