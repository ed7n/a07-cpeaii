package eden.cpeaii;

import java.util.Set;

public interface Ship extends Sprite {

  // Blaster
  public BlasterFactory getBlasterFactory();

  public Set<Blaster> getBlasters();

  public void updateBlasters();

  // energy
  public byte getEnergy();

  public void setEnergy(int amount);
}
