import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainClientGUI{
    public static Locale currentLocale = new Locale("ru", "RU");
    public static ResourceBundle rb = ResourceBundle.getBundle("bundle.Lang", currentLocale);
    private JButton exit = new JButton(rb.getString("EXIT"));
    private JPanel mainPanel = new JPanel();
    private JPanel commandsP = new JPanel();
    private JPanel showP = new JPanel(); // вставить таблицу
    private JPanel userP = new JPanel();
    private JButton logout = new JButton(rb.getString("Log out"));
    private JLabel us = new JLabel();
    private JLabel help = new JLabel();
    private JFrame mainDialog;
    private JFrame sureDialog;
    private JTextField ps;
    private JTextField nm;
    private JTextField ps2;
    private JTextField nm2;
    private JTextField psif;
    private JTextField nmif;
    private String answer;
    private JFrame not;
    private MyTableModel mTabel;
    private JButton eng = new JButton();
    private JButton rus = new JButton();
    private JButton hor = new JButton();
    private JButton est = new JButton();

    Font f = new Font("Arial Black", Font.BOLD, 14);

    private ActionListener buttonListener = new FirstActionListener();
    private String login;
    private String password;
    DatagramSocket clientSocket;
    InetAddress IPAddress;
    private CopyOnWriteArrayList<Shelter> sh = new CopyOnWriteArrayList<Shelter>();
    private boolean onRun;
    private boolean onRun2 = false;

    //Конструктор графического окна запускается все из UDPCLIENT

    public MainClientGUI(DatagramSocket d, InetAddress i) {
        clientSocket = d;
        IPAddress = i;
        onRun = true;
    }

    public void work(String username){


        /*new Thread(() -> {


        }).start();*/
        login = username;

        //Первоначальные настройки
        mainDialog = new JFrame();
        mainDialog.setTitle(rb.getString("Work bro!"));
        mainDialog.getContentPane().add(mainPanel);
        mainDialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Color wind = new Color(140,165,187);
        Color w = new Color(0,5,187);
        Color w2 = new Color(140,165,0);
        mainPanel.setBackground(wind);
        commandsP.setBackground(wind);
        userP.setBackground(wind);
        mainDialog.setTitle("Не смотри сюда");
        //разбиение Главной области на 3 разных по шаблону (смотри дискорд), расскраска такая пока что чтобы понимать области
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(commandsP,BorderLayout.WEST);
        mainPanel.add(showP,BorderLayout.CENTER);
        mainPanel.add(userP,BorderLayout.EAST);



        eng.setIcon(new ImageIcon("images/nz.jpg"));
        rus.setIcon(new ImageIcon("images/rus.jpg"));
        hor.setIcon(new ImageIcon("images/horvat.png"));
        est.setIcon(new ImageIcon("images/Est.jpg"));
        est.setPreferredSize(new Dimension(50, 18));
        hor.setPreferredSize(new Dimension(50, 18));
        rus.setPreferredSize(new Dimension(50, 18));
        eng.setPreferredSize(new Dimension(50, 18));
        Box buttonbox = Box.createHorizontalBox();
        rus.setActionCommand("rus");
        eng.setActionCommand("eng");
        hor.setActionCommand("hor");
        est.setActionCommand("est");

        rus.addActionListener(buttonListener);
        eng.addActionListener(buttonListener);
        hor.addActionListener(buttonListener);
        est.addActionListener(buttonListener);
        buttonbox.add(rus);
        buttonbox.add(eng);
        buttonbox.add(hor);
        buttonbox.add(est);




        Canvas can = new Canvas(sh);

        //Работа с правой панелью
        userP.setLayout(new GridLayout(4,1));
        //Вывод активного пользователя
        ImageIcon log = new ImageIcon("images/user.png");
        us.setIcon(log);
        us.setText(rb.getString("U are log in as: ") + username);

        us.setFont(f);
        us.setBorder(BorderFactory.createTitledBorder(rb.getString("Active user")));
        userP.add(us);
        //Вывод help
        help.setText("<html><p align=\"center\">Remove - " + rb.getString("delete chosen shelter") + "<br>" +
                "Add - " + rb.getString("insert new shelter") + "<br>" +
                "Remove_last - " + rb.getString("delete last shelter") + "<br>" +
                "Remove_first - " + rb.getString("delete first shelter") + "<br>" +
                /*"show - refresh collection<br>" +*/
                "AddIfMax - " + rb.getString("insert shelter, when it has maximum position") + "<br>" +
                "Info - " + rb.getString("shows collection's information") + "</html>");
        help.setBorder(BorderFactory.createTitledBorder(rb.getString("Command description")));
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
        mainPanel.add(buttonbox,BorderLayout.NORTH);





        //Работа с средней панелью
        showP.setLayout(new GridLayout(2,1));
        JPanel tr = new JPanel(new BorderLayout());
        mTabel = new MyTableModel(sh, clientSocket, IPAddress, login, rb);
        //mTabel.setShelter(sh);
        JTable table = new JTable(mTabel);
        table.setAutoCreateRowSorter(true);
        JScrollPane jscrlp = new JScrollPane(table);
        //JTextField search = new JTextField();
        JTextField search = RowFilterUtil.createRowFilter(table);



        new Thread(() -> {
                while (onRun) {
                    Packet p = takeColl();
                    if (p.getCollection() != null){
                        System.out.println("lol");
                        sh = p.getCollection();
                        mTabel.setShelter(sh);
                        mTabel.fireTableDataChanged();

                        can.getColl(sh);



                    }else{
                        answer = p.getAns();
                        if (answer.equals("disconnected")){
                            Thread.currentThread().interrupt();
                        }
                        createNotDialog(answer);
                    }

                }
        }).start();
        tr.add(search,BorderLayout.NORTH);
        tr.add(jscrlp,BorderLayout.CENTER);

        showP.add(tr);
        try{
            Packet pac = new Packet("show",username,null);
            DatagramPacket sendPacket = new DatagramPacket(Serializer.serialize(pac), Serializer.serialize(pac).length, IPAddress, 1703);
            clientSocket.send(sendPacket);
        }
        catch (Exception e){
            System.out.println("Loook back");
        }






        //Работа с левой панелью
        commandsP.setLayout(new GridLayout(7,1));
        JLabel descrip = new JLabel(rb.getString("Commands"));
        descrip.setFont(f);
        descrip.setHorizontalAlignment(SwingConstants.CENTER);
        descrip.setBorder(BorderFactory.createRaisedBevelBorder());
        commandsP.add(descrip);

        JPanel add = new JPanel();
        add.setLayout(new BoxLayout(add,BoxLayout.Y_AXIS));
        add.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JLabel addt = new JLabel(rb.getString("Add shelter"));
        addt.setFont(f);
        addt.setHorizontalAlignment(SwingConstants.CENTER);
        add.add(addt);
        Box np = Box.createHorizontalBox();
        JLabel name = new JLabel(rb.getString("Name: "));
        name.setFont(f);
        nm = new JTextField();
        nm.setColumns(10);
        JLabel pos = new JLabel(rb.getString("Position: "));
        pos.setFont(f);
        ps = new JTextField();
        ps.setColumns(10);
        np.add(name);
        np.add(Box.createHorizontalStrut(10));
        np.add(nm);
        np.add(Box.createHorizontalStrut(10));
        np.add(pos);
        np.add(Box.createHorizontalStrut(10));
        np.add(ps);
        JButton addb = new JButton(rb.getString("Add"));
        addb.setActionCommand("add");
        addb.setFont(f);
        addb.addActionListener(buttonListener);
        add.add(Box.createVerticalStrut(10));
        add.add(np);
        add.add(Box.createVerticalStrut(4));
        add.add(addb);
        add.setBorder(BorderFactory.createLineBorder(w));
        commandsP.add(add);

        JPanel rem = new JPanel();
        rem.setLayout(new BoxLayout(rem,BoxLayout.Y_AXIS));
        rem.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JLabel remt = new JLabel(rb.getString("Remove Shelter"));
        remt.setFont(f);
        remt.setHorizontalAlignment(SwingConstants.CENTER);
        rem.add(remt);
        Box npr = Box.createHorizontalBox();
        JLabel namer = new JLabel(rb.getString("Name: "));
        namer.setFont(f);
        nm2 = new JTextField();
        nm2.setColumns(10);
        JLabel posr = new JLabel(rb.getString("Position: "));
        posr.setFont(f);
        ps2 = new JTextField();
        ps2.setColumns(10);
        npr.add(namer);
        npr.add(Box.createHorizontalStrut(10));
        npr.add(nm2);
        npr.add(Box.createHorizontalStrut(10));
        npr.add(posr);
        npr.add(Box.createHorizontalStrut(10));
        npr.add(ps2);
        JButton addbr = new JButton(rb.getString("Remove"));
        addbr.setFont(f);
        addbr.setActionCommand("rem");
        addbr.addActionListener(buttonListener);
        rem.add(Box.createVerticalStrut(10));
        rem.add(npr);
        rem.add(Box.createVerticalStrut(4));
        rem.add(addbr);
        rem.setBorder(BorderFactory.createLineBorder(w));
        commandsP.add(rem);



        JPanel addif = new JPanel();
        addif.setLayout(new BoxLayout(addif,BoxLayout.Y_AXIS));
        addif.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel addtif = new JLabel(rb.getString("Add shelter, if max"));
        addtif.setFont(f);
        addtif.setHorizontalAlignment(SwingConstants.CENTER);
        addif.add(addtif);
        Box npif = Box.createHorizontalBox();
        JLabel nameif = new JLabel(rb.getString("Name: "));
        nameif.setFont(f);
        nmif = new JTextField();
        nmif.setColumns(10);
        JLabel posif = new JLabel(rb.getString("Position: "));
        posif.setFont(f);
        psif = new JTextField();
        psif.setColumns(10);
        npif.add(nameif);
        npif.add(Box.createHorizontalStrut(10));
        npif.add(nmif);
        npif.add(Box.createHorizontalStrut(10));
        npif.add(posif);
        npif.add(Box.createHorizontalStrut(10));
        npif.add(psif);
        JPanel kek = new JPanel();
        JButton addbif = new JButton(rb.getString("Add"));
        addbif.setActionCommand("addif");
        addbif.setFont(f);
        addbif.addActionListener(buttonListener);
        addif.add(Box.createVerticalStrut(10));
        addif.add(npif);
        addif.add(Box.createVerticalStrut(4));
        kek.add(addbif);
        addif.add(kek);
        addif.setBorder(BorderFactory.createLineBorder(w));

        commandsP.add(addif);



        Box lol = Box.createHorizontalBox();
        Box removelast = Box.createVerticalBox();
        removelast.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel removl = new JLabel(rb.getString("Remove last"));
        removl.setFont(f);
        removl.setHorizontalAlignment(SwingConstants.CENTER);
        JButton rl = new JButton(rb.getString("Remove"));
        rl.setFont(f);
        rl.setActionCommand("rl");
        rl.addActionListener(buttonListener);
        removelast.add(removl);
        removelast.add(Box.createVerticalStrut(19));
        removelast.add(rl);
        lol.add(removelast);
        JPanel mem = new JPanel();
        mem.setAlignmentX(Component.CENTER_ALIGNMENT);
        mem.setBorder(BorderFactory.createLineBorder(w));
        mem.add(lol);
        commandsP.add(mem);


        Box lol2 = Box.createHorizontalBox();
        Box removefirst = Box.createVerticalBox();
        removefirst.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel removf = new JLabel(rb.getString("Remove first"));
        removf.setFont(f);
        removf.setHorizontalAlignment(SwingConstants.CENTER);
        JButton rf = new JButton(rb.getString("Remove"));
        rf.setFont(f);
        rf.setActionCommand("rf");
        rf.addActionListener(buttonListener);
        removefirst.add(removf);
        removefirst.add(Box.createVerticalStrut(19));
        removefirst.add(rf);
        lol2.add(removefirst);
        JPanel mem2 = new JPanel();
        mem2.setAlignmentX(Component.CENTER_ALIGNMENT);
        mem2.setBorder(BorderFactory.createLineBorder(w));
        mem2.add(lol2);
        commandsP.add(mem2);



        Box lol3 = Box.createHorizontalBox();
        Box info = Box.createVerticalBox();
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel inf = new JLabel(rb.getString("Information"));
        inf.setFont(f);
        inf.setHorizontalAlignment(SwingConstants.CENTER);
        JButton rfi = new JButton(rb.getString("Informate"));
        rfi.setFont(f);
        rfi.setActionCommand("in");
        rfi.addActionListener(buttonListener);
        info.add(inf);
        info.add(Box.createVerticalStrut(19));
        info.add(rfi);
        lol3.add(info);
        JPanel mem23 = new JPanel();
        mem23.setAlignmentX(Component.CENTER_ALIGNMENT);
        mem23.setBorder(BorderFactory.createLineBorder(w));
        mem23.add(lol3);
        commandsP.add(mem23);



        //Работа с графикой
        showP.add(can);





        //mainDialog.pack();
        mainDialog.setSize(1900,720);
        mainDialog.setVisible(true);

    }



    public class FirstActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //Код, который нужно выполнить при нажатии
            String s = e.getActionCommand();
            switch (s) {
                case"logout":
                    onRun = false;

                    disconect();
                    AutorisationDialog gui = new AutorisationDialog(clientSocket,IPAddress);
                    gui.firstDialog();
                    mainDialog.dispose();
                    break;
                case"exit":
                    createSureDialog();
                    break;
                case"yes":
                    disconect();
                    System.exit(0);
                    break;
                case "no":
                    sureDialog.dispose();
                    break;
                case "add":
                    System.out.println("kek");
                    String args = "{\"name\":\"" + nm.getText() + "\",\"position\":\"" + ps.getText() + "\"}";
                    Packet p = new Packet("add" , args , login , "kek");
                    sendmsg(p);
                    break;
                case "ok":
                    not.dispose();
                    break;
                case "rem":
                    String args2 = "{\"name\":\"" + nm2.getText() + "\",\"position\":\"" + ps2.getText() + "\"}";
                    Packet p2 = new Packet("remove" , args2 , login , "kek");
                    sendmsg(p2);
                    break;
                case "addif":
                    args2 = "{\"name\":\"" + nmif.getText() + "\",\"position\":\"" + psif.getText() + "\"}";
                    p2 = new Packet("add_if_max" , args2 , login , "kek");
                    sendmsg(p2);
                    break;
                case "rl":
                    p2 = new Packet("remove_last" , login , "kek");
                    sendmsg(p2);
                    break;
                case "rf":
                    p2 = new Packet("remove_first" , login , "kek");
                    sendmsg(p2);
                    break;
                case "in":
                    p2 = new Packet("info" , login , "kek");
                    sendmsg(p2);

            }
        }
    }


    public void createSureDialog(){
        sureDialog = new JFrame();
        sureDialog.setLocationRelativeTo(null);
        sureDialog.setTitle(rb.getString("NO, BRO, PLZ NOOOOO"));
        sureDialog.setResizable(false);
        JPanel sr = new JPanel(new GridLayout(2, 1));
        sureDialog.getContentPane().add(sr);
        JLabel really = new JLabel(rb.getString("Are u sure? :(("));
        really.setHorizontalAlignment(SwingConstants.CENTER);
        sr.add(really);
        JButton yes = new JButton(rb.getString("Yes"));
        JButton no = new JButton(rb.getString("Okay, i will stay with u bro"));
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
    private Packet takeColl(){
        try {
            byte[] receiveData = new byte[4098];

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            Packet pac = Serializer.deserialize(receivePacket.getData());
            return pac;
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

    private void sendmsg(Packet p){
        try{
            DatagramPacket sendPacket = new DatagramPacket(Serializer.serialize(p), Serializer.serialize(p).length, IPAddress, 1703);
            clientSocket.send(sendPacket);
        }
        catch (Exception e){
            System.out.println("Loook back");
        }
    }

    private void createNotDialog(String ans){

        not = new JFrame();
        not.setLocationRelativeTo(null);
        not.setTitle(rb.getString("What u can?"));
        JPanel max = new JPanel(new GridLayout(2,1));
        JPanel wr = new JPanel(new FlowLayout(FlowLayout.CENTER));
        max.add(wr);
        not.add(max);
        JLabel an = new JLabel(ans);
        JButton ok = new JButton(rb.getString("Ok"));
        ok.setActionCommand("ok");
        ok.addActionListener(buttonListener);
        max.add(ok);

        wr.add(an);

        not.pack();
        not.setResizable(false);
        not.setVisible(true);

    }


}

class MyTableModel extends AbstractTableModel {

    CopyOnWriteArrayList<Shelter> shelter;
    DatagramSocket socket;
    InetAddress IP;
    String log;
    ResourceBundle rb;

    public MyTableModel(CopyOnWriteArrayList<Shelter> d, DatagramSocket socket, InetAddress IP, String log, ResourceBundle rb){
        this.shelter = d;
        this.socket = socket;
        this.IP = IP;
        this.log = log;
        this.rb = rb;
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
                result = rb.getString("Name");
                break;
            case 1:
                result = rb.getString("Position");
                break;
            case 2:
                result = rb.getString("ZoneData");
                break;
            case 3:
                result = rb.getString("Creator");
                break;
        }
        return result;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex >= 2) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Shelter shg = shelter.get(rowIndex);
        if (shg.getCreator().equals(log)) {
            switch (columnIndex) {
                case 0:
                    shg.setName((String) aValue);
                    break;
                case 1:
                    shg.setX((double) aValue);
                    break;
            }
        } else {
            JFrame not = new JFrame();
            not.setLocationRelativeTo(null);
            not.setTitle("What u can?");
            JPanel max = new JPanel(new GridLayout(2,1));
            JPanel wr = new JPanel(new FlowLayout(FlowLayout.CENTER));
            max.add(wr);
            not.add(max);
            JLabel an = new JLabel("You can't edit this filed.");
            wr.add(an);
            not.pack();
            not.setResizable(false);
            not.setVisible(true);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
        try{
            Packet p = new Packet(shelter);
            DatagramPacket sendPacket = new DatagramPacket(Serializer.serialize(p), Serializer.serialize(p).length, IP, 1703);
            CopyOnWriteArrayList<Shelter> kek = Serializer.deserialize(sendPacket.getData()).getCollection();
            System.out.println(kek);
            socket.send(sendPacket);
            System.out.println("memosno");
        }
        catch (Exception e){
            System.out.println("Loook back");
        }
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
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
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0));
                }
            }
        });
        return tf;
    }
}