import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientThread  extends Thread {
    private DatagramSocket socket;
    private DatagramPacket packet;
    private Connection con;
    @SuppressWarnings("FieldCanBeLocal")
    private InetAddress adress;
    ListOfShelters list;
    private Packet pac;
    private ArrayList<User> us;

    public ClientThread(DatagramSocket socket, ListOfShelters list, DatagramPacket packet, Connection con, InetAddress adress, Packet pac, ArrayList<User> us) {
        this.socket = socket;
        this.list = list;
        this.packet = packet;
        this.con = con;
        this.adress = adress;
        this.pac=pac;
        this.us = us;
        start();

    }

    @Override
    public void run() {
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        AutReg ar = new AutReg();

        //String msg = new String(packet.getData()).trim();
        //String req[] = msg.split(" ");


            String command = pac.getCommand();
            try {
                switch (command) {
                    case "show":
                        System.out.println("kek");
                        spam(list.show(),us,packet);
                        break;
                    case "remove_last":
                        sendMsg(list.remove_last(pac.getLogin()), address, port);
                        spam(list.show(),us,packet);
                        break;
                    case "remove_first":
                        sendMsg(list.remove_first(pac.getLogin()), address, port);
                        spam(list.show(),us,packet);
                        break;
                    case "add":
                        sendMsg(list.add(pac.getArgument(),pac.getLogin()), address, port);
                        spam(list.show(),us,packet);
                        break;
                    case "info":
                        sendMsg(list.info(), address, port);
                        break;
                    case "sort":
                        sendMsg(list.sort(), address, port);
                        break;
                    case "add_if_max":
                        sendMsg(list.addIfMax(pac.getArgument(),pac.getLogin()), address, port);
                        spam(list.show(),us,packet);
                        break;
                    case "remove":
                        sendMsg(list.remove(pac.getArgument(), pac.getLogin()), address, port);
                        spam(list.show(),us,packet);
                        break;
                    case "disconnect":
                        System.out.println("User with Port: " + port + ", IpAddress: " + pac.getLogin() + " disconnect. :(");
                        break;
                    case "load":
                        sendMsg(list.load(pac.getArgument()), address, port);
                        break;
                    case "save":
                        sendMsg(list.save(pac.getArgument()), address, port);
                        break;
                    case "help":
                        sendMsg(list.help(), address, port);
                        break;
                    case "A":
                        if(pac.getAt() == true){
                        String ans = ar.auth(con, pac.getLogin(), pac.getPassword());
                        sendMsg(ans, packet.getAddress(), packet.getPort());}
                        else{sendMsg("U had already logged in", packet.getAddress(), packet.getPort());}
                        break;
                    case "R":
                        if (pac.getAt()){
                        String answer = ar.reg(con,pac.getLogin());
                        sendMsg(answer, packet.getAddress(), packet.getPort());}
                        else{sendMsg("U had already logged in", packet.getAddress(), packet.getPort());}
                        break;
                    default:
                        String sm = "Unknown command";
                        sendMsg(sm,address,port);
                        break;
                }
            } catch (JsonSyntaxException | NullPointerException | NoSuchElementException e) {
                sendMsg("Enter is wrong.", address, port);
                e.printStackTrace();
            }

    }

    private void sendMsg(String msg, InetAddress address, int port) {
        try {
            Packet p = new Packet(msg,true);
            DatagramPacket h = new DatagramPacket(Serializer.serialize(p), Serializer.serialize(p).length, address, port);
            socket.send(h);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void spam(CopyOnWriteArrayList<Shelter> sh, ArrayList<User> ip, DatagramPacket request){

        try{

            boolean check = Serializer.deserialize(request.getData()).getAt();
            User us = new User(request.getAddress(),request.getPort());
            if(!ip.contains(us) && !check){
                ip.add(us);
                System.out.println("keks");
            }
            if( Serializer.deserialize(request.getData()).getCommand().equals("disconnect")){
                ip.remove(us);
                System.out.println("lolololol");
                System.out.println(ip);
            }
        }catch (ClassNotFoundException| IOException e){
            e.printStackTrace();
        }


        Packet pac = new Packet(sh);
        System.out.println(sh);
        int port;
        InetAddress adr;
        for (User i : ip) {
            try {
                port = i.getPort();
                adr = i.getIPadress();
                DatagramPacket h = new DatagramPacket(Serializer.serialize(pac), Serializer.serialize(pac).length, adr, port);
                socket.send(h);
                System.out.println(i);
            }catch(IOException ex){
                ex.printStackTrace();
                continue;
            }
        }

    }



}