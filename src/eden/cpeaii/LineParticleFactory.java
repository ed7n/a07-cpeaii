package eden.cpeaii;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class LineParticleFactory {

  // colors
  /** Outline color of a LineParticle to be drawn with */
  public final static Color OUTLINE = new Color(255, 95, 0);

  /** Fill color of a LineParticle to be filled with */
  public final static Color FILL = VectorSprite.TRANSPARENT;

  // attributes
  /** "the maximum number of recursive subdivisions allowed for any curved
   * segment"
   */
  private final int resolution;

  // interaction
  /** Effect upon intersection between a LineParticle and another Sprite */
  private Effect effect;

  public LineParticleFactory(int resolution, Effect effect) {
    // attributes
    this.resolution = resolution;
    // interaction
    this.effect = effect;
  }

  /** Disassembles a Sprite into LineParticles. This method only reads the
   * Path2D of the Sprite and hence does not introduce any side effect. In
   * common applications, the Sprite has to be manually removed after this
   * method is invoked.
   *
   * @param sprite Sprite to disassemble
   */
  public Set<LineParticle> disassemble(Sprite sprite) {
    Set<LineParticle> out = new HashSet<>();
    Rectangle2D temp = sprite.getPath2d().getBounds2D();
    temp.setRect(
        temp.getMinX() - (temp.getWidth() / 2),
        temp.getMinY() - (temp.getHeight() / 2),
        temp.getWidth() * 2,
        temp.getHeight() * 2
    );
    // iterates over the points of the Path2D
    PathIterator i = new FlatteningPathIterator(
        sprite.getPath2d().getPathIterator(null), 0.0, resolution);
    // to be used by currentSegment() to store the current Path segment
    double[] segment = new double[6];
    /*
     * to be used to construct LineParticles [0] and [2] will be used in the
     * following for loop [1] will be used to store the first point of the
     * Path2D
     */
    int[][] coordinates = new int[3][2];
    i.currentSegment(segment);
    coordinates[0][0] = (int) segment[0];
    coordinates[0][1] = (int) segment[1];
    coordinates[1] = Arrays.copyOf(coordinates[0], 2);
    i.next();
    for (byte b = 1; !i.isDone(); b *= -1) {
      // can be done nicer if one can cast between array types and lengths
      i.currentSegment(segment);
      coordinates[b + 1][0] = (int) segment[0];
      coordinates[b + 1][1] = (int) segment[1];
      /*
       * I find FlatteningPathIterator's behaviour a bit odd. isDone() returns
       * true after one invokes next() after the last segment. I guess it's not
       * designed for my use.
       */
      if (!Arrays.equals(coordinates[b + 1], coordinates[-b + 1]))
        out.add(new LineParticleImpl(
            coordinates[+b + 1][0],
            coordinates[+b + 1][1],
            coordinates[-b + 1][0],
            coordinates[-b + 1][1],
            Randomizer.random(-2, 2, false) * 100,
            Randomizer.random(-2, 2, false) * 100,
            true
        ));
      else
        out.add(new LineParticleImpl(
            coordinates[b + 1][0],
            coordinates[b + 1][1],
            coordinates[1][0],
            coordinates[1][1],
            Randomizer.random(-2, 2, false) * 100,
            Randomizer.random(-2, 2, false) * 100,
            true
        ));
      i.next();
    }
    out.forEach(l -> l.setViewBounds(temp));
    return out;
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

  private static class LineParticleImpl
      extends VectorSprite implements LineParticle {

    // velocity
    /** Speed at which a LineParticle moves */
    public static byte speed = 2;

    public LineParticleImpl(
        int x0, int y0, int x1, int y1, int dX, int dY, boolean visibility) {
      super(dX, dY, speed, visibility);
      this.path2d = new Path2D.Double();
      this.path2d.moveTo(x0, y0);
      this.path2d.lineTo(x1, y1);
      this.outline = OUTLINE;
      this.fill = FILL;
    }
  }
}
