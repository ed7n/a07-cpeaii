package eden.cpeaii;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import javax.swing.JComponent;
import javax.swing.Timer;

import eden.common.GDMAudioEngine;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameComponent extends JComponent {

  /** Updates per second */
  public static final byte RATE = 60;

  /** Text font */
  public static final Font FONT = new Font(Font.MONOSPACED, Font.BOLD, 16);

  /** Theme and field parameters */
  public static final Color ASTEROID_COLOR_DEFAULT = new Color(63, 63, 63);
  public static final Color[][] ASTEROID_COLORS = new Color[][]{
    new Color[]{new Color(95, 63, 47), new Color(191, 63, 0)},
    new Color[]{new Color(47, 63, 95), new Color(0, 63, 191)},
    new Color[]{new Color(63, 95, 47), new Color(63, 191, 0)},
    new Color[]{new Color(63, 47, 95), new Color(63, 0, 191)},
    new Color[]{new Color(95, 47, 63), new Color(191, 0, 63)},
    new Color[]{new Color(47, 95, 63), new Color(0, 191, 63)},
    new Color[]{VicViper.FILL, VectorSprite.TRANSPARENT}};
  public static final int[] ASTEROID_INTERVALS = new int[]{15, 10, 5};
  public static final int[] ASTEROID_SPEEDS_MAX = new int[]{7, 6, 10};
  public static final int[] ASTEROID_SPEEDS_MIN = new int[]{3, 2, 5};
  public static final Color STARDUST_COLOR_DEFAULT = new Color(255, 255, 255);
  public static final Color[] STARDUST_COLORS = new Color[]{
    new Color(255, 127, 0),
    new Color(0, 127, 255),
    new Color(127, 255, 0),
    new Color(127, 0, 255),
    new Color(255, 0, 127),
    new Color(0, 255, 127),
    STARDUST_COLOR_DEFAULT
  };
  public static final int[] STARDUST_LENGTHS = new int[]{2, 4, 0};

  /** Audio subsystem */
  public static int[] sounds;
  public static final GDMAudioEngine audio = new GDMAudioEngine(3);

  {
    try {
      URL fire = getClass().getResource("/FIRE.WAV");
      sounds = new int[]{audio.load(fire), audio.load(fire)};
      audio.load("BGM.WAV", "music");
    } catch (IOException | IllegalStateException | LineUnavailableException
        | UnsupportedAudioFileException e) {
    }
  }

  /** Main timer */
  private final Timer timer;

  // EventListeners
  /** KeyListener responding to KeyEvents to control Ship */
  private final KeyListener keyListenerShip;

  /** FocusListener responding to window focus changes */
  private final FocusListener focusListener;

  /** A slightly enlarged Rectangle2D of the bounds defining the GameComponent.
   * This determines the region in which a Sprite is visible. If done correctly,
   * it should allow off-screen drawing of a Sprite whose viewBounds--not
   * makeBounds--is assigned to this.
   */
  private Rectangle2D extendedBounds;

  /** Used for time-based behaviors */
  private int counter = 0;

  // Sprites
  private Ship ship;
  private Set<Asteroid> asteroids;
  private Set<LineParticle> lineParticles;

  // SpriteFactories
  private AsteroidFactory asteroidFactory;
  private LineParticleFactory lineParticleFactory;

  private StarDustTile[] starDustTiles;

  // interrupt buffers
  /** Input buffer to be used by the KeyListener */
  private Set<Integer> inputBuffer;

  /** Game state */
  private GameMode mode = GameMode.PLAY;

  /** Game score */
  private int score = 0;

  /** Game field */
  private byte field = 0;

  /** Game theme */
  private byte theme = 0;

  public GameComponent() {
    this.timer = new Timer(1000 / RATE, (ActionEvent actionEvent) -> {
      if (this.mode != GameMode.HOLD) {
        if (this.counter < 18600) {
          if (this.counter == 9900 || this.counter == 16500)
            fieldChange();
          // Asteroid generation
          if ((counter % ASTEROID_INTERVALS[this.field]) == 0) {
            Asteroid newAsteroids = this.asteroidFactory.make(
                Randomizer.random(-7, -3) * 10,
                Randomizer.random(-3, 3) * 10,
                Randomizer.random(
                    ASTEROID_SPEEDS_MIN[this.field],
                    ASTEROID_SPEEDS_MAX[this.field],
                    false));
            newAsteroids.setViewBounds(getBounds());
            this.asteroids.add(newAsteroids);
          }
        }
        update();
        repaint();
      }
      this.counter++;
    });
    // event listeners
    this.keyListenerShip = new KeyListenerShip();
    this.focusListener = new FocusListenerImpl();
    addKeyListener(keyListenerShip);
    addFocusListener(focusListener);
    setDoubleBuffered(true);
  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    paintComponent(g2);
  }

  private void paintComponent(Graphics2D g) {
    g.setFont(FONT);
    // backgrounds
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, getWidth(), getHeight());
    Arrays.stream(this.starDustTiles).forEach(s -> s.draw(g));
    // sprites
    this.asteroids.forEach(a -> a.draw(g));
    this.ship.draw(g);
    this.ship.getBlasters().forEach(b -> b.draw(g));
    this.lineParticles.forEach(l -> l.draw(g));
    g.setColor(Color.WHITE);
    if (this.mode == GameMode.GMOV)
      g.drawString("GAME OVER", 16, 16);
    g.drawString("SC: " + this.score, getWidth() / 2, 16);
    g.drawString(
        "Development C, 11/11/2020.", getWidth() - 272, getHeight() - 16);
  }

  public void start() throws InterruptedException {
    this.extendedBounds = new Rectangle2D.Double(
        -16.0, -16.0, getWidth() + 32.0, getHeight() + 32.0);
    // protagonists
    this.ship = new VicViper(16, getHeight() / 2, 2, true);
    this.ship.setViewBounds(this.extendedBounds);
    this.ship.setMovementBounds(getBounds());
    // anatagonists
    this.asteroids = new HashSet<>();
    this.lineParticles = new HashSet<>();
    // factories
    this.asteroidFactory = new AsteroidFactory(
        new Rectangle2D.Double(getWidth() + 32, 0, 0, getHeight()),
        this.extendedBounds, 24, Effect.DAMAGE, ASTEROID_COLOR_DEFAULT);
    this.lineParticleFactory = new LineParticleFactory(0, Effect.DAMAGE);
    // sprite tiles
    this.starDustTiles = new StarDustTile[]{
      new StarDustTile(
      getBounds(), this.extendedBounds, 2, 16, -25, 0, 3, STARDUST_COLOR_DEFAULT),
      new StarDustTile(
      getBounds(), this.extendedBounds, 1, 24, -25, 0, 2, STARDUST_COLOR_DEFAULT),
      new StarDustTile(
      getBounds(), this.extendedBounds, 1, 32, -25, 0, 1, STARDUST_COLOR_DEFAULT)
    };
    // event buffers
    this.inputBuffer = new HashSet<>(10);
    requestFocusInWindow();
    this.timer.start();
    while (true) {
      this.counter = 0;
      try {
        audio.playAndAwait("music");
      } catch (IllegalArgumentException e) {
      }
      this.theme = (byte) (this.theme == ASTEROID_COLORS.length - 1 ? 0
          : this.theme + 1);
      fieldChange();
    }
  }

  private void fieldChange() {
    this.field = (byte) (this.field == ASTEROID_INTERVALS.length - 1 ? 0
        : this.field + 1);
    this.starDustTiles[0].setLength(STARDUST_LENGTHS[this.field]);
    if (this.field > 0) {
      if (this.field == 1)
        this.starDustTiles[0].fill = STARDUST_COLORS[this.theme];
      else if (this.field == 2)
        this.starDustTiles[0].fill = VectorSprite.TRANSPARENT;
      this.asteroidFactory.fill = ASTEROID_COLORS[this.theme][this.field - 1];
      this.starDustTiles[1].fill = VectorSprite.TRANSPARENT;
      this.starDustTiles[2].fill = VectorSprite.TRANSPARENT;
    } else {
      this.asteroidFactory.fill = ASTEROID_COLOR_DEFAULT;
      this.starDustTiles[0].fill = STARDUST_COLOR_DEFAULT;
      this.starDustTiles[1].fill = Color.WHITE;
      this.starDustTiles[2].fill = Color.WHITE;
    }
  }

  // FIXME: refactor for unification
  private void update() {
    if (mode != GameMode.HOLD) {
      Arrays.stream(starDustTiles).forEach(s -> s.update());
      updateAsteroids();
      if (mode != GameMode.GMOV)
        updateShip();
      updateBlasters();
      updateLineParticles();
      if (mode != GameMode.GMOV)
        if (checkCollisions()) {
          mode = GameMode.GMOV;
          lineParticles.addAll(lineParticleFactory.disassemble(ship));
          ship.setVisibility(false);
          updateShip();
          removeKeyListener(keyListenerShip);
          inputBuffer.clear();
        }
    }
  }

  private void updateAsteroids() {
    asteroids.removeIf(a -> !a.isInView());
    asteroids.forEach(Asteroid::update);
  }

  private void updateShip() {
    ship.move();
    ship.updateBlasters();
  }

  private void updateBlasters() {
    ship.getBlasters().removeIf(b -> !b.isInView());
    ship.getBlasters().forEach(Blaster::update);
  }

  private void updateLineParticles() {
    lineParticles.removeIf(l -> !l.isInView());
    lineParticles.forEach(LineParticle::move);
  }

  private boolean checkCollisions() {
    Iterator<Asteroid> iA = asteroids.iterator();
    while (iA.hasNext()) {
      Asteroid a = iA.next();
      Iterator<Blaster> iB = ship.getBlasters().iterator();
      while (iB.hasNext())
        if (a.intersects(iB.next())) {
          lineParticles.addAll(lineParticleFactory.disassemble(a));
          iA.remove();
          iB.remove();
          a = null;
          this.score++;
          break;
        }
      if (a != null)
        if (a.intersects(ship))
          return true;
    }
    return false;

  }

  public class KeyListenerShip extends KeyAdapter {

    // ack: https://stackoverflow.com/a/22581039
    @Override
    public void keyPressed(KeyEvent keyEvent) {
      inputBuffer.add(keyEvent.getKeyCode());
      keyCheck();
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
      inputBuffer.remove(keyEvent.getKeyCode());
      keyCheck();
    }

    public void keyCheck() {
      if (inputBuffer.contains(KeyEvent.VK_UP) || inputBuffer.contains(
          KeyEvent.VK_KP_UP))
        if (inputBuffer.contains(KeyEvent.VK_LEFT) || inputBuffer.contains(
            KeyEvent.VK_KP_LEFT))
          ship.setDirection(Sprite.Direction.UL);
        else if (inputBuffer.contains(KeyEvent.VK_RIGHT) || inputBuffer.
            contains(KeyEvent.VK_KP_RIGHT))
          ship.setDirection(Sprite.Direction.UR);
        else
          ship.setDirection(Sprite.Direction.UU);
      else if (inputBuffer.contains(KeyEvent.VK_DOWN) || inputBuffer.contains(
          KeyEvent.VK_KP_DOWN))
        if (inputBuffer.contains(KeyEvent.VK_LEFT) || inputBuffer.contains(
            KeyEvent.VK_KP_LEFT))
          ship.setDirection(Sprite.Direction.DL);
        else if (inputBuffer.contains(KeyEvent.VK_RIGHT) || inputBuffer.
            contains(KeyEvent.VK_KP_RIGHT))
          ship.setDirection(Sprite.Direction.DR);
        else
          ship.setDirection(Sprite.Direction.DD);
      else if (inputBuffer.contains(KeyEvent.VK_LEFT) || inputBuffer.contains(
          KeyEvent.VK_KP_LEFT))
        ship.setDirection(Sprite.Direction.LL);
      else if (inputBuffer.contains(KeyEvent.VK_RIGHT) || inputBuffer.contains(
          KeyEvent.VK_KP_RIGHT))
        ship.setDirection(Sprite.Direction.RR);
      else
        ship.setDirection(Sprite.Direction.NO);
      if (inputBuffer.contains(KeyEvent.VK_X))
        ship.getBlasterFactory().setPower(true);
      else
        ship.getBlasterFactory().setPower(false);
    }
  }

  public class FocusListenerImpl extends FocusAdapter {

    @Override
    public void focusLost(FocusEvent focusEvent) {
      if (mode != GameMode.GMOV)
        mode = GameMode.HOLD;
      removeKeyListener(keyListenerShip);
      inputBuffer.clear();
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
      if (mode != GameMode.GMOV)
        mode = GameMode.PLAY;
      addKeyListener(keyListenerShip);
    }
  }
}
