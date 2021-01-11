package eden.cpeaii;

import java.awt.*;
import java.awt.geom.*;

import eden.common.io.Modal;

public class BlasterFactory {

  private static final Modal modal = new Modal("BlasterFactory");

  // color
  /** Outline color of a Blaster to be drawn with */
  public final static Color OUTLINE = VectorSprite.TRANSPARENT;

  /** Fill color of a Blaster to be filled with */
  public final static Color FILL = new Color(192, 192, 255);

  // boundaries
  /** Rectangular bounds in which an Asteroid will be made */
  private Rectangle2D makeBounds;

  // attributes
  /** Defines whether or not the BlasterFactory is powered */
  private boolean power;

  /** Strength of the BlasterFactory. Higher levels brings about more powerful
   * Blasters.
   */
  private byte level;

  /** Defines whether or not the BlasterFactory is ready to make a Blaster */
  private boolean ready;

  /** Duration in which the BlasterFactory transitions from a non-ready to a
   * ready state. This is not measured in any time unit. The BlasterFactory
   * readies when its counter reaches the timeout, which can be achieved by
   * repeatedly invoking the update() method.
   */
  private byte timeout;

  // interaction
  /** Effect upon intersection between a Blaster and another Sprite */
  private Effect effect;

  // automation
  /** Used for time-based behaviors */
  private byte counter;

  public BlasterFactory(
      Rectangle2D makeBounds, boolean power, int level, int timeout,
      Effect effect) {
    // boundaries
    this.makeBounds = makeBounds;
    // attributes
    this.power = power;
    this.level = (byte) level;
    this.ready = true;
    this.timeout = (byte) timeout;
    // interaction
    this.effect = effect;
    // automation
    this.counter = 0;
  }

  public Blaster make(int dX, int dY) {
    if (isPowered() && isReady()) {
      this.ready = false;
      return new BlasterImpl(
          (int) this.makeBounds.getCenterX(),
          (int) this.makeBounds.getCenterY(),
          level, dX, dY, true);
    }
    return null;
  }

  /** Updates the BlasterFactory. This method updates the ready state as
   * necessary.
   */
  public void update() {
    if (!this.ready)
      if (++this.counter >= this.timeout) {
        this.ready = true;
        this.counter = 0;
      }
  }

  /** Returns the rectangular bounds within which an Asteroid will be made
   *
   * @return make bounds of the AsteroidFactory
   */
  public Rectangle2D getMakeBounds() {
    return this.makeBounds;
  }

  /** Sets the rectangular bounds within which an Asteroid will be made
   *
   * @param makeBounds new make bounds of the AsteroidFactory
   */
  public void setMakeBounds(Rectangle2D makeBounds) {
    this.makeBounds = makeBounds;
  }

  /** Returns whether or not the BlasterFactory is powered
   *
   * @return true if the condition is met; false otherwise
   */
  public boolean isPowered() {
    return this.power;
  }

  /** Sets whether or not the BlasterFactory is powered
   *
   * @param power new power state
   */
  public void setPower(boolean power) {
    this.power = power;
  }

  /** Toggles whether or not the BlasterFactory is powered */
  public void togglePower() {
    this.power = !isPowered();
  }

  /** Returns the strength level of the BlasterFactory
   *
   * @return strength of the BlasterFactory
   */
  public byte getLevel() {
    return this.level;
  }

  /** Sets the strength level of the BlasterFactory
   *
   * @param level new strength of the BlasterFactory
   */
  public void setLevel(int level) {
    if ((this.level <= Byte.MAX_VALUE) && (level > 1))
      this.level = (byte) level;
    else
      modal.println(" BAD LVL", Modal.ERROR);
  }

  /** Returns whether or not the BlasterFactory is ready to make a Blaster
   *
   * @return true if the condition is met; false otherwise
   */
  public boolean isReady() {
    return this.ready;
  }

  /** Returns the duration in which the BlasterFactory transitions from a
   * non-ready to a ready state
   *
   * @return timeout of the BlasterFactory
   */
  public byte getTimeout() {
    return this.timeout;
  }

  /** Sets the duration in which the BlasterFactory transitions from a non-ready
   * to a ready state
   *
   * @param timeout new timeout of the BlasterFactory
   */
  public void setTimeout(int timeout) {
    if (timeout > Byte.MAX_VALUE)
      this.timeout = Byte.MAX_VALUE;
    else if (timeout > 0)
      this.timeout = (byte) timeout;
    else
      modal.println(" BAD TIME", Modal.ERROR);
  }

  /** Returns the Effect upon intersection between an Asteroid and another
   * Sprite
   *
   * @return effect of the AsteroidFactory
   */
  public Effect getEffect() {
    return this.effect;
  }

  /** Sets the Effect upon intersection between an Asteroid and another Sprite
   *
   * @param effect newEffect new effect of the AsteroidFactory
   */
  public void setEffect(Effect effect) {
    this.effect = effect;
  }

  private static class BlasterImpl extends VectorSprite implements Blaster {

    // geometry
    /** Width of a Blaster */
    public static final int WIDTH = 12;

    /** Height of a Blaster */
    public static final int HEIGHT = 2;

    // velocity
    /** Base speed of a Blaster. Actual speed is level-dependent. */
    public static final byte BASE_SPEED = 12;

    public BlasterImpl(
        int x, int y, int level, int dX, int dY, boolean visibility) {
      super(dX, dY, (int) (BASE_SPEED * ((level / 2) + 0.5)), visibility);
      // visual
      x -= WIDTH / 2;
      y -= HEIGHT / 2;
      Polygon polygon = new Polygon(
          new int[]{
            0, (WIDTH / 6), (int) (WIDTH * (5.0 / 6)), WIDTH, (int) (WIDTH
            * (5.0 / 6)), (WIDTH / 6), 0},
          new int[]{
            (HEIGHT / 2), 0, 0, (HEIGHT / 2), HEIGHT, HEIGHT, (HEIGHT / 2)},
          7);
      polygon.translate(x, y);
      this.path2d = new Path2D.Double(polygon);
      this.outline = OUTLINE;
      this.fill = FILL;
    }
  }
}
