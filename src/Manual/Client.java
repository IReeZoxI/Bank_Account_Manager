package src.Manual;

import java.io.*;
import java.net.*;

/**
 * Class Client to automatic tests from txt
 * @author Kydravtsev Yaroslav
 * @version 1.1
 * @since 2021-30-11
 */
public class Client {
    public static void main(String[] args) {
        String message;
        String messageClient;

        try{
            System.out.println("Подключение к серверу...");
            //Connect to server
            Socket clientSock = new Socket("127.0.0.1", 2536);

            System.out.println("Соединение установлено!");

            //Streams for write and read messages with server
            ObjectOutputStream coos = new ObjectOutputStream (clientSock.getOutputStream());
            BufferedReader stdin = new BufferedReader (new InputStreamReader (System.in));
            ObjectInputStream cois = new ObjectInputStream (clientSock.getInputStream());
            //Cycle to read and send messages to server
            while(true){
                System.out.print("Write info : ");
                message = stdin.readLine();
                if(message.length() == 0 ){
                    coos.writeObject(null);
                    break;
                }
                //write message to server
                coos.writeObject(message);
                //read message from server
                messageClient  = (String) cois.readObject();
                //show message
                System.out.println(messageClient);
            }
            //Close all
            coos.close();
            cois.close();
            clientSock.close();

        } catch (Exception e){
            System.out.println("Ошибка: "+e);
        }
    }
}