import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class AnimationRec extends JComponent implements ActionListener {

    Timer timer;
    int y;

    private Shelter s;
    private Color c;
    int height;
    Image pic;
    int pos;

    public AnimationRec (Shelter s, Color c) {
        this.s = s;
        this.c = c;
        pic = new ImageIcon("images/pismak1.png").getImage();
        timer = new Timer(20, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        pos = (int)s.getPos();
        height = s.getName().length()*15;
        g.drawImage(pic, pos-12, 252-y-60, null);
        g.setColor(c);
        g.fillRect(pos,252-y,10,y);
        if (y == height) {
            g.setColor(new Color(238, 238, 238));
            g.fillRect(pos-12, 252-60-height, 35, 60);
        }
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        y ++;
        if (y == height) {
            timer.stop();
        }
    }

    public static void main(String[] args) {
        Shelter shelter = new Shelter();
        shelter.setX(250);
        shelter.setName("Big Rock");

        JFrame frame = new JFrame() {};


        AnimationRec anim = new AnimationRec(shelter, Color.GREEN);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(500, 500));
        frame.add(anim);

        frame.setVisible(true);
    }
}