package funnybat.junk;

import java.sql.*;
import java.util.Map;

public class DBHandler {
    public Boolean DBStatus = true;
    public Map<String, Object> conf;
    public static String dbPath = "jdbc:sqlite:./plugins/Junk/JUNK.db";

    public static void create_events_table(){
        // SQL statement for creating a new table
        String sql =
               " create table if not exists events("+
               " id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "event_name text not null UNIQUE,"+
                "start_date timestamp,"+
                "end_date timestamp,"+
                "debuff_start integer DEFAULT 1"+
         " );";


        try{
            Connection conn = DriverManager.getConnection(dbPath);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
    public static void create_users_table(){
        // SQL statement for creating a new table
        String sql =
               " create table if not exists users("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "name text not null UNIQUE,"+
                    "uid text"+
                ");";


        try{
            Connection conn = DriverManager.getConnection(dbPath);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
    public static void create_users_user_in_event_stat(){
        // SQL statement for creating a new table
        String sql =
                " create table if not exists users("+
                        "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        "name text not null UNIQUE,"+
                        "uid text"+
                        ");";


        try{
            Connection conn = DriverManager.getConnection(dbPath);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
    public void createNewDatabase() {
        System.out.println("createNewDatabase");

        try {
            Connection conn = DriverManager.getConnection(dbPath);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void main(String[] args) {

    }

}
