package eden.cpeaii;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

  private final static int WIDTH = 640;
  public final static int HEIGHT = 480;

  private final GameComponent component;

  public GameFrame() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setSize(WIDTH, HEIGHT);
    setTitle("C Pea II");
    this.component = new GameComponent();
    add(this.component);
    setVisible(true);
  }

  public static void main(String[] args) throws InterruptedException {
    System.setProperty("sun.java2d.opengl", "True");
    new GameFrame().component.start();
  }
}
