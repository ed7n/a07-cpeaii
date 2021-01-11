package eden.cpeaii;

public enum Effect {

  NONE(0, 0, 0),
  HIT(-16, 0, 50),
  HIT_MORE(-32, 0, 200),
  DAMAGE(-16, 0, 0),
  DAMAGE_MORE(-32, 0, 0),
  RECOVER(48, 0, -40),
  RECOVER_MORE(96, 0, -150),
  SPEED_UP(4, 0.25, 100),
  SPEED_DOWN(4, -0.5, 100),
  SCORE_UP(0, 0, 1000);

  public final byte operandHealth;
  public final double operandSpeed;
  public final short operandScore;

  Effect(int operandHealth, double operandSpeed, int operandScore) {
    this.operandHealth = (byte) operandHealth;
    this.operandSpeed = operandSpeed;
    this.operandScore = (short) operandScore;
  }
};
