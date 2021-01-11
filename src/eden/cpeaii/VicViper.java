package eden.cpeaii;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class VicViper extends VectorSprite implements Ship {

  // geometry
  /** Width of a Ship */
  public final static int WIDTH = 24;

  /** Height of a Ship */
  public final static int HEIGHT = 18;

  // colors
  /** Outline color of a Ship to be drawn with */
  public final static Color OUTLINE = Color.WHITE;

  /** Fill color of a Ship to be filled with */
  public final static Color FILL = new Color(255, 255, 255, 127);

  // aggregations
  /** BlasterFactory within a Ship */
  private final BlasterFactory blasterFactory;

  /** Set of Blasters made by the BlasterFactory within a Ship */
  private final Set<Blaster> blasters;

  /** Ship health, power, or such */
  private byte energy;

  public VicViper(int x, int y, int speed, boolean visibility) {
    super(speed, visibility);
    // visual
    x -= WIDTH / 2;
    y -= HEIGHT / 2;
    Polygon polygon = new Polygon(
        new int[]{
          0, (int) (WIDTH * (1.0 / 3)), WIDTH, (int) (WIDTH * (1.0 / 3)), 0,
          WIDTH / 6, 0},
        new int[]{
          0, (int) (HEIGHT * (5.0 / 16)), HEIGHT / 2, (int) (HEIGHT
          * (11.0 / 16)), HEIGHT, HEIGHT / 2, 0},
        7
    );
    polygon.translate(x, y);
    this.path2d = new Path2D.Double(polygon);
    this.outline = OUTLINE;
    this.fill = FILL;
    // aggregations
    this.blasterFactory = new BlasterFactory(
        path2d.getBounds2D(), false, 1, 15, Effect.DAMAGE);
    this.blasters = new HashSet<>();
    this.energy = Byte.MAX_VALUE;
  }

  @Override
  public void update() {
    super.update();
    updateBlasters();
  }

  @Override
  public BlasterFactory getBlasterFactory() {
    return this.blasterFactory;
  }

  @Override
  public Set<Blaster> getBlasters() {
    return this.blasters;
  }

  @Override
  public void updateBlasters() {
    this.blasters.removeIf(b -> !b.isInView());
    if (this.blasterFactory.isPowered() && this.blasterFactory.isReady()) {
      this.blasterFactory.setMakeBounds(this.path2d.getBounds2D());
      Blaster blaster = this.blasterFactory.make(100, 0);
      blaster.setViewBounds(this.viewBounds);
      this.blasters.add(blaster);
      for (int i = 0; i < GameComponent.sounds.length; i++)
        if (GameComponent.audio.play(i))
          break;
    }
    this.blasters.forEach(Blaster::update);
    this.blasterFactory.update();
  }

  @Override
  public byte getEnergy() {
    return this.energy;
  }

  @Override
  public void setEnergy(int energy) {
    if (energy > Byte.MAX_VALUE)
      this.energy = Byte.MAX_VALUE;
    else if (energy < Byte.MIN_VALUE)
      this.energy = Byte.MIN_VALUE;
    else
      this.energy = (byte) energy;
  }
}
