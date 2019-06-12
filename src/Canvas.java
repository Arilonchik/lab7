import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Canvas extends JComponent {
    public int mx = -1000, my = -1000;

    final Random ran = new Random();
    private CopyOnWriteArrayList<Shelter> sh;

    private HashMap<String,Color> logs = new HashMap<>();
    public Canvas(CopyOnWriteArrayList<Shelter> sh){
        this.sh = sh;

        this.addMouseMotionListener(new MouseMotionListener(){
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                hint(e);
            }
        });
    }
    public void hint(MouseEvent e){
        mx = e.getX();
        my = e.getY();
        System.out.println(mx+ " " + my);
        super.repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;

        String user;
        int high;
        int pos;
        for (Shelter s: sh){
            user = s.getCreator();

            if(!logs.containsKey(user)){
                logs.put(user,new Color(ran.nextInt(255)+1,ran.nextInt(255)+1,ran.nextInt(255)+1));

            }

            g2d.setPaint(logs.get(s.getCreator()));
            pos = (int)s.getPos();
            high = s.getName().length();
            g2d.fillRect(pos,312-high*10,10,high*10);



        }
        g2d.setPaint(Color.RED);
        g2d.drawLine(1,312,1000,312);
        g2d.drawLine(1000,312,990,305);
        g2d.drawLine(1000,312,990,318);
        int x1;
        int x2;
        int y1;
        int y2;

        for(Shelter s: sh){
            x1 = (int)s.getPos();
            x2 = x1+10;
            y1 = 312;
            y2 = 312-(s.getName().length()*10);

            if(mx>x1 && mx<x2 && my<y1 && my>y2){
                System.out.println("kekekke");
                g2d.setPaint(Color.black);
                g2d.drawString("Shelter: " + s.getName(), mx+10, my-20);
                g2d.drawString("Position " + s.getPos(), mx+10, my);
                g2d.drawString("Creator: " + s.getCreator(), mx+10, my+20);
            }
        }


    }

    public void getColl(CopyOnWriteArrayList<Shelter> c){
        this.sh = c;
        System.out.println(sh);
        super.repaint();
    }


}
