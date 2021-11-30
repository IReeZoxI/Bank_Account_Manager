package src.Auto;

import src.Classes.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class AutoServer for automatic tests from txt
 * @author Kudriavtsev Yaroslav
 * @version 1.1
 * @since 2021-30-11
 */
public class AutoServer {

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientAccepted = null;
        ObjectInputStream sois = null;
        ObjectOutputStream soos = null;
        String clientMessageReceived;


        try{
            System.out.println("Сервер начал работу!");
            //Import csv to mysql table
            DB db = new DB("resources/bank_customers.csv");
            db.CSVtoDB();

            //Connect to port
            serverSocket = new ServerSocket (2536);
            clientAccepted = serverSocket.accept();

            //Streams for write and read messages with server
            sois = new ObjectInputStream (clientAccepted.getInputStream());
            soos = new ObjectOutputStream (clientAccepted.getOutputStream());

            while(true){
                //Read message from server
                clientMessageReceived = (String) sois.readObject();
                //Print in server message
                System.out.println(clientMessageReceived);
                //find user in table
                ArrayList<String> list = db.FindUser(clientMessageReceived);
                //Print in server message
                System.out.println("User request : " + list.toString());

                //if user in table
                if(!list.isEmpty()){
                    //If type of account = CHECKING
                    if(list.get(2).equals("CheckingAccount"))
                    {
                        //Initializing Class
                        CheckingAccount customer = new CheckingAccount(list.get(0),Double.parseDouble(list.get(1)),Integer.parseInt(list.get(3)));
                        //Print Info about customer
                        soos.writeObject(customer.ShowInfo());
                        //Working with operations
                        while (true){
                            clientMessageReceived = (String) sois.readObject();
                            ArrayList<String> var = new ArrayList<>(Arrays.asList(clientMessageReceived.split(" ")));
                            if(clientMessageReceived.contains("withdraw"))
                            {   //Balance can't be minus
                                if(customer.getBalance() -Double.parseDouble(var.get(1)) > 0.0 ){
                                    customer.Withdraw(Double.parseDouble(var.get(1)));
                                    soos.writeObject("Susses");
                                }else soos.writeObject("Failed. Balance < 0");
                            }
                            else if(clientMessageReceived.contains("deposit")) {customer.Deposit(Double.parseDouble(var.get(1)));soos.writeObject("Susses");}
                            else if(clientMessageReceived.contains("generate")) {customer.GenerateCheck();soos.writeObject("Susses");}
                            else if(clientMessageReceived.contains("exit")) break;
                            else {soos.writeObject("Unknown Command");}
                        }
                        break;
                    }
                    //If type of account = SAVINGS
                    else if(list.get(2).equals("SavingsAccount"))
                    {
                        //Initializing Class
                        SavingsAccount customer = new SavingsAccount(list.get(0),Double.parseDouble(list.get(1)),Integer.parseInt(list.get(3)));
                        //Print Info about customer
                        soos.writeObject(customer.ShowInfo());
                        while (true){
                            clientMessageReceived = (String) sois.readObject();
                            ArrayList<String> var = new ArrayList<>(Arrays.asList(clientMessageReceived.split(" ")));
                            if(clientMessageReceived.contains("withdraw"))
                            {   //Balance can't be minus
                                if(customer.getBalance() - Double.parseDouble(var.get(1)) > 0.0 ){
                                    customer.Withdraw(Double.parseDouble(var.get(1)));
                                    soos.writeObject("Susses");
                                }else soos.writeObject("Failed. Balance < 0");
                            }
                            else if(clientMessageReceived.contains("deposit")) {customer.Deposit(Double.parseDouble(var.get(1)));soos.writeObject("Susses");}
                            else if(clientMessageReceived.contains("generate")) {customer.GenerateCheck();soos.writeObject("Susses");}
                            else if(clientMessageReceived.contains("percents")) {customer.BalanceWithPercents();soos.writeObject("Susses");}
                            else if(clientMessageReceived.contains("exit")) break;
                            else {soos.writeObject("Unknown Command");}
                        }
                    }
                    else // if type of account = BUSINESS
                    {   //Initializing Class
                        BusinessAccount customer = new BusinessAccount(list.get(0),Double.parseDouble(list.get(1)),Integer.parseInt(list.get(3)));
                        //Print Info about customer
                        soos.writeObject(customer.ShowInfo());
                        while (true){
                            clientMessageReceived = (String) sois.readObject();
                            ArrayList<String> var = new ArrayList<>(Arrays.asList(clientMessageReceived.split(" ")));
                            if(clientMessageReceived.contains("withdraw"))
                            {   //Balance can't be minus
                                if(customer.getBalance() - Double.parseDouble(var.get(1)) > 0.0 ){
                                    customer.Withdraw(Double.parseDouble(var.get(1)));
                                    soos.writeObject("Susses");
                                }else soos.writeObject("Failed. Balance < 0");
                            }
                            else if(clientMessageReceived.contains("deposit")) {customer.Deposit(Double.parseDouble(var.get(1)));soos.writeObject("Susses");}
                            else if(clientMessageReceived.contains("generate")) {customer.GenerateCheck();soos.writeObject("Susses");}
                            else if(clientMessageReceived.contains("percents")) {customer.BalanceWithPercents();soos.writeObject("Susses");}
                            else if(clientMessageReceived.contains("get")) {customer.GetTransactions(Double.parseDouble(var.get(1)));soos.writeObject("Susses");}
                            else if(clientMessageReceived.contains("make")) {
                                //Balance can't be minus
                                if(customer.getBalance() - Double.parseDouble(var.get(1)) > 0.0 ){
                                    customer.MakeTransactions(Double.parseDouble(var.get(1)));
                                    soos.writeObject("Susses");
                                }else soos.writeObject("Failed. Balance < 0");
                            }
                            else if(clientMessageReceived.contains("exit")) break;
                            else {soos.writeObject("Unknown Command");}
                        }
                        break;
                    }
                }
                break;
            }

        } catch (Exception e){
            System.out.println("Ошибка: "+e);
        } finally{
            try{//Close all
                sois.close();
                soos.close();
                clientAccepted.close();
                serverSocket.close();
            } catch (Exception e){
                System.out.println("Ошибка: "+e);
            }
        }
    }
}
