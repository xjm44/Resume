package database;

import java.sql.*;
// adapted from https://github.com/xerial/sqlite-jdbc
public class Database {

    /**
     * <b>Preconditions:</b><br>
     *     a database named mydatabase
     * <br>
     * <b>Postconditions:</b><br>
     *     data is inserted.
     * <br>
     *
     * @param category categories in the database. list = list of cryptocurrencies. one = detail of a cryptocurrency. converse = conversion between two cryptocurrencies.
     * @param data data to be inserted.
     * @return if inserted successfully.
     * @throws IllegalArgumentException If any of the other preconditions are violated.
     */
    public boolean insertData(String category, String data) throws IllegalArgumentException{
        // eg. db.insertData("one","test one");
        // eg. db.insertData("list","test list");
        // eg. db.insertData("converse","test convo");
        try{
            return insert(category, data);
        }catch(Exception e){
            return false;
        }

    }

    /**
     * <b>Preconditions:</b><br>
     *     a database named mydatabase
     * <br>
     * <b>Postconditions:</b><br>
     *     output of a category. <br>
     *     list = list of cryptocurrencies. one = detail of a cryptocurrency. converse = conversion between two cryptocurrencies.
     * <br>
     *
     * @param category categories in the database. list = list of cryptocurrencies. one = detail of a cryptocurrency. converse = conversion between two cryptocurrencies.
     * @return output of a category.
     * @throws IllegalArgumentException If any of the other preconditions are violated.
     */
    public String getDetail(String category)throws IllegalArgumentException{
        //eg. db.getDetail("one"));
        //eg. db.getDetail("list");
        //eg. db.getDetail("converse");
        try{
            return obtain(category);
        }catch(Exception e){
            return "failed getting database/ out of memory";
        }
    }
    /**
     * <b>Preconditions:</b><br>
     *     a database named mydatabase
     * <br>
     * <b>Postconditions:</b><br>
     *     clear/empty the database
     * <br>
     */
    public void clear(){
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/mydatabase.db")) {
            // create a database connection

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists cryptocurrency");
            statement.executeUpdate("create table cryptocurrency (category string, data string)");


        } catch (SQLException e) {
            System.out.println(e);
        }
    }



    // insert into db
    private boolean insert(String category,String data){
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/mydatabase.db")) {
            // create a database connection

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate("insert into cryptocurrency values('" + category + "', " + "'" + data + "'" + ")");
            return true;

        } catch (Exception e) {
            return false;
        }
    }
    // load from db
    private String obtain(String category){

        Connection connection = null;
        String output ="";
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/mydatabase.db");

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.


            ResultSet result = statement.executeQuery("select * from cryptocurrency where category is '"+category+"'");

            while(result.next())
            {
                //"" + result.getString("category") + "\n" +
                String temp = "" + result.getString("data") +"\n";
                output += temp + "\n";
            }
            return output;
        }
        catch(Exception e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.out.println(e.getMessage());
            return "failed getting database/ out of memory";
        }
        finally {
            try {
                if(connection != null)
                    connection.close();
            }
            catch(Exception e) {
                // connection close failed.
                return "connection close failed";
            }
        }
    }


}

