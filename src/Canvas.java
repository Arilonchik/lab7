import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

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
            g2d.fillRect(pos,312-high*10,15,high*10);
        }
        g2d.setPaint(Color.RED);
        g2d.drawLine(1,312,1000,312);
        g2d.drawLine(1000,312,990,305);
        g2d.drawLine(1000,312,990,318);




        /* 	Устанавливает цвет рисования в зелёный*/
        g2d.setPaint(Color.GREEN);

        /* 	Рисует текущим цветом прямоугольник	*/
        g2d.drawRect(100, 100, 80, 20);

        g2d.setPaint(Color.RED);
        /* 	Рисует текущим цветом в координатах (150,150) строку "привет мир"*/
        g2d.drawString("Привет мир", 150, 150);

        g2d.setColor(Color.blue);
        /*	Рисует текущим цветом овал в координатах (200,50)*/
        g2d.fillOval(200, 50, 50, 20);

        /* 	Вызывает обновление себя после завершения рисования	*/
        super.repaint();
    }

    public void getColl(CopyOnWriteArrayList<Shelter> c){
        this.sh = c;
        super.repaint();
    }
}
