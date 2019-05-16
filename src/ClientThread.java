import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientThread  extends Thread {
    private DatagramSocket socket;
    private DatagramPacket packet;
    private Connection con;
    @SuppressWarnings("FieldCanBeLocal")
    private InetAddress adress;
    ListOfShelters list;
    private Packet pac;

    public ClientThread(DatagramSocket socket, ListOfShelters list, DatagramPacket packet, Connection con, InetAddress adress, Packet pac) {
        this.socket = socket;
        this.list = list;
        this.packet = packet;
        this.con = con;
        this.adress = adress;
        this.pac=pac;
        System.out.println("kekkeded");
        start();

    }

    @Override
    public void run() {
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        AutReg ar = new AutReg();

        //String msg = new String(packet.getData()).trim();
        //String req[] = msg.split(" ");


            String command = pac.getCommand();
            try {
                switch (command) {
                    case "show":
                        sendMsg(list.show(), address, port);
                        break;
                    case "remove_last":
                        sendMsg(list.remove_last(pac.getLogin()), address, port);
                        break;
                    case "remove_first":
                        sendMsg(list.remove_first(pac.getLogin()), address, port);
                        break;
                    case "add":
                        sendMsg(list.add(pac.getArgument(),pac.getLogin()), address, port);
                        break;
                    case "info":
                        sendMsg(list.info(), address, port);
                        break;
                    case "sort":
                        sendMsg(list.sort(), address, port);
                        break;
                    case "add_if_max":
                        sendMsg(list.addIfMax(pac.getArgument(),pac.getLogin()), address, port);
                        break;
                    case "remove":
                        sendMsg(list.remove(pac.getArgument(), pac.getLogin()), address, port);
                        break;
                    case "disconnect":
                        System.out.println("User with Port: " + port + ", IpAddress: " + address + " disconnect. :(");
                        break;
                    case "load":
                        sendMsg(list.load(pac.getArgument()), address, port);
                        break;
                    case "save":
                        sendMsg(list.save(pac.getArgument()), address, port);
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
        msg += "\n";
        DatagramPacket h = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, port);
        try {
            System.out.println("sent");
            socket.send(h);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}