import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.net.DatagramPacket;
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
    private JFrame sureDialog;

    private ActionListener buttonListener = new FirstActionListener();
    private String login;
    private String password;
    DatagramSocket clientSocket;
    InetAddress IPAddress;
    private CopyOnWriteArrayList<Shelter> sh;

    //Конструктор графического окна запускается все из UDPCLIENT

    public MainClientGUI(DatagramSocket d, InetAddress i) {
        clientSocket = d;
        IPAddress = i;
    }

    public void work(String username){


        new Thread(() -> {
            try{
                Packet pac = new Packet("show",username,"lfCg1");
                DatagramPacket sendPacket = new DatagramPacket(Serializer.serialize(pac), Serializer.serialize(pac).length, IPAddress, 1703);
                clientSocket.send(sendPacket);}catch (Exception e){
                System.out.println("Loook back");
            }
            while (true) {
                sh = takeColl();
            }
        }).start();

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
        MyTableModel mTabel = new MyTableModel();
        mTabel.setShelter(sh);
        JTable table = new JTable(mTabel);
        table.setAutoCreateRowSorter(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(200);
        tcm.getColumn(1).setPreferredWidth(75);
        tcm.getColumn(2).setPreferredWidth(300);
        tcm.getColumn(3).setPreferredWidth(203);
        JScrollPane jscrlp = new JScrollPane(table);


        new Thread(() -> {
            while (true) {


                    mTabel.fireTableDataChanged();

            }
        }).start();

        showP.add(jscrlp);

        mainDialog.pack();
        mainDialog.setSize(1280,720);
        mainDialog.setVisible(true);





        //Работа с левой панелью
        commandsP.setLayout(new GridLayout(7,1));
        JLabel descrip = new JLabel("                  Commands                     ");
        descrip.setHorizontalAlignment(SwingConstants.CENTER);
        descrip.setBorder(BorderFactory.createRaisedBevelBorder());
        commandsP.add(descrip);
        Box add = Box.createVerticalBox();

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
                    createSureDialog();
                    return;
                case"yes":
                    System.exit(0);
                    return;
                case "no":
                    sureDialog.dispose();
                    return;

            }
        }
    }


    public void createSureDialog(){
        sureDialog = new JFrame();
        sureDialog.setLocationRelativeTo(null);
        sureDialog.setTitle("NO, BRO, PLZ NOOOOO");
        sureDialog.setResizable(false);
        JPanel sr = new JPanel(new GridLayout(2, 1));
        sureDialog.getContentPane().add(sr);
        JLabel really = new JLabel("Are u sure? :((");
        really.setHorizontalAlignment(SwingConstants.CENTER);
        sr.add(really);
        JButton yes = new JButton("Yes");
        JButton no = new JButton("Okay, i will stay with u bro");
        Box buttonbox = Box.createHorizontalBox();
        yes.setPreferredSize(new Dimension(100, 18));
        yes.setActionCommand("yes");
        yes.addActionListener(buttonListener);
        no.setPreferredSize(new Dimension(100, 18));
        no.setActionCommand("no");
        no.addActionListener(buttonListener);
        buttonbox.add(yes);
        buttonbox.add(Box.createHorizontalStrut(155));
        buttonbox.add(no);
        sr.add(buttonbox);

        sureDialog.pack();
        sureDialog.setSize(400, 100);
        sureDialog.setVisible(true);

    }
    private CopyOnWriteArrayList<Shelter> takeColl(){
        try {
            byte[] receiveData = new byte[4098];

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            Packet pac = Serializer.deserialize(receivePacket.getData());
            System.out.println(pac.getCollection());
            return pac.getCollection();
        }catch(IOException | ClassNotFoundException e){
            System.out.println("dkk");
            e.printStackTrace();
            return null;
        }

    }
    private void disconect(){
        try {
            Packet pac = new Packet("disconnect", login, null);
            DatagramPacket sendPacket = new DatagramPacket(Serializer.serialize(pac), Serializer.serialize(pac).length, IPAddress, 1703);
            clientSocket.send(sendPacket);
        }catch(Exception e){}


    }
}

class MyTableModel extends AbstractTableModel {

    CopyOnWriteArrayList<Shelter> shelter;


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

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex == 3) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void setShelter(CopyOnWriteArrayList<Shelter> shelter) {
        this.shelter = shelter;
    }
}

class RowFilterUtil {
    public static JTextField createRowFilter(JTable table) {
        RowSorter<? extends TableModel> rs = table.getRowSorter();
        if (rs == null) {
            table.setAutoCreateRowSorter(true);
            rs = table.getRowSorter();
        }

        TableRowSorter<? extends TableModel> rowSorter =
                (rs instanceof TableRowSorter) ? (TableRowSorter<? extends TableModel>) rs : null;

        if (rowSorter == null) {
            throw new RuntimeException("Cannot find appropriate rowSorter: " + rs);
        }

        final JTextField tf = new JTextField(15);
        tf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update(e);
            }

            private void update(DocumentEvent e) {
                String text = tf.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        return tf;
    }
}