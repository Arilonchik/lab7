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
    private InetAddress adress;
    ListOfShelters list;

    public ClientThread(DatagramSocket socket, ListOfShelters list, DatagramPacket packet, Connection con, InetAddress adress) {
        this.socket = socket;
        this.list = list;
        this.packet = packet;
        this.con = con;
        this.adress = adress;
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

        String msg = new String(packet.getData()).trim();
        String req[] = msg.split(" ");

            String command = req[0];
            try {
                switch (command) {
                    case "show":
                        sendMsg(list.show(con), address, port);
                        break;
                    case "remove_last":
                        sendMsg(list.remove_last(con), address, port);
                        break;
                    case "remove_first":
                        sendMsg(list.remove_first(con), address, port);
                        break;
                    case "add":
                        sendMsg(list.add(req[1],con), address, port);
                        break;
                    case "info":
                        sendMsg(list.info(con), address, port);
                        break;
                    case "sort":
                        sendMsg(list.sort(), address, port);
                        break;
                    case "add_if_max":
                        sendMsg(list.addIfMax(req[1],con), address, port);
                        break;
                    case "remove":
                        sendMsg(list.remove(req[1],con), address, port);
                        break;
                    case "disconnect":
                        System.out.println("User with Port: " + port + ", IpAddress: " + address + " disconnect. :(");
                        break;
                    case "load":
                        sendMsg(list.load(req[1].trim()), address, port);
                        break;
                    case "save":
                        sendMsg(list.save(req[1]), address, port);
                        break;

                    default:
                        DatagramPacket response = new DatagramPacket(packet.getData(), packet.getData().length, address, port);
                        try {
                            socket.send(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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