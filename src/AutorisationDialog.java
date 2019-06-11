import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AutorisationDialog {
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
    private JFrame auth = new JFrame();
    JFrame wrong;
    JFrame reg = new JFrame();
    private ActionListener listenButton = new FirstActionListener();
    JPasswordField pas;
    JTextField log;
    JTextField sentem;


    public AutorisationDialog(DatagramSocket d, InetAddress i) {
        clientSocket = d;
        IPAddress = i;
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
        regb.setActionCommand("reg");
        regb.addActionListener(listenButton);
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



    private boolean authorization() {
        String l = log.getText();

        char[] p1 = pas.getPassword();
        String p = String.valueOf(p1);

        Packet pac = new Packet("A", l, p, true);
        String ans = sendwait(pac, clientSocket, IPAddress);
        if (ans.equals("Success")) {
            this.login = l;
            this.password = p;
            MainClientGUI gui = new MainClientGUI();
            gui.work(login);
            auth.dispose();
            //snd.close();
            //snd.stopPlay();
        } else {
            //System.out.println(ans);
            createAllert(ans,"Something wrong bro!");
            return false;
        }
        return true;

    }

    public boolean registration(){
        mainWin.setVisible(false);
        reg = new JFrame();
        reg.setLocationRelativeTo(null);
        reg.setTitle("Hmm, who're u?");
        reg.setResizable(false);
        JPanel regpan = new JPanel(new GridLayout(2, 1));
        reg.getContentPane().add(regpan);
        reg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel who = new JLabel("We didn't see u before...");
        who.setHorizontalAlignment(SwingConstants.CENTER);
        regpan.add(who);
        Box com = Box.createVerticalBox();
        Box text = Box.createHorizontalBox();
        Box but = Box.createHorizontalBox();

        JLabel em = new JLabel("Enter ur Email");
        sentem = new JTextField();
        text.add(em);
        text.add(Box.createHorizontalStrut(15));
        text.add(sentem);

        JButton back = new JButton("Back");
        JButton register = new JButton("Register");
        back.setActionCommand("back");
        back.addActionListener(listenButton);
        register.setActionCommand("register");
        register.addActionListener(listenButton);
        but.add(back);
        but.add(Box.createHorizontalStrut(244));
        but.add(register);
        com.add(text);
        com.add(Box.createVerticalStrut(15));
        com.add(but);
        regpan.add(com);

        reg.pack();
        reg.setSize(400, 150);
        reg.setVisible(true);

        return true;
    }

    private void singUp(){
        String em = sentem.getText();
        Packet pacs = new Packet("R", em, null, true);
        String ans = sendwait(pacs, clientSocket, IPAddress);
        if (ans.equals("Success")) {
            reg.dispose();
            auth();
            createAllert(ans,"It's okay, bro");

        } else {
            createAllert(ans,"Something wrong bro");
            //snd.startPlay("yesterday.mp3");
        }
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
                    reg.setVisible(false);
                    auth.setVisible(false);
                    mainWin.setVisible(true);
                    return;
                case "log in":
                    authorization();
                    return;
                case "ok":
                    wrong.dispose();
                    return;
                case "reg":
                    registration();
                    return;
                case "register":
                    singUp();
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
    private void createAllert(String ans, String body){

        wrong = new JFrame();
        wrong.setLocationRelativeTo(null);
        wrong.setTitle(body);
        JPanel max = new JPanel(new GridLayout(2,1));
        JPanel wr = new JPanel(new FlowLayout(FlowLayout.CENTER));
        max.add(wr);
        wrong.add(max);
        JLabel an = new JLabel(ans);
        JButton ok = new JButton("Ok");
        ok.setActionCommand("ok");
        ok.addActionListener(listenButton);
        max.add(ok);

        wr.add(an);

        wrong.pack();
        //wrong.setSize(300, 100);
        wrong.setResizable(false);
        wrong.setVisible(true);

    }
}
