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

    final Random ran = new Random();
    private CopyOnWriteArrayList<Shelter> sh;
    private HashMap<String,Color> logs = new HashMap<>();
    public Canvas(CopyOnWriteArrayList<Shelter> sh){
        this.sh = sh;
    }


    public void paintComponent(Graphics g){
        super.paintComponents(g);
        Graphics2D g2d=(Graphics2D)g;
        String user;
        int high;
        int pos;
        for (Shelter s: sh){
            user = s.getCreator();
            if(!logs.containsKey(user)){
                logs.put(user,new Color(ran.nextInt(255)+1,ran.nextInt(255)+1,ran.nextInt(255)+1));

            }
            pos = (int)s.getPos();
            high = s.getName().length();
            g2d.setPaint(logs.get(s.getCreator()));
            Rect tr = new Rect(s,logs.get(s.getCreator()));
            tr.paintComponent(g);

            //System.out.println("erfuihefhu");
            //tr.addMouseListener(new CustomListener());


        }
        g2d.setPaint(Color.RED);
        g2d.drawLine(1,312,1000,312);
        g2d.drawLine(1000,312,990,305);
        g2d.drawLine(1000,312,990,318);




        super.repaint();
    }

    public void getColl(CopyOnWriteArrayList<Shelter> c){
        this.sh = c;
        super.repaint();
    }


}
