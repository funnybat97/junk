package funnybat.junk;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import funnybat.junk.commands.CommandManager;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.yaml.snakeyaml.Yaml;

public final class Junk extends JavaPlugin implements Listener {
    public ItemStack JunkPotion;
    public JunkPotionManager JunkManager = new JunkPotionManager();
    public Boolean JunkEventStatus;
    public DBHandler DB;

    public Team event_team;

    public ScoreboardManager board_manager;

    public Scoreboard board;

    @Override
    public void onEnable() {
        board_manager = Bukkit.getScoreboardManager();
        board = board_manager.getNewScoreboard();
        event_team = board.registerNewTeam("junk_event");
        System.out.println("Start Junk Plugin");
        JunkPotion = JunkManager.initJunkPotion();
        DB = new DBHandler(this);
        DB.conn = DB.creatConnector();
        DB.initAll();
        getServer().getPluginManager().registerEvents(this,this);
        // Plugin startup logic
        getCommand("junk_event").setExecutor(new CommandManager(DB));
        System.out.println("Started Successfully");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                DB.users_reload_debuffs(JunkManager);
                // code here
            }
        }, 0, 20 * 60 * 15);

    }
    public void onDisable(){
        try {
            DB.conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @EventHandler
    public void onEnter(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        player.getInventory().addItem(JunkPotion);

    }
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        System.out.println(item.getItemMeta());
        System.out.println(item.getType());
        System.out.println(player.getName());
        String player_name =player.getName();
        event_team.addPlayer(player);
       if (item.getItemMeta().equals(JunkPotion.getItemMeta())) {
           player.sendMessage("Бахаешь?");

           DB.add_user(player_name);
           DB.add_user_event_stat(player_name);
        }else if(item.getType() == Material.MILK_BUCKET){
           List<PotionEffectType> thirstEffects = JunkManager.thirstEffects;

           new BukkitRunnable() {
               @Override
               public void run() {
                   for (PotionEffectType pe : thirstEffects) player.addPotionEffect(new PotionEffect(pe,60*20,0),true);
                   player.sendMessage("Самый умный?");
               }
           }.runTaskLater(this, 10);
       }
    }
    @EventHandler
    public void onPlayerDrinkMilk(PlayerItemConsumeEvent e) {
        if (e.getItem() == null) return;
        if (e.getItem().getType() != Material.MILK_BUCKET) return;


    }
}
