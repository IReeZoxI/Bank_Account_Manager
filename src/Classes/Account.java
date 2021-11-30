package src.Classes;

/**
 * Abstract Class Account with abstract methods
 * @author Kudriavtsev Yaroslav
 * @version 1.1
 * @since 2021-30-11
 */
abstract class Account {
    /**
     * Percents for deposit
     */
    final double deposit_percents = 0.15;
    /**
     * Government Taxes
     */
    final double taxes = 0.1;

    /**
     * Function to show Customers info
     * @return Full Info about Customer
     */
    abstract String ShowInfo();

    /**
     * Function to check balance
     * @return double balance
     */
    abstract double getBalance();

    /**
     * Function to check customer name
     * @return Customer name
     */
    abstract String getFullName();

    /**
     * Function to generate check
     */
    abstract void GenerateCheck();
}
