package src.Classes;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for business customers
 * @author Kudriavtsev Yaroslav
 * @version 1.1
 * @since 2021-30-11
 */
public class BusinessAccount extends Account{
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
     * BusinessAccount class constructor
     * @param balance Started balance
     * @param fullname Customer name
     * @param years Amount of deposits years
     */
    public BusinessAccount(String fullname, double balance, int years){

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
        return "Business Account >> Name : " + fullname
                + " . Balance : " + balance
                + " Deposit for " + years + " years.";
    }

    /**
     * Function to return customer balance
     * @return Customer Balance
     */
    @Override
    public double getBalance() {return balance;}

    /**
     * Function to return customer fullname
     * @return Customer fullname
     */
    @Override
    String getFullName() {return fullname;}

    /**
     * Function to generate Customer Check
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
     * Function to make business transaction
     * with government taxes
     * @param amount Money for business transaction
     */
    public void MakeTransactions(double amount){
        balance -= amount + amount * taxes;
        transactions.put(LocalTime.now(),
                "Make Transaction %.2f to balance with %.2f taxes.".formatted(amount,amount*taxes));
    }

    /**
     * Function to get business transaction
     * with government taxes
     * @param amount Money for business transaction
     */
    public void GetTransactions(double amount){
        balance += amount - amount * taxes;
        transactions.put(LocalTime.now(),
                "Get Transaction %.2f to balance with %.2f taxes.".formatted(amount,amount*taxes));
    }

    /**
     * Function to take away money from account
     * without government taxes
     * @param amount Money for business transaction
     */
    public void Withdraw(double amount) {
        balance -= amount ;
        transactions.put(LocalTime.now(),
                "Withdraw %.2f to balance .".formatted(amount));
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

    /**
     * Balance after years of deposit
     */
    public void BalanceWithPercents(){
        double pbalance = balance;
        for (int i = 0; i < years; i++) {
            pbalance += pbalance * deposit_percents;
        }
        transactions.put(LocalTime.now(),
                "Balance after %d years it will be %.2f .".formatted(years,pbalance));
    }

}
