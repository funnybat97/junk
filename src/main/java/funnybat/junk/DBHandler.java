package funnybat.junk;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DBHandler {


    public Boolean DBStatus = true;
    public Map<String, Object> conf;
    public static String dbPath = "jdbc:sqlite:./plugins/Junk/JUNK.db";
    public static Connection conn;

    private static JavaPlugin plugin;


    public DBHandler(JavaPlugin junk_plugin){
        plugin = junk_plugin;
    }

    public static void run_sql(String sql, String error_message){

        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                try{

                    Statement stmt = conn.createStatement();
                    stmt.execute(sql);

                } catch (SQLException e) {
                    System.out.println(error_message);
                    System.out.println(e.getMessage());
                }
            }
        });
    }
    public static Statement get_statement(){
        try{
            Statement stmt = conn.createStatement();
            return stmt;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static Integer get_current_event() throws SQLException {
        String sql =
                "select id from events where end_date ='3000-01-01' " +
                        "order by 1 desc limit 1;";

        Statement statement = get_statement();

        ResultSet rs = statement.executeQuery(sql);
        return rs.getInt("id");
    }
    public static Integer get_current_event_active() throws SQLException {
        String sql =
                "select id from events where end_date ='3000-01-01' and start_date is not null " +
                        "order by 1 desc limit 1;";

        Statement statement = get_statement();

        ResultSet rs = statement.executeQuery(sql);
        return rs.getInt("id");
    }
    public static Integer get_user_id(String name) throws SQLException {
        String sql =
                "select id from users where name ='"+name+"';";

        Statement statement = get_statement();
        ResultSet rs = statement.executeQuery(sql);
        return rs.getInt("id");
    }
    public static ArrayList<String> get_users_name_by_id(ArrayList<Integer> users_ids_list) throws SQLException {
        String users_str= users_ids_list.stream().map(Object::toString)
                .collect(Collectors.joining(", "));
        String sql =
                "select name from users where id in ("+users_str+");";

        Statement statement = get_statement();
        ResultSet rs = statement.executeQuery(sql);
        ArrayList<String> user_names = new ArrayList<String>();
        while (rs.next()) {
            user_names.add(rs.getString("name"));
        }
        return user_names;

    }
    public static Integer get_user_event_stat(Integer event_id, Integer user_id) throws SQLException {
        String sql =
                "select id from user_in_event_stat where user_id ="+user_id +
                        " and event_id ="+event_id+" ;";

        Statement statement = get_statement();
        ResultSet rs = statement.executeQuery(sql);
        return rs.getInt("id");
    }
    public static ResultSet get_users_event_stat(Integer event_id) throws SQLException {
        String sql =
                "select * from user_in_event_stat where "+
                        "event_id ="+event_id+" ;";

        Statement statement = get_statement();
        ResultSet rs = statement.executeQuery(sql);
        return rs;
    }
    public static void users_update_status(String users_names){
        String sql =
                "update user_in_event_stat " +
                        "set last_consume_time = CURRENT_TIMESTAMP," +
                        "consume_count = consume_count + 1 "+
                        "where user_id in (' + users_names + ');";
        run_sql(sql,"Error during event stop");
    }
    public static void users_reload_debuffs(JunkPotionManager JunkManager)  {

        try {
            List<PotionEffectType> thirstEffects = JunkManager.thirstEffects;
            Integer event_id = get_current_event_active();
            ResultSet rs = get_users_event_stat(event_id);
            ArrayList<Integer> users_to_update = new ArrayList<Integer>();
            LocalTime now = LocalTime.now();
            while (rs.next()){
                System.out.println(rs.getTime("last_consume_time"));
                LocalTime time_to_debuff = rs.getTime("last_consume_time").toLocalTime();
                time_to_debuff.plusMinutes(15);
                if (now.isAfter(time_to_debuff)){
                    users_to_update.add(rs.getInt("user_id"));


                }
            }
            ArrayList<String> user_names_array = get_users_name_by_id(users_to_update);
            String user_names = user_names_array.stream().map(Object::toString)
                    .collect(Collectors.joining(", "));
            System.out.println(user_names);
            users_update_status(user_names);
            for(int i =0;i<user_names_array.size();i++){
                Player player =Bukkit.getPlayer(user_names_array.get(i));
                for (PotionEffectType pe : thirstEffects) player.addPotionEffect(new PotionEffect(pe,60*20,0),true);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    public static void add_user_event_stat(String name){

        try{
            Integer event_id = get_current_event();
            System.out.println(event_id);
            Integer user_id = get_user_id(name);
            System.out.println(user_id);
            //Integer user_stat_id = get_user_event_stat(event_id,user_id);
            //System.out.println(user_stat_id);
            String sql = "insert into user_in_event_stat" +
                    "(event_id,user_id, consume_count,status)" +
                    "values" +
                    "(" +
                    event_id+","+user_id+",1,'buffs active'"+
                    ")";
            System.out.println(sql);
            run_sql(sql,"Error during user_stat creation");


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }



    }

    public static void add_user(String name){
        String sql =
                "INSERT INTO users (name)" +
                "VALUES('" + name + "');";
        run_sql(sql,"Error during user creation");
    }

    public static void start_event(String name){

        String sql =
                "update events " +
                        "set start_date = CURRENT_TIMESTAMP, "+
                        "end_date = '3000-01-01' "+
                "where event_name = '" + name + "';";
        run_sql(sql,"Error during event start");
    }

    public static void stop_event(String name){

        String sql =
                "update events " +
                        "set end_date = CURRENT_TIMESTAMP "+
                        "where event_name = '" + name + "'";
        run_sql(sql,"Error during event stop");
    }
    public static void create_event(String name){
        String sql =
                "INSERT INTO events (event_name)" +
                        "VALUES('"+name+"');";
        run_sql(sql,"Error during event creation");
    }
    public static void create_events_table(){
        // SQL statement for creating a new table
        String sql =
               " create table if not exists events("+
               " id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "event_name text not null UNIQUE,"+
                "start_date timestamp,"+
                "end_date timestamp UNIQUE default '3000-01-01',"+
                "debuff_start integer DEFAULT 1"+
         " );";


        try{
            Statement stmt = conn.createStatement();
            System.out.println("events table was created");
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
            Statement stmt = conn.createStatement();
            System.out.println("users table was created");
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
    public static void create_users_user_in_event_stat(){
        // SQL statement for creating a new table
        String sql =
                "create table if not exists user_in_event_stat("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "user_id integer not null,"+
                "event_id integer not NULL,"+
                "consume_count integer not NULL,"+
                "status text not NULL,"+
                "last_consume_time timestamp DEFAULT CURRENT_TIMESTAMP,"+
                "first_consume_time timestamp DEFAULT CURRENT_TIMESTAMP,"+
                "FOREIGN KEY(event_id) REFERENCES events(id),"+
                "FOREIGN KEY(user_id) REFERENCES users(id)," +
                "UNIQUE(user_id,event_id)"+
        ");";


        try{
            Statement stmt = conn.createStatement();
            System.out.println("create_users_user_in_event_stat was created");
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }


    public void createNewDatabase() {
        System.out.println("createNewDatabase");

        try {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void initAll(){
        createNewDatabase();
        create_events_table();
        create_users_table();
        create_users_user_in_event_stat();
    }
    public Connection creatConnector(){
        try {
            return DriverManager.getConnection(dbPath);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void main() {

    }

}
