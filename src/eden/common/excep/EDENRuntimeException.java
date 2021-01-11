package eden.common.excep;

import static eden.common.excep.EDENException.NULL_PROBLEM;
import static eden.common.excep.EDENException.NULL_REMEDY;
import static eden.common.excep.EDENException.NULL_SUBJECT;
import static eden.common.excep.EDENException.makeMessage;

/**
 * An extension to Java's {@code RuntimeException} for a greater brevity in
 * error reporting. It follows a subject-problem-remedy structure, each of which
 * can be obtained with their respective accessor methods.
 * <p>
 * For simplicity, its {@code RuntimeException} parent omits the remedy message.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public class EDENRuntimeException extends RuntimeException {

//~~OBJECT CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** The subject that has the problem. */
  private final String subject;

  /** The problem that the subject has. */
  private final String problem;

  /** Suggested remedy to the problem. */
  private final String remedy;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Makes an {@code EDENException} with the given subject and problem. */
  public EDENRuntimeException(String subject, String problem) {
    this(subject, problem, null, null);
  }

  /**
   * Makes an {@code EDENRuntimeException} with the given subject, problem, and
   * remedy.
   */
  public EDENRuntimeException(String subject, String problem, String remedy) {
    this(subject, problem, remedy, null);
  }

  /**
   * Makes an {@code EDENRuntimeException} with the given subject, problem, and
   * cause.
   */
  public EDENRuntimeException(String subject, String problem, Throwable cause) {
    this(subject, problem, null, cause);
  }

  /**
   * Makes an {@code EDENRuntimeException} with the given subject, problem,
   * remedy, and cause.
   */
  public EDENRuntimeException(
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
  protected EDENRuntimeException() {
    this.subject = null;
    this.problem = null;
    this.remedy = null;
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
