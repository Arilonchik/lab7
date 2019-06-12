import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.sql.SQLOutput;
import java.util.HashMap;

public class Rect extends JComponent implements MouseListener, MouseMotionListener {
    private Shelter s;
    private Color c;

    public Rect(Shelter s, Color c) {
        this.s = s;
        this.c = c;
        int high = s.getName().length();
        //this.setSize(10,high*10);

    }

    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;

         int pos = (int)s.getPos();
         int high = s.getName().length();
        g2d.setPaint(c);
        g2d.fillRect(pos,312-high*10,10,high*10);

        super.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("fjseifjhsojfndsjofvndsfjklidsrjgvio");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("fjseifjhsojfndsjofvndsfjklidsrjgvio");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("fjseifjhsojfndsjofvndsfjklidsrjgvio");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("fjseifjhsojfndsjofvndsfjklidsrjgvio");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("fjseifjhsojfndsjofvndsfjklidsrjgvio");
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println("relkfjeriohfoewfouhewifhowefwefui");
    }
}
