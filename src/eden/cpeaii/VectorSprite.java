package eden.cpeaii;

import java.awt.*;
import java.awt.geom.*;

import eden.common.io.Modal;

/** A VectorSprite is a Sprite graphically represented as a vector as opposed to
 * raster graphics. This abstract superclass defines properties common to all
 * Sprites, a Sprite subclass may extend this class and then define specific
 * attributes and behaviors.
 */
public abstract class VectorSprite implements Sprite {

  private static final Modal modal = new Modal("VectorSprite");

  // geometry
  /** Path2D representing an empty Shape */
  public static final Path2D SHAPE_NULL = new Path2D.Float();

  /** AffineTransform representing the identity transformation */
  public static final AffineTransform IDENTITY_TRANSFORMATION
      = new AffineTransform();

  // color
  /** Color representing full transparency */
  public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

  // visual
  /** Geometric representation of the VectorSprite */
  Path2D path2d;

  /** Outline color of the VectorSprite to be drawn with */
  Color outline;

  /** Fill color of the VectorSprite to be filled with */
  Color fill;

  /** Graphical visibility of the VectorSprite */
  boolean visibility;

  // geometry
  /** AffineTransform to be applied to path2D upon update */
  AffineTransform affineTransform;

  // velocity
  /** X-direction at which the VectorSprite moves. Upon move, the VectorSprite
   * translates by ((dX / 100.0) * speed) in the x-direction.<br>
   * Range: [-100, +100]
   */
  byte dX;

  /** Y-direction at which the VectorSprite moves. Upon move, the VectorSprite
   * translates by ((dY / 100.0) * speed) in the Y-direction.<br>
   * Range: [-100, +100]
   */
  byte dY;

  /** Direction multiplier to simulate speed */
  byte speed;

  // boundaries
  /** Rectangular bounds within which the VectorSprite is visible */
  Rectangle2D viewBounds;

  /** Rectangular bounds within which the VectorSprite is movable */
  Rectangle2D movementBounds;

  public VectorSprite(int speed, boolean visibility) {
    this(0, 0, speed, visibility);
  }

  public VectorSprite(int dX, int dY, int speed, boolean visibility) {
    // visual
    this.path2d = SHAPE_NULL;
    this.outline = TRANSPARENT;
    this.fill = TRANSPARENT;
    this.visibility = visibility;
    // geometry
    this.affineTransform = new AffineTransform();
    // velocity
    this.dX = (byte) dX;
    this.dY = (byte) dY;
    this.speed = (byte) speed;
  }

  /** Draws the VectorSprite
   *
   * @param g Graphics2D context to be passed
   */
  @Override
  public void draw(Graphics2D g) {
    this.path2d.transform(this.affineTransform);
    if (isVisible()) {
      g.setColor(this.outline);
      g.draw(this.path2d);
      g.setColor(this.fill);
      g.fill(this.path2d);
    }
    this.affineTransform.setTransform(IDENTITY_TRANSFORMATION);
  }

  /** Translates the VectorSprite as defined by its velocity instance variables
   */
  @Override
  public void move() {
    double tX = (this.dX / 100.0) * this.speed;
    double tY = (this.dY / 100.0) * this.speed;
    // check VectorSprite against its movementBounds
    if (this.movementBounds != null) {
      Rectangle2D path2dBounds = this.path2d.getBounds2D();
      // x-axis
      if ((((int) this.movementBounds.getMinX())
          - ((int) path2dBounds.getMinX())) > tX)
        tX = Math.max(((int) path2dBounds.getMinX()
            - (int) this.movementBounds.getMinX()), 0);
      else if (((int) this.movementBounds.getMaxX()
          - (int) path2dBounds.getMaxX()) < tX)
        tX = Math.max(((int) this.movementBounds.getMaxX()
            - (int) path2dBounds.getMaxX()), 0);
      // y-axis
      if ((((int) this.movementBounds.getMinY())
          - ((int) path2dBounds.getMinY())) > tY)
        tY = Math.max(((int) path2dBounds.getMinY()
            - (int) this.movementBounds.getMinY()), 0);
      else if (((int) this.movementBounds.getMaxY()
          - (int) path2dBounds.getMaxY()) < tY)
        tY = Math.max(((int) this.movementBounds.getMaxY()
            - (int) path2dBounds.getMaxY()), 0);
    }
    this.affineTransform.translate(tX, tY);
  }

  /** Updates the VectorSprite. A Sprite subclass may invoke this method as is
   * and then/or define specific behaviors.
   */
  @Override
  public void update() {
    move();
    //scale();
    //rotate();
    //applyFriction();
  }

  /** Checks whether the VectorSprite intersects another Sprite
   *
   * @param sprite Sprite to check against
   * @return true if the condition is met; false otherwise
   */
  @Override
  public boolean intersects(Sprite sprite) {
    // less expensive
    if (this.path2d.intersects(sprite.getPath2d().getBounds2D())) {
      // more expensive
      Area areaThis = new Area(this.path2d);
      Area areaSprite = new Area(sprite.getPath2d());
      areaThis.intersect(areaSprite);
      return !areaThis.isEmpty();
    }
    return false;
  }

  /** Checks whether or not the VectorSprite is within its view bounds
   *
   * @return true if the condition is met; false otherwise
   */
  @Override
  public boolean isInView() {
    return this.viewBounds == null ? true : this.path2d.intersects(
        this.viewBounds);
  }

  /** Returns the rectangular bounds within which the VectorSprite is visible
   *
   * @return view bounds of the VectorSprite
   */
  @Override
  public Rectangle2D getViewBounds() {
    return this.viewBounds;
  }

  /** Sets the rectangular bounds within which the VectorSprite is visible
   *
   * @param viewBounds new view bounds of the VectorSprite
   */
  @Override
  public void setViewBounds(Rectangle2D viewBounds) {
    this.viewBounds = viewBounds;
  }

  /** Returns the rectangular bounds within which the VectorSprite is movable
   *
   * @return move bounds of the VectorSprite
   */
  @Override
  public Rectangle2D getMovementBounds() {
    return this.movementBounds;
  }

  /** Sets the rectangular bounds within which the VectorSprite is movable
   *
   */
  @Override
  public void setMovementBounds(Rectangle2D movementBounds) {
    this.movementBounds = movementBounds;
  }

  /** Returns the x- and y-direction at which the VectorSprite moves
   *
   * @return x- and y-direction of the VectorSprite
   */
  @Override
  public byte[] getDirection() {
    return new byte[]{this.dX, this.dY};
  }

  /** Sets the x- and y-direction at which the VectorSprite moves
   *
   * @param d new x- and y-direction of the VectorSprite
   */
  @Override
  public void setDirection(byte[] d) {
    if (d.length > 1)
      setDirection((int) d[0], (int) d[1]);
    else
      modal.println(" BAD d[]", Modal.ERROR);
  }

  /** Sets the x- and y-direction at which the VectorSprite moves
   *
   * @param dX new x-direction of the VectorSprite
   * @param dY new y-direction of the VectorSprite
   */
  @Override
  public void setDirection(int dX, int dY) {
    if (dX <= 100 && dX >= -100)
      this.dX = (byte) dX;
    else
      modal.println(" BAD dX", Modal.ERROR);
    if (dY <= 100 && dY >= -100)
      this.dY = (byte) dY;
    else
      modal.println(" BAD dY", Modal.ERROR);
  }

  /** Sets the x- and y-direction at which the VectorSprite moves
   *
   * @param direction Direction whose dX and dY are to be read from
   */
  @Override
  public void setDirection(Direction direction) {
    this.dX = direction.dX;
    this.dY = direction.dY;
  }

  /** Returns the geometric representation of the VectorSprite
   *
   * @return Path2D of the VectorSprite
   */
  @Override
  public Path2D getPath2d() {
    return this.path2d;
  }

  /** Sets the geometric representation of the VectorSprite
   *
   * @param path2d new Path2D of the VectorSprite
   */
  @Override
  public void setPath2d(Path2D path2d) {
    this.path2d = path2d;
  }

  /** Returns the speed at which the VectorSprite moves
   *
   * @return speed of the VectorSprite
   */
  @Override
  public byte getSpeed() {
    return this.speed;
  }

  /** Sets the speed at which the VectorSprite moves
   *
   * @param speed new speed of the VectorSprite
   */
  @Override
  public void setSpeed(int speed) {
    if (speed > Byte.MAX_VALUE)
      this.speed = Byte.MAX_VALUE;
    else if (speed < Byte.MIN_VALUE)
      this.speed = Byte.MIN_VALUE;
    else
      this.speed = (byte) speed;
  }

  /** Returns whether or not the VectorSprite is explicitly visible
   *
   * @return true if the condition is met; false otherwise
   */
  @Override
  public boolean isVisible() {
    return this.visibility;
  }

  /** Sets whether the VectorSprite is explicitly visible
   *
   * @param newVisibility new visibility state
   */
  @Override
  public void setVisibility(boolean newVisibility) {
    this.visibility = newVisibility;
  }

  /** Returns the fill color of the VectorSprite to be drawn with
   *
   * @return fill color of the VectorSprite
   */
  public Color getColorFill() {
    return this.fill;
  }

  /** Sets the fill color of the VectorSprite to be drawn with
   *
   * @param color new fill color of the VectorSprite
   */
  public void setColorFill(Color color) {
    this.fill = color;
  }

  /** Returns the outline color of the VectorSprite to be drawn with
   *
   * @return outline color of the VectorSprite
   */
  public Color getColorOutline() {
    return this.outline;
  }

  /** Sets the outline color of the VectorSprite to be drawn with
   *
   * @param color new outline color of the VectorSprite
   */
  public void setColorOutline(Color color) {
    this.outline = color;
  }
}
