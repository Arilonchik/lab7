import java.io.*;
import java.net.*;

class UDPClient
{
    private String login;
    private String password;
    private void work() {
        try {
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
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
            SoundPlayer snd = SoundPlayer.playSound("mk11.wav");
            snd.start();
            while (check) {
                System.out.println("A/R?");
                String ar = inFromUser.readLine().trim();

                switch (ar) {
                    case "A":
                        String answ = sendwait("A", clientSocket, IPAddress);
                        System.out.println("Enter login: ");
                        String log = inFromUser.readLine();
                        System.out.println("Enter password: ");
                        String pas = inFromUser.readLine();
                        String logp = log + "_" + pas;
                        String ans = sendwait(logp, clientSocket, IPAddress);
                        if (ans.equals("Success")) {
                            System.out.println("U have logged in with " + log);
                            this.login = log;
                            this.password = pas;
                            snd.close();
                            check = false;
                        } else {
                            System.out.println(ans + " Try again");
                        }
                        break;
                    case "R":
                        String anse = sendwait("R", clientSocket, IPAddress);
                        System.out.println("Enter ur email: ");
                        String em = inFromUser.readLine();
                        ans = sendwait(em, clientSocket, IPAddress);
                        if (ans.equals("Success")) {
                            System.out.println("U have registered with " + em + " Ur password has been sent to ur email");
                        } else {
                            System.out.println(ans + " Try again");
                        }
                        break;
                    default:
                        System.out.println("Invalid message");
                        break;

                }
            }

            while (true) {
                try {
                    System.out.print("-> ");
                    String sentence = inFromUser.readLine().trim();
                    if (sentence.equals("import")) {
                        try {
                            sentence = (sentence + " " + login + " " + password);
                            byte[] data = new byte[1000];
                            System.out.println("Введите название файла плез:");
                            System.out.print("-> ");
                            String path = inFromUser.readLine();
                            DatagramSocket s = new DatagramSocket();
                            InetAddress addr = InetAddress.getLocalHost();
                            DatagramPacket sendPacket = new DatagramPacket(sentence.getBytes(), sentence.getBytes().length, IPAddress, 1703);
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
                    if (sentence.equals("disconnect")) {
                        sentence = (sentence + " " + login + " " + password);
                        DatagramPacket sendPacket = new DatagramPacket(sentence.getBytes(), sentence.getBytes().length, IPAddress, 1703);
                        clientSocket.send(sendPacket);
                        break;
                    } else if (!sentence.equals("import")) {
                        sentence = (sentence + " " + login + " " + password);
                        String modifiedSentence = sendwait(sentence, clientSocket, IPAddress);
                        System.out.println("FROM SERVER:\n" + modifiedSentence);
                    }
                } catch (PortUnreachableException ex) {
                    System.out.println("Сервер временно недоступен, подождите...\n" + secret.getsos());
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
    private String sendwait(String sentence, DatagramSocket clientSocket, InetAddress IPAddress){
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket sendPacket = new DatagramPacket(sentence.getBytes(), sentence.getBytes().length, IPAddress, 1703);
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