/*import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Anime extends JComponent implements ActionListener {

    Timer timer;
    HashMap<Shelter,Integer> y = new HashMap<Shelter,Integer>();

    //private Shelter s;
    private Color c;
    int height;
    Image pic;
    int pos;
    private CopyOnWriteArrayList<Shelter> sh;

    public Anime (CopyOnWriteArrayList<Shelter> sh, Color c) {
        this.sh = sh;
        this.c = c;
        pic = new ImageIcon("images/pismak.png").getImage();
        timer = new Timer(20, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {

        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setPaint(logs.get(s.getCreator()));
        g2d.fillRect(pos, 312-height, 10, y);
        if (y == height) {
            g2d.setPaint(new Color(2, 238, 238));
            g2d.fillRect(pos - 12, 252 - 60 - height, 35, 60);
        }
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(Shelter s:sh)
        y ++;
        if (y == height) {
            timer.stop();
        }
    }

}
*/