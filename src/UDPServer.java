import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class UDPServer {
    private DatagramSocket udpSocket;
    private Connection con;
    private ArrayList<User> users = new ArrayList<>();
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
        ArrayList<InetAddress> addresses = new ArrayList<>();


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

                    new ClientThread(udpSocket, list, request, con, request.getAddress(),packet);
                    try{

                        boolean check = Serializer.deserialize(request.getData()).getAt();
                        User us = new User(request.getAddress(),request.getPort());
                        if(!users.contains(us) && !check){
                            users.add(us);
                            System.out.println("keks");
                        }
                        if( Serializer.deserialize(request.getData()).getCommand().equals("disconnect")){
                            users.remove(us);
                            System.out.println("lolololol");
                            System.out.println(users);
                        }
                    }catch (ClassNotFoundException e){
                        e.printStackTrace();
                    }

                    spam(list.show(),users);
                    list.savePost();
                    if (packet.getArgument() != null) {
                        System.out.println("Message from " +
                                packet.getLogin() +
                                ":/" + request.getPort() +
                                ": " + packet.getCommand() + " " +
                                packet.getArgument());
                    } else {
                        System.out.println("Message from " +
                                packet.getLogin() +
                                ":/" + request.getPort() +
                                ": " + packet.getCommand());
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
            //String data =inr.readLine();
            String data ="Collectionslab";
            System.out.print("Connecting to " + data + "...\nEnter SQL login: ");
            String login =  "postgres"; //
            inr.readLine();
            System.out.print("Password: ");
            String pas = "postgres";
            DataConnection Dcon = new DataConnection(data, login, pas);
            Connection con = Dcon.connect();
            if (!con.equals(null)) {
                System.out.println("Success");
                System.out.println("New server?");
                String d =inr.readLine();
                if (d.equals("yes")){
                    try{
                        String sql = "DROP TABLE IF EXISTS \"COLLECTION\";\n" +
                                " \n" +
                                "CREATE TABLE \"COLLECTION\"\n" +
                                "(\n" +
                                "       \t\"NAME\" VARCHAR NOT NULL,\n" +
                                "        \"DATE\" VARCHAR NOT NULL,\n" +
                                "        \"CREATOR\" VARCHAR NOT NULL,\n" +
                                "       \t\"POSITION\" VARCHAR NOT NULL\n" +
                                ");\nDROP TABLE IF EXISTS \"USERS\";\n" +
                                " \n" +
                                "CREATE TABLE \"USERS\"\n" +
                                "(\n" +
                                "       \t\"EMAIL\" VARCHAR NOT NULL,\n" +
                                "        \"PASS\" VARCHAR NOT NULL\n" +
                                ");";
                        PreparedStatement psmt = con.prepareStatement(sql);
                        psmt.executeUpdate();
                        psmt.close();
                    }catch(SQLException e){
                        System.out.println("Not work");
                        return con;
                    }
                }
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
    private void spam(CopyOnWriteArrayList<Shelter> sh, ArrayList<User> ip){
        Packet pac = new Packet(sh);
        int port;
        InetAddress adr;
            for (User i : ip) {
                try {
                port = i.getPort();
                adr = i.getIPadress();
                DatagramPacket h = new DatagramPacket(Serializer.serialize(pac), Serializer.serialize(pac).length, adr, port);
                udpSocket.send(h);
                System.out.println(i);
                }catch(IOException ex){
                    ex.printStackTrace();
                    continue;
                }
            }

    }


}