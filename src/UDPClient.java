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
            //createAutReg();
            MainClientGUI test = new MainClientGUI(clientSocket,IPAddress);
            test.work("kek");
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
                        String modifiedSentence = "0";// sendwait(pacs, clientSocket, IPAddress);
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
    // Теперь работа только через авторизацию
    private void createAutReg(){
        AutorisationDialog gui = new AutorisationDialog(clientSocket,IPAddress);
        gui.firstDialog();
    }


    }
