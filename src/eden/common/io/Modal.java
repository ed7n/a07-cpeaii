package eden.common.io;

import eden.common.excep.NullifiedObjectException;
import eden.common.object.Dieable;
import eden.common.object.Nullifiable;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Objects;

import static eden.common.shared.Constants.STDOUT;

/**
 * Encapsulates a {@code PrintStream} for the printing of tagged messages. An
 * example of so follows:
 * <p>
 * <code>[EDEN/i] This is an information message</code>
 * <p>
 * The text enclosed in brackets ([]) is the tag, containing the identifier:
 * EDEN, followed by the message mode ({@code i}). Finally, the actual message
 * follows after the tag, separated by a space ( ) character.
 * <p>
 * This version of the {@code Modal} class implements six message modes: {@link
 * #PROMPT}, {@link #INFO}, {@link #ALERT}, {@link #ERROR}, {@link #INPUT}, and
 * {@link #DEBUG}, each of which is assigned a filter bit within an eight-bit
 * (one-byte) word, which can denote up to eight possible simultaneous modes,
 * the two unused bits are reserved for future extensions.
 * <p>
 * For transparency, use the constants provided to create mode filter
 * patterns--which can be done by chaining the desired modes with a bitwise OR
 * operator ({@code |})--and to specify message modes.
 * <p>
 * Extra caution must be exercised when using a {@code PrintStream} that wraps
 * either {@code System.out} or {@code System.err}. See {@link #close()} for
 * details.
 * <p>
 * For transitional convenience, all but the {@code write} methods of this class
 * are symbolically compatible to the ones in Java's {@code PrintStream}, and
 * that their behaviors are about the same.
 *
 * @author Brendon
 * @version u0r6, 04/18/2020.
 */
public class Modal implements Appendable, Closeable, Dieable, Nullifiable {

//~~CLASS CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Message mode: prompt. */
  public static final byte PROMPT = (byte) 0b00000001;

  /** Message mode: information. */
  public static final byte INFO = (byte) 0b00000010;

  /** Message mode: alert. */
  public static final byte ALERT = (byte) 0b00000100;

  /** Message mode: error. */
  public static final byte ERROR = (byte) 0b00001000;

  /** Message mode: input. This mode is a subset of {@link #PROMPT}. */
  public static final byte INPUT = (byte) 0b00010001;

  /** Message mode: debug. */
  public static final byte DEBUG = (byte) 0b10000000;

//~~OBJECT CONSTANTS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** PrintStream to which messages are to be printed. */
  protected final PrintStream stream;

  /** Identifier. */
  protected final String name;

  /** Filter bit pattern. */
  protected final byte filter;

//~~OBJECT FIELDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /** Throwable defining the death of this Modal. */
  protected Throwable deathCause;

  /** Current message mode. To be used in conjunction with inline. */
  protected byte mode;

  /**
   * Indicates whether the next print will be inline. This is set to true after
   * a print, and held until the next print is either of a different mode, or
   * followed by a line separator only with println.
   */
  protected boolean inline = false;

//~~CONSTRUCTORS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Makes an unfiltered {@code Modal} with the given name and {@code
   * System.out}.
   */
  public Modal(String name) throws IllegalArgumentException {
    this(name, STDOUT, 255);
  }

  /**
   * Makes an unfiltered {@code Modal} with the given name and {@code
   * PrintStream}.
   */
  public Modal(String name, PrintStream stream)
      throws IllegalArgumentException {
    this(name, stream, 255);
  }

  /**
   * Makes a {@code Modal} with the given name, {@code PrintStream}, and filter
   * bits.
   */
  public Modal(String name, PrintStream stream, int filter) {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(stream, "stream");
    this.name = name;
    this.stream = stream;
    this.filter = (byte) (filter & 255);
  }

//~~OBJECT METHODS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Flushes the {@code PrintStream} of this {@code Modal}, then checks and
   * returns its error state.
   */
  public boolean checkError() {
    return getPrintStream().checkError();
  }

  /** Flushes the {@code PrintStream} of this {@code Modal}. */
  public void flush() {
    getPrintStream().flush();
  }

  /** Marks this {@code Modal} dead with the given cause. */
  public void die(Throwable cause) {
    this.deathCause = cause;
  }

  /**
   * Prints the given formatted {@code String} using the default {@code Locale}
   * with the given arguments. The default {@code Locale} is obtained with
   * {@code Locale.getDefault()}.
   */
  public Modal format(String format, Object... arguments) {
    return format(Locale.getDefault(), format, arguments);
  }

  /**
   * Prints the given formatted {@code String} using the given {@code Locale}
   * with the given arguments.
   *
   * @param locale Localization to apply during formatting. Passing a
   * {@code null} skips this.
   */
  public Modal format(Locale locale, String format, Object... arguments) {
    print(String.format(locale, format, arguments));
    return this;
  }

  /** Prints this {@code Modal}. */
  public void print() {
    print(this.toString());
  }

  /** Prints the given {@code boolean}. */
  public void print(boolean b) {
    print(Boolean.toString(b));
  }

  /** Prints the given {@code byte}. */
  public void print(byte b) {
    print(Byte.toString(b));
  }

  /** Prints the given {@code char}. */
  public void print(char c) {
    print(Character.toString(c));
  }

  /** Prints the given {@code short}. */
  public void print(short s) {
    print(Short.toString(s));
  }

  /** Prints the given {@code int}. */
  public void print(int i) {
    print(Integer.toString(i));
  }

  /** Prints the given {@code long}. */
  public void print(long l) {
    print(Long.toString(l));
  }

  /** Prints the given {@code float}. */
  public void print(float f) {
    print(Float.toString(f));
  }

  /** Prints the given {@code double}. */
  public void print(double d) {
    print(Double.toString(d));
  }

  /** Prints the given {@code CharSequence}. */
  public void print(CharSequence charSequence) {
    print(charSequence.toString());
  }

  /** Prints the given {@code Object}. */
  public void print(Object obj) {
    print(String.valueOf(obj));
  }

  /** Prints the given {@code String}. */
  public void print(String string) {
    print(string, Modal.INFO);
  }

  /** Prints the given message with the given mode. */
  public void print(String message, int mode) {
    if (isObjectDead())
      return;
    byte pattern = (byte) (mode & 255);
    if ((getFilter() & pattern) == 0)
      return;
    char symbol = getSymbol(pattern);
    this.inline = pattern == this.mode;
    if (!this.inline) {
      getPrintStream().print("[" + getName() + "/" + symbol + "] " + message);
      this.mode = pattern;
    } else
      getPrintStream().print(message);
  }

  /** Alias to {@link #format(String, Object...)}. */
  public Modal printf(String format, Object... arguments) {
    return format(format, arguments);
  }

  /** Alias to {@link #format(Locale, String, Object...)}. */
  public Modal printf(Locale locale, String format, Object... arguments) {
    return format(locale, format, arguments);
  }

  /** Prints this {@code Modal} followed by the line separator. */
  public void println() {
    println(this.toString());
  }

  /** Prints the given {@code boolean} followed by the line separator. */
  public void println(boolean b) {
    println(Boolean.toString(b));
  }

  /** Prints the given {@code byte} followed by the line separator. */
  public void println(byte b) {
    println(Byte.toString(b));
  }

  /** Prints the given {@code char} followed by the line separator. */
  public void println(char c) {
    println(Character.toString(c));
  }

  /** Prints the given {@code short} followed by the line separator. */
  public void println(short s) {
    println(Short.toString(s));
  }

  /** Prints the given {@code int} followed by the line separator. */
  public void println(int i) {
    println(Integer.toString(i));
  }

  /** Prints the given {@code long} followed by the line separator. */
  public void println(long l) {
    println(Long.toString(l));
  }

  /** Prints the given {@code float} followed by the line separator. */
  public void println(float f) {
    println(Float.toString(f));
  }

  /** Prints the given {@code double} followed by the line separator. */
  public void println(double d) {
    println(Double.toString(d));
  }

  /** Prints the given {@code CharSequence} followed by the line separator. */
  public void println(CharSequence charSequence) {
    println(charSequence.toString());
  }

  /** Prints the given {@code Object} followed by the line separator. */
  public void println(Object obj) {
    println(String.valueOf(obj));
  }

  /** Prints the given {@code String} followed by the line separator. */
  public void println(String string) {
    println(string, Modal.INFO);
  }

  /**
   * Prints the given message as the given mode followed by the line separator.
   */
  public void println(String message, int mode) {
    if (isObjectDead())
      return;
    byte pattern = (byte) (mode & 255);
    if ((getFilter() & pattern) == 0)
      return;
    char symbol = getSymbol(pattern);
    if (pattern != this.mode)
      getPrintStream().println("[" + getName() + "/" + symbol + "] " + message);
    else
      getPrintStream().println(message);
    this.mode = 0;
    this.inline = false;
  }

  /** Returns the {@code PrintStream} of this {@code Modal}. */
  public PrintStream getPrintStream() {
    return this.stream;
  }

  /** Returns the identifier of this {@code Modal}. */
  public String getName() {
    return this.name;
  }

  /** Returns the filter bit pattern of this {@code Modal}. */
  public byte getFilter() {
    return this.filter;
  }

  /** {@inheritDoc} */
  @Override
  public Throwable getObjectDeathCause() {
    return this.deathCause;
  }

  /** {@inheritDoc} */
  @Override
  public void nullifyObject() {
    getPrintStream().close();
    this.mode = 0;
    this.inline = false;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isObjectNullified() {
    return getObjectDeathCause() == NullifiedObjectException.NUL;
  }

  /**
   * Shorthand to {@link #print(CharSequence)}, with the addition of returning
   * this {@code Modal}.
   */
  @Override
  public Modal append(CharSequence charSequence) {
    print(charSequence);
    return this;
  }

  /**
   * Prints the given {@code CharSequence} at the given index range and returns
   * this {@code Modal}.
   */
  @Override
  public Modal append(CharSequence charSequence, int start, int end) {
    if (charSequence == null)
      print("null");
    else {
      if (start < 0 || end < 0 || start > end || end > charSequence.length())
        throw new IndexOutOfBoundsException(
            "[start, end]: [" + start + ", " + end
            + "] > [0, " + charSequence.length() + "]");
      print(charSequence.subSequence(start, end).toString());
    }
    return this;
  }

  /**
   * Shorthand to {@link #print(char)}, with the addition of returning this
   * {@code Modal}.
   */
  @Override
  public Modal append(char character) {
    print(character);
    return this;
  }

  /**
   * Closes the {@code PrintStream} of this {@code Modal}, enabling the dead
   * flag.
   * <p>
   * An exception applies if the {@code PrintStream} is either {@code
   * System.out} or {@code System.err}: They will not be closed, though their
   * encapsulating {@code Modal} will still be flagged as dead. However, any
   * {@code PrintStream} that wraps {@code System.out} or {@code System.err}
   * will still be closed.
   */
  @Override
  public void close() {
    if (isObjectDead())
      return;
    if (getPrintStream() != System.out && getPrintStream() != System.err) {
      getPrintStream().close();
      die(new IOException("Stream closed"));
    }
  }

  protected char getSymbol(byte pattern) {
    if (isUnusedFilter(pattern))
      throw new IllegalArgumentException(
          "isUnusedFilter: " + Integer.toString(filter & 96, 2));
    char out;
    if ((pattern & PROMPT) != 0)
      if ((pattern & INPUT) != PROMPT)
        out = '_';
      else
        out = '?';
    else if ((pattern & INFO) != 0)
      out = 'i';
    else if ((pattern & ALERT) != 0)
      out = '!';
    else if ((pattern & ERROR) != 0)
      out = 'X';
    else if ((pattern & DEBUG) != 0)
      out = '$';
    else
      throw new RuntimeException("Fatal");
    return out;
  }

  protected boolean isValidFilterPattern(int pattern) {
    return (pattern & -256) == 0;
  }

  protected boolean isUnusedFilter(byte pattern) {
    return (pattern & 96) != 0;
  }
}
