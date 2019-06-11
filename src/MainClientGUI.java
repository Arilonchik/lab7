import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainClientGUI{
    private JButton ex = new JButton("EXIT");
    private JPanel mainPanel = new JPanel();
    private JPanel commandsP = new JPanel();
    private JPanel showP = new JPanel(); // вставить таблицу
    private JPanel userP = new JPanel();
    private JButton logout = new JButton("Log out");
    private JLabel us = new JLabel();
    private JLabel help = new JLabel();
    private JFrame mainDialog;

    private String login;
    private String password;
    DatagramSocket clientSocket;
    InetAddress IPAddress;


    private JPanel mainPanel2 = new JPanel();
    private JPanel ap = new JPanel();
    private JButton regb = new JButton("Registration");
    private JButton autb = new JButton("Authorization");
    private JButton eng = new JButton();
    private JButton rus = new JButton();
    private JButton hor = new JButton();
    private JButton est = new JButton();
    private JButton exit = new JButton("Exit");
    protected JFrame sure;
    private JFrame mainWin;
    private JFrame auth;
    private ActionListener listenButton = new AutorisationDialog.FirstActionListener();
    JPasswordField pas;
    JTextField log;

    //Конструктор графического окна запускается все из UDPCLIENT

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

        mainDialog.pack();
        mainDialog.setSize(1280,720);
        mainDialog.setVisible(true);

    }
    public void firstDialog() {
        mainWin = new JFrame();
        mainWin.setTitle("Hello brotishka!");
        mainWin.setResizable(false);
        mainWin.getContentPane().add(mainPanel2);
        mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        mainWin.setLocation((int) width / 2, (int) height / 2);

        mainPanel2.setLayout(new BorderLayout());


        eng.setIcon(new ImageIcon("images/nz.jpg"));
        rus.setIcon(new ImageIcon("images/rus.jpg"));
        hor.setIcon(new ImageIcon("images/horvat.png"));
        est.setIcon(new ImageIcon("images/Est.jpg"));
        est.setPreferredSize(new Dimension(50, 18));
        hor.setPreferredSize(new Dimension(50, 18));
        rus.setPreferredSize(new Dimension(50, 18));
        eng.setPreferredSize(new Dimension(50, 18));
        Box buttonbox = Box.createHorizontalBox();
        buttonbox.add(rus);
        buttonbox.add(eng);
        buttonbox.add(hor);
        buttonbox.add(est);
        JPanel treat = new JPanel(new FlowLayout(FlowLayout.CENTER));
        treat.add(buttonbox, JPanel.CENTER_ALIGNMENT);
        mainPanel2.add(treat, BorderLayout.NORTH);

        JPanel choose = new JPanel();
        choose.setLayout(new GridLayout(1, 2));
        autb.setPreferredSize(new Dimension(100, 18));
        autb.setActionCommand("Aut");
        autb.addActionListener(listenButton);
        regb.setPreferredSize(new Dimension(100, 18));
        choose.add(autb);
        choose.add(regb);
        mainPanel2.add(choose, BorderLayout.CENTER);
        exit.setActionCommand("exit");
        exit.addActionListener(listenButton);
        mainPanel2.add(exit, BorderLayout.AFTER_LAST_LINE);


        mainWin.pack();
        mainWin.setSize(320, 180);
        mainWin.setVisible(true);
    }

    private void auth() {
        mainWin.setVisible(false);
        auth = new JFrame();
        auth.setLocationRelativeTo(null);
        auth.setTitle("Hmm, who are u???");
        auth.setResizable(false);
        JPanel mainautp = new JPanel(new GridLayout(3, 1));
        auth.getContentPane().add(mainautp);
        auth.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel who = new JLabel("Tell to us all truth!");
        who.setHorizontalAlignment(SwingConstants.CENTER);
        mainautp.add(who);

        Box login = Box.createHorizontalBox();
        Box pass = Box.createHorizontalBox();
        Box common = Box.createVerticalBox();
        log = new JTextField(5);
        log.setPreferredSize(new Dimension(100, 18));
        JLabel elog = new JLabel("Enter ur email");
        login.add(elog);
        login.add(Box.createHorizontalStrut(30));
        login.add(log);
        pas = new JPasswordField();
        JLabel epas = new JLabel("Enter ur password");
        pass.add(epas);
        pass.add(Box.createHorizontalStrut(4));
        pass.add(pas);

        common.add(login);
        common.add(Box.createVerticalStrut(20));
        common.add(pass);

        mainautp.add(common);

        Box buttons = Box.createHorizontalBox();
        JButton back = new JButton("Back");
        JButton aut = new JButton("Log in");
        back.setActionCommand("back");
        back.addActionListener(listenButton);
        aut.setActionCommand("log in");
        aut.addActionListener(listenButton);

        buttons.add(aut);
        buttons.add(Box.createHorizontalStrut(264));
        buttons.add(back);

        mainautp.add(buttons);

        auth.pack();
        auth.setSize(400, 245);
        auth.setVisible(true);

    }

    public class FirstActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //Код, который нужно выполнить при нажатии
            String s = e.getActionCommand();
            ActionListener sa = new SupportActionListener();
            switch (s) {
                case "exit":
                    sure = new JFrame();
                    sure.setLocationRelativeTo(null);
                    sure.setTitle("NO, BRO, PLZ NOOOOO");
                    sure.setResizable(false);
                    JPanel sr = new JPanel(new GridLayout(2, 1));
                    sure.getContentPane().add(sr);
                    JLabel really = new JLabel("Are u sure? :((");
                    really.setHorizontalAlignment(SwingConstants.CENTER);
                    sr.add(really);
                    JButton yes = new JButton("Yes");
                    JButton no = new JButton("Okay, i will stay with u bro");
                    Box buttonbox = Box.createHorizontalBox();
                    yes.setPreferredSize(new Dimension(100, 18));
                    yes.setActionCommand("yes");
                    yes.addActionListener(sa);
                    no.setPreferredSize(new Dimension(100, 18));
                    no.setActionCommand("no");
                    no.addActionListener(sa);
                    buttonbox.add(yes);
                    buttonbox.add(Box.createHorizontalStrut(155));
                    buttonbox.add(no);
                    sr.add(buttonbox);

                    sure.pack();
                    sure.setSize(400, 100);
                    sure.setVisible(true);
                    return;
                case "Aut":
                    auth();
                    return;
                case "back":
                    auth.dispose();
                    mainWin.setVisible(true);
                    return;
                case "log in":
                    authorization();
                    return;
            }
        }
    }

    public class SupportActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //Код, который нужно выполнить при нажатии
            String s = e.getActionCommand();
            switch (s) {
                case "yes":
                    System.exit(0);
                    return;
                case "no":
                    sure.dispose();
            }
        }
    }

    private String authorization() {
        String l = log.getText();

        char[] p1 = pas.getPassword();
        String p = String.valueOf(p1);

        Packet pac = new Packet("A", l, p, true);
        String ans = sendwait(pac, clientSocket, IPAddress);
        if (ans.equals("Success")) {
            this.login = l;
            this.password = p;

            //snd.close();
            //snd.stopPlay();
        } else {
            System.out.println(ans + " Try again");
        }
        return null;

    }

    private String sendwait(Packet pac, DatagramSocket clientSocket, InetAddress IPAddress) {
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket sendPacket = new DatagramPacket(Serializer.serialize(pac), Serializer.serialize(pac).length, IPAddress, 1703);
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData()).trim();
            return modifiedSentence;
        } catch (IOException e) {
            System.out.println("Something goes wrong...");
            return null;
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