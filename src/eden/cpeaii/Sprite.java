package eden.cpeaii;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;

public interface Sprite {

  public enum Direction {
    NO(-000, -000),
    UU(-000, -100),
    DD(-000, +100),
    LL(-100, -000),
    RR(+100, -000),
    UL(-100, -100),
    DR(+100, +100),
    UR(+100, -100),
    DL(-100, +100);

    /** X-direction */
    public final byte dX;
    /** Y-direction */
    public final byte dY;

    Direction(int dX, int dY) {
      this.dX = (byte) dX;
      this.dY = (byte) dY;
    }
  }

  // render
  public void draw(Graphics2D g);

  public void move();

  public void update();

  // interaction
  public boolean intersects(Sprite s);

  // boundaries
  public boolean isInView();

  public Rectangle2D getViewBounds();

  public void setViewBounds(Rectangle2D r);

  public Rectangle2D getMovementBounds();

  public void setMovementBounds(Rectangle2D r);

  // velocity
  public byte[] getDirection();

  public void setDirection(byte[] d);

  public void setDirection(int dX, int dY);

  public void setDirection(Direction d);

  public byte getSpeed();

  public void setSpeed(int amount);

  // visibility
  public boolean isVisible();

  public void setVisibility(boolean visibility);

  // visual
  public Path2D getPath2d();

  public void setPath2d(Path2D p);
}
