package src.Classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 *  Class CSWrite which connect to database table
 *      and add info about friends.
 *
 * @author Kudriavtsev Yaroslav
 * @version 1.1
 * @since 2021-30-11
 */
public class DB {

    protected final String FilePath;
    String url;
    Connection connection;

    /**
     * Constructor of Class AutoCsv
     * @param FilePath Path to csv with information about friends
     * @throws SQLException if a database access error occurs or the url is null
     */
    public DB(String FilePath) throws SQLException {

        this.FilePath = FilePath;
        // url to local server
        url = "jdbc:mysql://localhost:3306/bank";

        // Connector to mysql database
        connection = DriverManager.getConnection(url , "root", "Amwey921");

     }


    /**
     * Function CSVtoDB to connect to data table and input info about friends.
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     * @throws IOException Wrong path or missed file
     */
    public void CSVtoDB() throws SQLException, IOException {

        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();

        IsEmptyTable(statement);

        String sql = "INSERT INTO customers(FullName,Balance,Type,Years) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ImportCsvToTable(preparedStatement);
        ClearCopy(statement);
    }

    /**
     * Import data from csv file to table
     * @param preparedStatement An object that represents a precompiled SQL statement.
     * @throws IOException Wrong path or missed file
     * @throws SQLException If a database access error occurs or this method is called on a closed connection
     */
    public void ImportCsvToTable(PreparedStatement preparedStatement) throws IOException, SQLException {
        BufferedReader lineReader = new BufferedReader(new FileReader(FilePath));
        String lineText;
        while ((lineText = lineReader.readLine()) != null) {
            String[] info = lineText.split(",");


            String name = info[0];
            double balance = Double.parseDouble(info[1]);
            String type = info[2];
            int years = Integer.parseInt(info[3]);

            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, balance);
            preparedStatement.setString(3, type);
            preparedStatement.setInt(4, years);
            preparedStatement.addBatch();
        }
        System.out.println("Data has been inserted successfully.");
        preparedStatement.executeBatch();
    }

    /**
     * Function for delete cloned or similar lines in table
     * @param statement The object used for executing a static SQL statement and returning the results it produces
     * @throws SQLException If a database access error occurs or this method is called on a closed connection
     */
    public void ClearCopy(Statement statement) throws SQLException {
        String Clear = "DELETE t1 FROM customers t1 INNER JOIN customers t2 WHERE t1.idCustomers < t2.idCustomers AND t1.FullName = t2.FullName";
        statement.executeUpdate(Clear);
        System.out.println("Copy data cleared");
    }

    /**
     * Check empty table or not
     * If not clearing table
     * @param statement The object used for executing a static SQL statement and returning the results it produces
     * @throws SQLException If a database access error occurs or this method is called on a closed connection
     */
    public void IsEmptyTable(Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * from customers");
        if (rs.next()) {
            String delete = "TRUNCATE TABLE customers";
            statement.executeUpdate(delete);
            System.out.println("Table was not empty. Data was cleared");
        }
    }

    /**
     * Function to find user in table
     * @param name Name of customer for search
     * @return list with information about customer ( empty list if customer not in table)
     * @throws SQLException If a database access error occurs or this method is called on a closed connection
     */
    public ArrayList<String> FindUser(String name) throws SQLException {
        ArrayList<String> list = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT  * FROM customers WHERE FullName = \"%s\"".formatted(name);
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            Collections.addAll(list, resultSet.getString("FullName"),
                    resultSet.getString("Balance"),
                    resultSet.getString("Type"),
                    resultSet.getString("Years"));
        }
        resultSet.close();
        statement.close();
        CloseConnection();
        return list;
    }
    /**
     * Close Connection with MYSQL server
     * @throws SQLException If a database access error occurs or this method is called on a closed connection
     */
    public void CloseConnection() throws SQLException {
        connection.commit();
        connection.close();
    }

}
