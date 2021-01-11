package eden.cpeaii;

import java.awt.*;
import java.awt.geom.*;

public class AsteroidFactory {

  // colors
  /** Outline color of an Asteroid to be drawn with */
  public final static Color OUTLINE = new Color(191, 191, 191);

  /** Fill color of an Asteroid to be filled with */
  public Color fill;

  // boundaries
  /** Rectangular bounds in which an Asteroid will be made */
  private Rectangle2D makeBounds;

  /** Rectangular bounds in which an Asteroid is visible */
  private Rectangle2D viewBounds;

  // geometry
  /** The radius of an Asteroid to be made with */
  private byte radius;

  // interaction
  /** Effect upon intersection between an Asteroid and another Sprite */
  private Effect effect;

  public AsteroidFactory(
      Rectangle2D makeBounds, Rectangle2D viewBounds, int radius, Effect effect,
      Color fill) {
    // colors
    this.fill = fill;
    // boundaries
    this.makeBounds = makeBounds;
    this.viewBounds = viewBounds;
    // geometry
    this.radius = (byte) radius;
    // interaction
    this.effect = effect;
  }

  public Asteroid make(int dX, int dY, int speed) {
    return new AsteroidImpl(
        (int) Randomizer.random(makeBounds.getMinX(), makeBounds.getMaxX()),
        (int) Randomizer.random(makeBounds.getMinY(), makeBounds.getMaxY()),
        this.radius, dX, dY, speed, true, this.fill);
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

  /** Returns the rectangular bounds within which an Asteroid is visible
   *
   * @return view bounds of the AsteroidFactory
   */
  public Rectangle2D getViewBounds() {
    return this.viewBounds;
  }

  /** Sets the rectangular bounds within which an Asteroid is visible
   *
   * @param viewBounds new view bounds of the AsteroidFactory
   */
  public void setViewBounds(Rectangle2D viewBounds) {
    this.viewBounds = viewBounds;
  }

  /** Returns the radius of an Asteroid to be made with
   *
   * @return radius of the AsteroidFactory
   */
  public byte getRadius() {
    return this.radius;
  }

  /** Sets the radius of an Asteroid to be made with
   *
   * @param radius new radius of the AsteroidFactory
   */
  public void setRadius(int radius) {
    if (radius > Byte.MAX_VALUE)
      this.radius = Byte.MAX_VALUE;
    else if (radius < Byte.MIN_VALUE)
      this.radius = Byte.MIN_VALUE;
    else
      this.radius = (byte) radius;
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
   * @param effect new effect of the AsteroidFactory
   */
  public void setEffect(Effect effect) {
    this.effect = effect;
  }

  private static class AsteroidImpl extends VectorSprite implements Asteroid {

    // geometry
    /** Number of joints an Asteroid to be made with */
    public static byte POINTS = 8;

    public AsteroidImpl(
        int x, int y, int radius, int dX, int dY, int speed, boolean visibility,
        Color fill) {
      super(dX, dY, speed, visibility);
      // visual
      x -= radius / 2;
      y -= radius / 2;
      Polygon polygon = new Polygon();
      for (byte b = 0; b < POINTS; b++) {
        int random = Randomizer.random(radius / 2, (int) (radius * (5.0 / 3)));
        polygon.addPoint(
            (int) (random * +Math.cos((2 * Math.PI * b) / POINTS)),
            (int) (random * -Math.sin((2 * Math.PI * b) / POINTS))
        );
      }
      polygon.translate(x, y);
      this.path2d = new Path2D.Double(polygon);
      this.outline = OUTLINE;
      this.fill = fill;
    }
  }
}
