package eden.cpeaii;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import eden.common.io.Modal;

public class StarDustTile {

  private static final Modal modal = new Modal("StarDustTile");

  // colors
  /** Outline color of a StarDust to be drawn with */
  public final static Color OUTLINE = Color.WHITE;

  /** Fill color of a StarDust to be filled with */
  public Color fill;

  // boundaries
  /** Rectangular bounds in which a StarDust will be made */
  private Rectangle2D makeBounds;

  /** Rectangular bounds defining the StarDustTile. This determines the region
   * in which a StarDust is visible.
   */
  private Rectangle2D viewBounds;

  // attributes
  /** Side length of a StarDust */
  private byte length;

  /** Number of StarDusts in a StarDustTile */
  private byte density;

  // velocity
  /** X-direction at which the StarDustTile moves. Upon move, the StarDustTile
   * translates by ((dX / 100.0) * speed) in the x-direction.<br>
   * Range: [-100, +100]
   */
  private final byte dX;

  /** Y-direction at which the StarDustTile moves. Upon move, the StarDustTile
   * translates by ((dY / 100.0) * speed) in the Y-direction.<br>
   * Range: [-100, +100]
   */
  private final byte dY;

  /** Direction multiplier to simulate speed */
  private final byte speed;

  // aggregations
  /** Set of StarDusts in the StarDustTile. Size defined by density. */
  private final Set<StarDust> starDusts;

  public StarDustTile(
      Rectangle2D makeBounds, Rectangle2D viewBounds, int length, int density,
      int dX, int dY, int speed, Color fill) {
    // boundaries
    this.makeBounds = makeBounds;
    this.viewBounds = viewBounds;
    // attributes
    this.length = (byte) length;
    this.density = (byte) density;
    // velocity
    this.dX = (byte) dX;
    this.dY = (byte) dY;
    this.speed = (byte) speed;
    // aggregations
    this.starDusts = new HashSet<>();
    // visual
    this.fill = fill;
    for (byte b = 0; b < density; b++)
      this.starDusts.add(new StarDustImpl(
          (int) Randomizer.random(makeBounds.getMinX(), makeBounds.getMaxX()),
          (int) Randomizer.random(makeBounds.getMinY(), makeBounds.getMaxY()),
          length, dX, dY, speed, this.fill));
    this.starDusts.forEach(s -> s.setViewBounds(viewBounds));
  }

  /** Draws the StarDustTile
   *
   * @param g Graphics2D context to be passed
   */
  public void draw(Graphics2D g) {
    this.starDusts.forEach(s -> s.draw(g));
  }

  /** Updates the StarDustTile. This method removes and makes StarDusts as
   * necessary.
   */
  public void update() {
    this.starDusts.removeIf(s -> !s.isInView());
    this.starDusts.forEach(StarDust::update);
    Set<StarDust> newDusts = new HashSet<>();
    int tempX = 0;
    // FIXME: refactor to accept any direction
    if (this.dX > 0)
      tempX = (int) this.makeBounds.getMinX();
    else if (this.dX < 0)
      tempX = (int) this.makeBounds.getMaxX();
    else
      modal.println(" UPD UNEX", Modal.ERROR);
    for (byte b = (byte) this.starDusts.size(); b <= this.density; b++)
      newDusts.add(new StarDustImpl(
          tempX,
          (int) Randomizer.random(
              this.makeBounds.getMinY(), this.makeBounds.getMaxY()),
          this.length, this.dX, this.dY, this.speed, this.fill));
    newDusts.forEach(s -> s.setViewBounds(this.viewBounds));
    this.starDusts.addAll(newDusts);
  }

  /** Returns the rectangular bounds in which a StarDust will be made
   *
   * @return make bounds of the StarDustTile
   */
  public Rectangle2D getMakeBounds() {
    return this.makeBounds;
  }

  /** Sets the rectangular bounds in which a StarDust will be made
   *
   * @param makeBounds new make bounds of the StarDustTile
   */
  public void setMakeBounds(Rectangle2D makeBounds) {
    this.makeBounds = makeBounds;
  }

  /** Returns the rectangular bounds in which a StarDust is visible
   *
   * @return view bounds of the StarDustTile
   */
  public Rectangle2D getViewBounds() {
    return this.viewBounds;
  }

  /** Sets the rectangular bounds in which a StarDust is visible
   *
   * @param viewBounds new view bounds of the StarDustTile
   */
  public void setViewBounds(Rectangle2D viewBounds) {
    this.viewBounds = viewBounds;
  }

  /** Returns the side length of a StarDust
   *
   * @return length of a StarDust
   */
  public byte getLength() {
    return this.length;
  }

  /** Sets the side length of a StarDust
   *
   * @param length new length of a StarDust
   */
  public void setLength(int length) {
    if (length > Byte.MAX_VALUE)
      this.length = Byte.MAX_VALUE;
    else if (length < Byte.MIN_VALUE)
      this.length = Byte.MIN_VALUE;
    else
      this.length = (byte) length;
  }

  /** Returns the number of StarDusts in a StarDustTile
   *
   * @return density of the StarDustTile
   */
  public byte getDensity() {
    return this.density;
  }

  /** Sets the number of StarDusts in a StarDustTile
   *
   * @param density new density of the StarDustTile
   */
  public void setDensity(int density) {
    if (density > Byte.MAX_VALUE)
      this.density = Byte.MAX_VALUE;
    else if (density < Byte.MIN_VALUE)
      this.density = Byte.MIN_VALUE;
    else
      this.density = (byte) density;
  }

  private static class StarDustImpl extends VectorSprite implements StarDust {

    public StarDustImpl(
        int x, int y, int length, int dX, int dY, int speed, Color fill) {
      super(dX, dY, speed, true);
      // visual
      x -= length / 2;
      y -= length / 2;
      this.path2d = new Path2D.Double();
      this.path2d.moveTo(x, y);
      this.path2d.lineTo(x + length, y);
      this.path2d.lineTo(x + length, y + length);
      this.path2d.lineTo(x, y + length);
      this.path2d.lineTo(x, y);
      this.outline = fill;
      this.fill = fill;
    }
  }
}
