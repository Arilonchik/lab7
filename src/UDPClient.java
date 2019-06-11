import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class UDPClient
{
    private String login;
    private String password;
    SoundPlayer snd;
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    DatagramSocket clientSocket;
    InetAddress IPAddress;
    private void work() {



        try {
            clientSocket = new DatagramSocket();
            IPAddress = InetAddress.getByName("localhost");
            clientSocket.connect(IPAddress, 1703);


            //И если захочешь связаться с сервером, тебе нужно закоментить 2 нижнии строчки и раскоментить createAutReg() и наоборот
            createAutReg();
            //MainClientGUI test = new MainClientGUI(clientSocket,IPAddress);
            //test.work("kek");

    }catch(Exception e){
            System.out.println("No");
        }
    }


    public static void main(String args[]) throws Exception {
        UDPClient client =new UDPClient();
        client.work();
    }
    // Теперь работа только через авторизацию
    private void createAutReg(){
        AutorisationDialog gui = new AutorisationDialog(clientSocket,IPAddress);
        gui.firstDialog();
    }


    }
