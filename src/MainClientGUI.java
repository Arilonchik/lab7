import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainClientGUI extends JFrame {
    private JButton exit = new JButton("EXIT");
    private JPanel mainPanel = new JPanel();
    private JPanel commandsP = new JPanel();
    private JPanel showP = new JPanel(); // вставить таблицу
    private JPanel userP = new JPanel();
    private JButton logout = new JButton("Log out");
    private JLabel us = new JLabel();
    private JLabel help = new JLabel();

    //Конструктор графического окна запускается все из UDPCLIENT
    public MainClientGUI(String username) {
        //Первоначальные настройки
        this.getContentPane().add(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Color wind = new Color(140,165,187);
        Color w = new Color(0,5,187);
        Color w2 = new Color(140,165,0);
        mainPanel.setBackground(wind);
        commandsP.setBackground(w);
        showP.setBackground(w2);
        userP.setBackground(wind);
        setTitle("Не смотри сюда");
        //разбиение Главной области на 3 разных по шаблону (смотри дискорд), расскраска такая пока что чтобы понимать области
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(commandsP,BorderLayout.WEST);
        mainPanel.add(showP,BorderLayout.CENTER);
        mainPanel.add(userP,BorderLayout.EAST);


        //Работа с правой панелью
        userP.setLayout(new GridLayout(4,1));
        //Вывод активного пользователя
        ImageIcon log = new ImageIcon("images/user.png");
        us.setIcon(log);
        us.setText("U are log in as: " + username);
        Font f = new Font("Arial Black", Font.BOLD, 14);
        us.setFont(f);
        us.setBorder(BorderFactory.createTitledBorder("Active user"));
        userP.add(us);
        //Вывод help
        help.setText("<html><p align=\"center\">Remove - delete chosen shelter<br>" +
                "Add - insert new shelter<br>" +
                "Remove_last - delete last shelter<br>" +
                "Remove_first - delete first shelter<br>" +
                "show - refresh collection<br>" +
                "AddIfMax - inset shelter, when it has maximum position<br>" +
                "Info - shows collection's information</html>");
        help.setBorder(BorderFactory.createTitledBorder("Command description"));
        help.setFont(f);
        userP.add(help);


        //Работа с средней панелью
        showP.setLayout(new GridLayout(2,1));
        
        CopyOnWriteArrayList<Shelter> collection = new CopyOnWriteArrayList<>();
        Shelter sdf = new Shelter();
        sdf.setCreator("alsdjfl");
        collection.add(sdf);
        collection.add(new Shelter());
        collection.add(new Shelter());
        collection.add(new Shelter());
        collection.add(new Shelter());

        JTable table = new JTable(new MyTableModel(collection));
        JScrollPane jscrlp = new JScrollPane(table);
        JButton btnPress = new JButton("Click!");
        btnPress.addActionListener(ae -> {
            collection.add(new Shelter());
            collection.add(new Shelter());

        });
        showP.add(jscrlp);
        showP.add(btnPress);
    }
}

class MyTableModel extends AbstractTableModel {

    CopyOnWriteArrayList<Shelter> shelter;

    MyTableModel(CopyOnWriteArrayList<Shelter> shelter) {
        super();
        this.shelter = shelter;
    }

    @Override
    public int getRowCount() {
        return shelter.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int r, int c) {
        switch (c) {
            case 0:
                return shelter.get(r).getName();
            case 1:
                return shelter.get(r).getPos();
            case 2:
                return shelter.get(r).getDate();
            case 3:
                return shelter.get(r).getCreator();
            default:
                return "";
        }
    }

    @Override
    public String getColumnName(int c) {
        String result = "";
        switch (c) {
            case 0:
                result = "Name";
                break;
            case 1:
                result = "Position";
                break;
            case 2:
                result = "ZoneData";
                break;
            case 3:
                result = "Creator";
                break;
        }
        return result;
    }
}