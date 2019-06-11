import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainClientGUI{
    private JButton exit = new JButton("EXIT");
    private JPanel mainPanel = new JPanel();
    private JPanel commandsP = new JPanel();
    private JPanel showP = new JPanel(); // вставить таблицу
    private JPanel userP = new JPanel();
    private JButton logout = new JButton("Log out");
    private JLabel us = new JLabel();
    private JLabel help = new JLabel();
    private JFrame mainDialog;

    private ActionListener buttonListener = new FirstActionListener();
    private String login;
    private String password;
    DatagramSocket clientSocket;
    InetAddress IPAddress;

    //Конструктор графического окна запускается все из UDPCLIENT

    public MainClientGUI(DatagramSocket d, InetAddress i) {
        clientSocket = d;
        IPAddress = i;}

    public void work(String username){
        //Первоначальные настройки
        mainDialog = new JFrame();
        mainDialog.setTitle("Work bro!");
        mainDialog.getContentPane().add(mainPanel);
        mainDialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Color wind = new Color(140,165,187);
        Color w = new Color(0,5,187);
        Color w2 = new Color(140,165,0);
        mainPanel.setBackground(wind);
        commandsP.setBackground(w);
        showP.setBackground(w2);
        userP.setBackground(wind);
        mainDialog.setTitle("Не смотри сюда");
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


        //Работа с кнопаками выхода
        JPanel d = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        Box out = Box.createHorizontalBox();
        logout.setActionCommand("logout");
        exit.setActionCommand("exit");
        logout.addActionListener(buttonListener);
        exit.addActionListener(buttonListener);
        out.add(logout);
        out.add(Box.createHorizontalStrut(50));
        out.add(exit);
        d.add(out);
        mainPanel.add(d,BorderLayout.AFTER_LAST_LINE);






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
        MyTableModel mTabel = new MyTableModel(collection);
        JTable table = new JTable(mTabel);
        JScrollPane jscrlp = new JScrollPane(table);

        new Thread(() -> {
            while (true) {
                mTabel.fireTableDataChanged(); //fgj
            }
        }).start();

        showP.add(jscrlp);

        mainDialog.pack();
        mainDialog.setSize(1280,720);
        mainDialog.setVisible(true);





        //Работа с левой панелью


    }
    public class FirstActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //Код, который нужно выполнить при нажатии
            String s = e.getActionCommand();
            switch (s) {
                case"logout":
                    mainDialog.dispose();
                    AutorisationDialog gui = new AutorisationDialog(clientSocket,IPAddress);
                    gui.firstDialog();
                    return;
                case"exit":
                    System.exit(0);
                    return;

            }
        }
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