import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.Image;

public class SnakeGameFiles extends JFrame {
    public SnakeGameFiles() {
        super("Snake Game By Adiraj");
        add(new Board());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocation(560, 100);
        setResizable(false);
        ImageIcon notepadIcon = new ImageIcon(ClassLoader.getSystemResource("Img/Snake.png"));
        Image icon = notepadIcon.getImage();
        setIconImage(icon);
    }

    public static void main(String[] args) {
        new SnakeGameFiles();
    }
}
