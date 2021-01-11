package eden.common.shared;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * The {@code Constants} class consists of constants used in various parts of
 * the API.
 *
 * @author Brendon
 * @version u0r0, under construction.
 */
public final class Constants {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  public static final InputStream STDIN = System.in;
  public static final PrintStream STDOUT = System.out;
  public static final PrintStream STDERR = System.err;
  public static final String DOUBLE_QUOTE = "\"";
  public static final String EOL = System.lineSeparator();
  public static final String NUL_STRING = "";
  public static final String SPACE = " ";
  public static final int BYTES_CAPACITY = 4096;
  public static final int EXIT_FAILURE = 1;
  public static final int EXIT_SUCCESS = 0;
  public static final int LINE_WIDTH = 80;
  public static final int STRING_CAPACITY = 1024;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** To prevent instantiations of this class. */
  private Constants() {
  }
}
