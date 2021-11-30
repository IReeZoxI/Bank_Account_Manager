package src.Classes;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for withdraws and deposits
 * @author Kudriavtsev Yaroslav
 * @version 1.1
 * @since 2021-30-11
 **/
public class CheckingAccount extends Account {
    /**
     * Years of deposit
     */
    private final int years;
    /**
     * Customer balance
     */
    private double balance;
    /**
     * FullName of Customer
     */
    private final String fullname;
    /**
     * Dictionary for operations
     */
    Map<LocalTime,String> transactions = new HashMap<>();

    /**
     * CheckingAccount class constructor
     * @param balance Started balance
     * @param fullname Customer name
     * @param years = 0 . Checking Account can't deposit money
     */
    public CheckingAccount(String fullname,double balance,int years) {
        this.fullname = fullname;
        this.balance = balance;
        this.years = years;
    }

    /**
     * Function to show Customers info
     * @return Full Info about Customer
     */
    @Override
    public String ShowInfo() {
        return "Checking Account >> Name : " + fullname
                + " . Balance : " + balance ;
    }

    /**
     * Function to return customer balance
     * @return Customer Balance
     */
    @Override
    public double getBalance() {
        return balance;
    }

    /**
     * Function to return customer fullname
     * @return Customer fullname
     */
    @Override
    String getFullName() {
        return fullname;
    }

    /**
     * Function to generate check
     */
    @Override
    public void GenerateCheck() {
        try {
            FileWriter myWriter = new FileWriter("resources/%s Check.txt".formatted(fullname));
            myWriter.write(ShowInfo());
            myWriter.write("\n \n");
            for(Map.Entry<LocalTime, String> item : transactions.entrySet()){

                myWriter.write("\nTime : %s  Action: %s \n".formatted( item.getKey(), item.getValue()));
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Function to take away money from account
     * without government taxes
     * @param amount Money for business transaction
     */
    public void Withdraw(double amount) {
        if (balance - amount > 0) {
            balance -= amount;
            transactions.put(LocalTime.now(),
                    "Withdraw %.2f to balance .".formatted(amount));
        }

    }

    /**
     * Function to accept money for account
     * without government taxes
     * @param amount Money for business transaction
     */
    public void Deposit(double amount) {
        balance += amount;
        transactions.put(LocalTime.now(),
                "Deposit %.2f to balance .".formatted(amount));
    }
}


