import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class UDPServer {
    private DatagramSocket udpSocket;
    private Connection con;
    public UDPServer(int port) throws IOException {
        udpSocket = new DatagramSocket(port);
        System.out.println("-- Running Server at " + InetAddress.getLocalHost() + " --");
    }

    public void work() throws IOException {
        String msg;
        /*int minuts = 10;
        int time = minuts*60000;
        udpSocket.setSoTimeout(time);*/

        //Блок регистрации БД
        con = getCon();
        new Thread(() -> {
            Scanner listen = new Scanner(System.in);
            while (true) {
                String exit = listen.nextLine();
                if (exit.equals("stop")) {
                    System.exit(0);
                }
            }
        }).start();

        ListOfShelters list = new ListOfShelters("Downloads/collection.xml");
        try {
            list = takeCol(con);
        }catch(SQLException e){
            System.out.println("Got wrong...");
        }
        Runtime.getRuntime().addShutdownHook(new Thread(list::saveOnServer));
        AutReg ar = new AutReg();

        while (true) {
            try {
                byte[] buf1 = new byte[1024];
                DatagramPacket request = new DatagramPacket(buf1, buf1.length);

                udpSocket.receive(request);
                Packet packet = null;
                try {
                    packet = Serializer.deserialize(request.getData());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if(packet.getLogin()!= null) {
                    if (packet.getCommand().equals("import")) {
                        File file = new File("imp_data.xml");
                        System.out.println("Прием данных…");
                        try { // прием файла
                            acceptFile(file, udpSocket, 1000);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    new ClientThread(udpSocket, list, request, con, request.getAddress());
                    msg = new String(request.getData()).trim();
                    System.out.println("Message from " + request.getAddress().getHostAddress() + ":/" + request.getPort() + ": " + msg);
                } else{

                    switch (packet.getCommand()){
                        case "A":
                            sendMsg("A",request.getAddress(), request.getPort());
                            String a = takeMsg();
                            String logp[] = a.split("_");
                            System.out.println(logp[0] + logp[1]);
                            String ans = ar.auth(con, logp[0], logp[1]);
                            sendMsg(ans, request.getAddress(), request.getPort());
                        break;
                    case "R":
                        sendMsg("R",request.getAddress(), request.getPort());
                            String em = takeMsg();
                            String answer = ar.reg(con,em);
                            sendMsg(answer, request.getAddress(), request.getPort());
                        break;
                    }
                }
            } catch (IOException e) {

                e.printStackTrace();

            }
        }

    }


    public static void main(String[] args) throws Exception {
        UDPServer server = new UDPServer(1703);
        server.work();
    }


    //Подключение к базе данных
    private Connection getCon(){
        try {
            BufferedReader inr = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter database: ");
            String data ="Collectionslab";// inr.readLine();
            System.out.print("Connecting to " + data + "...\nEnter SQL login: ");
            String login ="postgres"; //inr.readLine();
            System.out.print("Password: ");
            String pas = "postgres"; inr.readLine();
            DataConnection Dcon = new DataConnection(data, login, pas);
            Connection con = Dcon.connect();
            if (!con.equals(null)) {
                System.out.println("Success");
                return con;
            } else {
                System.out.println("Fuck");
                return null;
            }
        }catch(IOException e){
            System.out.println("wtf?");
            return null;
        }
    }

    //реализация import
    private static void acceptFile(File file, DatagramSocket udpSocket, int pacSize) throws IOException {
        byte data[] = new byte[pacSize];
        DatagramPacket pac = new DatagramPacket(data, data.length);
        FileOutputStream os = new FileOutputStream(file);
        try {
            udpSocket.setSoTimeout(6000);
            while (true) {
                udpSocket.receive(pac);
                os.write(data);
                os.flush();
            }
        } catch (SocketTimeoutException e) {
// если время ожидания вышло
            os.close();
            System.out.println("Истекло время ожидания, прием данных закончен");
            udpSocket.setSoTimeout(600000000);
        }
    }

    //Прием пакета
    private String takeMsg(){
        try {
            byte[] buf2 = new byte[1024];
            DatagramPacket req2 = new DatagramPacket(buf2, buf2.length);
            udpSocket.receive(req2);
            String ans = new String(req2.getData()).trim();
            Scanner scanner = new Scanner(ans);
            String command = scanner.next();
            return command;
        }catch (IOException e){
            e.printStackTrace();
            return null;

        }
    }

    private void sendMsg(String msg, InetAddress address, int port) {
        msg += "\n";
        DatagramPacket h = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, port);
        try {
            System.out.println("sent");
            udpSocket.send(h);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param c
     * @return
     * @throws SQLException
     */
    private ListOfShelters takeCol(Connection c) throws SQLException {
            CopyOnWriteArrayList<Shelter> sh = new CopyOnWriteArrayList<Shelter>();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM \"COLLECTION\";");
            while (rs.next()) {
                String name = rs.getString(1);
                String date = rs.getString(2);
                String cr = rs.getString(3);
                String pos = rs.getString(4);
                Double d2 = Double.valueOf(pos);
                Shelter shel = new Shelter(d2, name, cr, date);
                sh.add(shel);
            }
            ListOfShelters list = new ListOfShelters(sh,c);
            return list;
    }

}