package funnybat.junk;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
import org.yaml.snakeyaml.Yaml;

public final class Junk extends JavaPlugin implements Listener {
    public ItemStack JunkPotion;
    public JunkPotionManager JunkPotionManager;
    public Boolean JunkEventStatus;
    public DBHandler DB;
    @Override
    public void onEnable() {
        System.out.println("Start Junk Plugin");
        // Plugin startup logic
        JunkPotion = JunkPotionManager.initJunkPotion();
        DB = new DBHandler();
        DB.createNewDatabase();
        getServer().getPluginManager().registerEvents(this,this);
        System.out.println("Started Successfully");


    }
    @EventHandler
    public void onEnter(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.getInventory().addItem(JunkPotion);
    }
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        System.out.println(item.getType());
       if (item.getItemMeta().equals(JunkPotion.getItemMeta())) {
           player.sendMessage("Бахаешь?");

        }
    }
    @EventHandler
    public void onEntityPotionEffectEvent(EntityPotionEffectEvent event){
        PotionEffect buff_speed = new PotionEffect(PotionEffectType.LUCK, 5, 1);
        Entity ent = event.getEntity();
       // System.out.println(buff_speed);
        System.out.println(event.getOldEffect().equals(JunkPotionManager.buff_speed));
        System.out.println(event.getOldEffect().getType().equals(PotionEffectType.LUCK));

       if(event.getOldEffect().getType().equals(PotionEffectType.LUCK)){
            ent.sendMessage(String.valueOf(event.getEntityType()));

        }

    }
}
