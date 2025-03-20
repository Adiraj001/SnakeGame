import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    private final int BOARD_WIDTH = 300;
    private final int BOARD_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int RAND_POS = 22;
    private final int ALL_DOTS = (BOARD_WIDTH * BOARD_HEIGHT) / (DOT_SIZE * DOT_SIZE);
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;
    private int food_x;
    private int food_y;
    private Image food;
    private Image Dot;
    private Image Head;
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    private Timer gameTimer;
    private JButton restartButton;
    private int dots;


    public Board() {
        addKeyListener(new TAdapter());
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(new Color(0, 0, 200));
        setFocusable(true);

        loadImages();
        initGame();
        initRestartButton();
    }

    public void loadImages() {
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Img/Food.png"));
        food = i1.getImage().getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);
        ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("Img/Dot.png"));
        Dot = i2.getImage().getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);
        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("Img/Head.png"));
        Head = i3.getImage().getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_SMOOTH);
    }

    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            y[i] = 50;
        }
        locateFood();
        gameTimer = new Timer(140, this);
        gameTimer.start();
    }

    public void initRestartButton() {
        restartButton = new JButton("Restart");
        restartButton.setBounds((BOARD_WIDTH - 100) / 2, BOARD_HEIGHT / 2 + 20, 100, 30);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        restartButton.setVisible(false);
        add(restartButton);
    }

    public void restartGame() {
        inGame = true;
        left = false;
        right = true;
        up = false;
        down = false;
        dots = 3;
        for (int i = 0; i < dots; i++) {
            y[i] = 50;
        }
        locateFood();
        gameTimer.restart();
        restartButton.setVisible(false);
        repaint();
    }

    public void locateFood() {
        int r = (int) (Math.random() * RAND_POS);
        food_x = (r * DOT_SIZE);

        r = (int) (Math.random() * RAND_POS);
        food_y = (r * DOT_SIZE);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (inGame) {
            g.drawImage(food, food_x, food_y, this);
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(Head, x[i], y[i], this);
                } else {
                    g.drawImage(Dot, x[i], y[i], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        String msg = "Game Over";
        Font font = new Font("SAN_SERIF", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(msg, (BOARD_WIDTH - metrics.stringWidth(msg)) / 2, BOARD_HEIGHT / 2);
        restartButton.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        Board board = new Board();
        frame.add(board);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[(i - 1)];
            y[i] = y[(i - 1)];
        }
        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
    }

    public void checkFood() {
        if ((x[0] == food_x) && (y[0] == food_y)) {
            dots++;
            locateFood();
        }
    }

    public void collision() {
        for (int i = dots; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }
        if (y[0] >= BOARD_HEIGHT) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
        if (x[0] >= BOARD_WIDTH) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (!inGame) {
            gameTimer.stop();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (inGame) {
            checkFood();
            move();
            collision();
        }
        repaint();
    }

    public class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && !right) {
                left = true;
                up = false;
                down = false;
            }
            if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && !left) {
                right = true;
                up = false;
                down = false;
            }
            if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && !down) {
                up = true;
                right = false;
                left = false;
            }
            if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && !up) {
                down = true;
                right = false;
                left = false;
            }
        }
    }
}
