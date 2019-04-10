package sample;

import java.sql.*;

public class Datasource {

    public static final String CONNECTION_STRING = "jdbc:sqlite:/Users/nubaf/Documents/Social Network/src/sample/socialNetwork.db";
    private static Connection connection;

    public static void open() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {

        }
    }
    //Insert,Delete,
    public static void executeQuery(String str) throws SQLException{
        Statement statement=null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(str);
        } catch (SQLException e ) {
            System.out.println(e.getMessage());
        }
        finally {
            if (statement!=null) statement.close();
        }
    }
    public static ResultSet dbExecute(String str){
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt=connection.createStatement();
            rs=stmt.executeQuery(str);
            return rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void loadData() {
        String sqlSearch = "SELECT * FROM users";
        ResultSet rs = dbExecute(sqlSearch);
        try {
            while(rs.next()) {
                String name = rs.getString("name");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String dob = rs.getString("dob");
                String img = rs.getString("img");
             //   String friends = rs.getString("friends");
                boolean tech = rs.getBoolean("tech");
                boolean politics = rs.getBoolean("politics");
                boolean sports = rs.getBoolean("sports");
                boolean entertainment = rs.getBoolean("entertainment");
                String gender = rs.getString("gender");
                String mobile = rs.getString("mobile");
                User user = new User(name,username,password,dob,gender,mobile);
                user.setImg(img);
                user.setInterests(tech,politics,sports,entertainment);
                Accounts.accountList.add(user);
            }
            ResultSet rs2 = dbExecute(sqlSearch);
            int i =0;
            while (rs2.next()) {
                String friends = rs2.getString("friends");
                if(friends!=null)
                    Accounts.accountList.get(i).createList(friends);
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
