import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutorisationDialog {
    private JPanel mainPanel = new JPanel();
    private JPanel ap = new JPanel();
    private JButton regb = new JButton("Registration");
    private  JButton autb = new JButton("Authorization");
    private JButton eng = new JButton();
    private  JButton rus = new JButton();
    private JButton hor = new JButton();
    private  JButton est = new JButton();
    private JButton exit = new JButton("Exit");
    protected JFrame sure;
    private JFrame mainWin;
    private JFrame auth;

    public void firstDialog(){
        mainWin = new JFrame();
        mainWin.setTitle("Hello brotishka!");
        mainWin.setResizable(false);
        mainWin.getContentPane().add(mainPanel);
        mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        mainWin.setLocation((int)width/2,(int)height/2);

        mainPanel.setLayout(new BorderLayout());

        ActionListener listenButton = new FirstActionListener();

        eng.setIcon(new ImageIcon("images/nz.jpg"));
        rus.setIcon(new ImageIcon("images/rus.jpg"));
        hor.setIcon(new ImageIcon("images/horvat.png"));
        est.setIcon(new ImageIcon("images/Est.jpg"));
        est.setPreferredSize(new Dimension(50,18));
        hor.setPreferredSize(new Dimension(50,18));
        rus.setPreferredSize(new Dimension(50,18));
        eng.setPreferredSize(new Dimension(50,18));
        Box buttonbox = Box.createHorizontalBox();
        buttonbox.add(rus);
        buttonbox.add(eng);
        buttonbox.add(hor);
        buttonbox.add(est);
        JPanel treat = new JPanel(new FlowLayout(FlowLayout.CENTER));
        treat.add(buttonbox,JPanel.CENTER_ALIGNMENT);
        mainPanel.add(treat,BorderLayout.NORTH);

        JPanel choose = new JPanel();
        choose.setLayout(new GridLayout(1,2));
        autb.setPreferredSize(new Dimension(100,18));
        autb.setActionCommand("Aut");
        autb.addActionListener(listenButton);
        regb.setPreferredSize(new Dimension(100,18));
        choose.add(autb);
        choose.add(regb);
        mainPanel.add(choose,BorderLayout.CENTER);
        exit.setActionCommand("exit");
        exit.addActionListener(listenButton);
        mainPanel.add(exit,BorderLayout.AFTER_LAST_LINE);



        mainWin.pack();
        mainWin.setSize(320,180);
        mainWin.setVisible(true);
    }
    private void auth(){
        mainWin.setVisible(false);
        auth = new JFrame();
        auth.setLocationRelativeTo(null);
        auth.setTitle("Hmm, who are u???");
        auth.setResizable(false);
        JPanel mainautp = new JPanel(new GridLayout(3,1));
        auth.getContentPane().add(mainautp);
        auth.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel who = new JLabel("Tell to us all truth!");
        who.setHorizontalAlignment(SwingConstants.CENTER);
        mainautp.add(who);

        Box login = Box.createHorizontalBox();
        Box pass = Box.createHorizontalBox();
        JTextField log = new JTextField(5);
        log.setPreferredSize(new Dimension(100,18));
        JLabel elog = new JLabel("Enter ur email");
        login.add(elog);
        login.add(Box.createHorizontalStrut(30));
        login.add(log);
        JPasswordField pas = new JPasswordField();
        JLabel epas = new JLabel("Enter ur password");
        pass.add(epas);
        pass.add(Box.createHorizontalStrut(30));
        pass.add(pas);

        mainautp.add(login);
        mainautp.add(pass);



        auth.pack();
        auth.setSize(400,200);
        auth.setVisible(true);

    }
    public class FirstActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //Код, который нужно выполнить при нажатии
            String s = e.getActionCommand();
            ActionListener sa = new SupportActionListener();
            switch(s){
                case "exit":
                    sure = new JFrame();
                    sure.setLocationRelativeTo(null);
                    sure.setTitle("NO, BRO, PLZ NOOOOO");
                    sure.setResizable(false);
                    JPanel sr = new JPanel(new GridLayout(2,1));
                    sure.getContentPane().add(sr);
                    JLabel really = new JLabel("Are u sure? :((");
                    really.setHorizontalAlignment(SwingConstants.CENTER);
                    sr.add(really);
                    JButton yes = new JButton("Yes");
                    JButton no = new JButton("Okay, i will stay with u bro");
                    Box buttonbox = Box.createHorizontalBox();
                    yes.setPreferredSize(new Dimension(100,18));
                    yes.setActionCommand("yes");
                    yes.addActionListener(sa);
                    no.setPreferredSize(new Dimension(100,18));
                    no.setActionCommand("no");
                    no.addActionListener(sa);
                    buttonbox.add(yes);
                    buttonbox.add(Box.createHorizontalStrut(155));
                    buttonbox.add(no);
                    sr.add(buttonbox);

                    sure.pack();
                    sure.setSize(400,100);
                    sure.setVisible(true);
                    return;
                case "Aut":
                    auth();
                    return;
            }
        }
    }

    public class SupportActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //Код, который нужно выполнить при нажатии
        String s = e.getActionCommand();
        switch(s){
            case "yes":
                System.exit(0);
                return;
            case "no":
                sure.dispose();
        }
        }
    }
}
