import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class UDPClient
{
    private String login;
    private String password;
    private void work() {
        SoundPlayer snd;

        try {
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            Scanner scanner = new Scanner(System.in);
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");
            clientSocket.connect(IPAddress, 1703);

            //AutReg ar = new AutReg();
            System.out.println("\nHello! " +
                    "It's a console.\n" +
                    "List of command: remove_last, remove {element}, show, add_if_max {element}, " +
                    "remove_first, info, add {element}");
            //Работа с визуальной частью регистрации
            boolean check = true;
            System.out.println("Hello, u need to auth or register, CHOOSE YOUR DESTINY: A/R?");
            //Вкинуть сюда музяку из морты
           snd = new SoundPlayer("mk11.mp3");
            while (check) {
                System.out.println("A/R?");
                String ar = inFromUser.readLine().trim();

                try{
                switch (ar) {
                        case "A":
                            System.out.println("Enter login: ");
                            String log = inFromUser.readLine();
                            System.out.println("Enter password: ");
                            String pas = inFromUser.readLine();
                            Packet pac = new Packet("A", log, pas, true);
                            String ans = sendwait(pac, clientSocket, IPAddress);
                            if (ans.equals("Success")) {
                                System.out.println("U have logged in with " + log);
                                this.login = log;
                                this.password = pas;
                                //snd.close();
                                //snd.stopPlay();
                                check = false;
                                snd.close();
                            } else {
                                System.out.println(ans + " Try again");
                            }
                            break;
                        case "R":
                            System.out.println("Enter ur email: ");
                            String em = inFromUser.readLine();
                            Packet pacs = new Packet("R", em, null, true);
                            ans = sendwait(pacs, clientSocket, IPAddress);
                            if (ans.equals("Success")) {
                                System.out.println("U have registered with " + em + " Ur password has been sent to ur email");
                            } else {
                                System.out.println(ans + " Try again");
                                //snd.startPlay("yesterday.mp3");
                            }
                            break;
                        default:
                            System.out.println("Invalid message");
                            break;

                    }
                }catch(NullPointerException e){ System.out.println("Сервер временно недоступен, подождите...\n" + secret.getsos());


                    continue;}
            }

            while (true) {
                try {
                    System.out.print("-> ");
                    String sentence = inFromUser.readLine().trim();
                    String req[] = sentence.split(" ");
                    String command = req[0];
                    String argument = null;
                    if(req.length > 1){
                    argument = req[1];}
                    /*String command = scanner.next();
                    String argument;
                    if (scanner.hasNext()) {
                        argument = scanner.nextLine().trim();
                    }
                    else{
                        argument = null;
                    }*/

                    if (command.equals("import")) {
                        try {
                            //sentence = (sentence + " " + login + " " + password);
                            Packet pacs = new Packet(command,login,password);
                            byte[] data = new byte[1000];
                            System.out.println("Введите название файла плез:");
                            System.out.print("-> ");
                            String path = inFromUser.readLine();
                            pacs.setPath(path);
                            DatagramSocket s = new DatagramSocket();
                            InetAddress addr = InetAddress.getLocalHost();
                            DatagramPacket sendPacket = new DatagramPacket(Serializer.serialize(pacs), Serializer.serialize(pacs).length, IPAddress, 1703);
                            clientSocket.send(sendPacket);
                            FileInputStream fr = new FileInputStream(new File(path));
                            DatagramPacket pac;
                            while (fr.read(data) != -1) {
//создание пакета данных
                                pac = new DatagramPacket(data, data.length, addr, 1703);
                                s.send(pac);//отправление пакета
                            }
                            fr.close();
                            System.out.println("Файл отправлен и сохранен в imp_data.xml");
                        } catch (UnknownHostException e) {
// неверный адрес получателя
                            e.printStackTrace();
                        } catch (SocketException e) {
// возникли ошибки при передаче данных
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
// не найден отправляемый файл
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    if (command.equals("disconnect")) {
                        //sentence = (sentence + " " + login + " " + password);
                        Packet pacs = new Packet(command,login,password);
                        DatagramPacket sendPacket = new DatagramPacket(Serializer.serialize(pacs), Serializer.serialize(pacs).length, IPAddress, 1703);
                        clientSocket.send(sendPacket);
                        break;
                    } else if (!command.equals("import")) {
                        //sentence = (sentence + " " + login + " " + password);
                        Packet pacs;
                        if (argument!= null) {
                            pacs = new Packet(command,argument, login, password);
                        }else{
                            pacs = new Packet(command, login, password);
                        }
                        String modifiedSentence = sendwait(pacs, clientSocket, IPAddress);
                        System.out.println("FROM SERVER:\n" + modifiedSentence);
                        if (modifiedSentence.equals("Enter is wrong.")){new SoundPlayer("loose.mp3",200);
                        }
                    }
                } catch (PortUnreachableException | NullPointerException ex) {
                    System.out.println("Сервер временно недоступен, подождите...\n" + secret.getsos());
                    new SoundPlayer("dimon.mp3",200);
                    continue;
                } catch (IOException e) {
                    System.out.println("Something goes wrong");
                }
            }

            clientSocket.close();
        } catch (IOException e){
            System.out.println("Something goes wrong");
        }
    }


    public static void main(String args[]) throws Exception {
        UDPClient client =new UDPClient();
        client.work();
    }


    //Более удобный варинат отправления и принятия пакета
    private String sendwait(Packet pac, DatagramSocket clientSocket, InetAddress IPAddress){
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket sendPacket = new DatagramPacket(Serializer.serialize(pac), Serializer.serialize(pac).length, IPAddress, 1703);
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData()).trim();
            return modifiedSentence;
        }catch(IOException e){
            System.out.println("Something goes wrong...");
            return null;
        }
    }

}