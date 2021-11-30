package src.Auto;

import java.io.*;
import java.net.*;

/**
 * Class AutoClient for automatic tests from txt
 * @author Kudriavtsev Yaroslav
 * @version 1.1
 * @since 2021-30-11
 */
public class AutoClient {
    public static void main(String[] args) {

        String messageClient;
        try{
            System.out.println("Подключение к серверу...");
            //Connect to server
            Socket clientSock = new Socket("127.0.0.1", 2536);

            System.out.println("Соединение установлено!");
            //Streams for write and read messages with server
            ObjectOutputStream coos = new ObjectOutputStream (clientSock.getOutputStream());
            ObjectInputStream cois = new ObjectInputStream (clientSock.getInputStream());
            //Path to test file
            String path = "resources/SavingsAccount.txt";
            FileInputStream fstream = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            //Read line by line file
            while ((strLine = br.readLine()) != null) {
                //Check line empty or no
                if (strLine.length() == 0) {
                    coos.writeObject(null);
                    break;
                }
                //Print command
                System.out.println(strLine);
                //Send to server command
                coos.writeObject(strLine);
                //Get answer
                messageClient = (String) cois.readObject();
                //Print answer
                System.out.println(messageClient);
            }
            //Close all
            coos.close();
            cois.close();
            br.close();
            fstream.close();
            clientSock.close();

        } catch (Exception e){
            System.out.println("Program ended");
        }
    }
}