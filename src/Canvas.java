import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Canvas extends JComponent implements ActionListener{
    public int mx = -1000, my = -1000;

    final Random ran = new Random();
    private CopyOnWriteArrayList<Shelter> sh;
    Image pic;
    Timer timer;
    HashMap<Shelter,Integer> y = new HashMap<Shelter,Integer>();
    HashMap<Shelter,Integer> height = new HashMap<Shelter,Integer>();

    private boolean check = false;

    private HashMap<String,Color> logs = new HashMap<>();
    public Canvas(CopyOnWriteArrayList<Shelter> sh){
        this.sh = sh;

        pic = new ImageIcon("images/pismak.png").getImage();


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
        //System.out.println(mx+ " " + my);
        check = false;
        super.repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;
        if (check == false){
        String user;
        int high;
        int pos;
        for (Shelter s: sh) {



            g2d.setPaint(logs.get(s.getCreator()));
            pos = (int) s.getPos();
            high = s.getName().length();

            g2d.fillRect(pos, 312 - high * 10, 10, high * 10);
        }

        }
            if (check == true) {

                for (Shelter s : sh) {

                    String user = s.getCreator();
                    System.out.println(user);

                    System.out.println("che za memosa");
                    int pos;
                    //System.out.println(y);
                    //System.out.println(height);

                    if(y.get(s)!=height.get(s)) {
                        y.put(s,y.get(s)+1);
                        System.out.println("lolez");
                        pos = (int) s.getPos();

                        //g2d.drawImage(pic, pos - 12, 312 - y.get(s) - 60, null);
                        g2d.setPaint(logs.get(s.getCreator()));
                        g2d.fillRect(pos, 312-height.get(s), 10, y.get(s));

                    }
                    if(y.get(s)==height.get(s)) {
                        g2d.setPaint(logs.get(s.getCreator()));
                        g2d.fillRect((int)s.getPos(), 312-height.get(s), 10, y.get(s));
                    }

                }

                repaint();

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

    @Override
    public void actionPerformed(ActionEvent e) {
        for(Shelter s:sh){
            y.put(s,y.get(s)+1);
        }
        //if (y == height) {
            //timer.stop();
        //}
    }

    public void getColl(CopyOnWriteArrayList<Shelter> c){
        this.sh = c;
        System.out.println(sh);
        check = true;
        for (Shelter s : sh){
            y.put(s,1);
            height.put(s,s.getName().length() * 10);
            String user = s.getCreator();
            if (!logs.containsKey(user)) {
                logs.put(user, new Color(ran.nextInt(255) + 1, ran.nextInt(255) + 1, ran.nextInt(255) + 1));
            }
        }
        super.repaint();
    }


}
