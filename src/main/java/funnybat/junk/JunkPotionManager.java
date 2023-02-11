package funnybat.junk;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionType;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JunkPotionManager {

    public List<PotionEffectType> thirstEffects = new ArrayList<>(Arrays.asList(PotionEffectType.SLOW, PotionEffectType.CONFUSION, PotionEffectType.POISON));
    public static Integer durationByEffectType(PotionEffectType type){
        if (type.equals(PotionEffectType.SPEED)) {

            return 60;
        } else if (type.equals(PotionEffectType.SATURATION)){

            return 60;
        } else if (type.equals(PotionEffectType.FAST_DIGGING)){

            return 60;
        } else if (type.equals(PotionEffectType.SLOW)){

            return 180;
        } else if (type.equals(PotionEffectType.CONFUSION)){

            return 180;

        } else if (type.equals(PotionEffectType.SATURATION)){

            return 180;

        }
        return 60;
    }
    public static PotionEffect buff_speed = new PotionEffect(PotionEffectType.SATURATION, 5, 1);

    public static void initRecipe(ItemStack JunkPotion){
        ShapedRecipe recipe = new ShapedRecipe(JunkPotion);
        recipe.shape("IG", "GB");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('G', Material.GOLD_INGOT);
        recipe.setIngredient('B', Material.GLASS_BOTTLE);
        Bukkit.addRecipe(recipe);
    }


    public static ItemStack initJunkPotion(){

        ItemStack JunkPotion = new Potion(PotionType.SPEED).toItemStack(1);
        PotionMeta meta = (PotionMeta) JunkPotion.getItemMeta();
        meta.setDisplayName("Турбач");
        meta.addCustomEffect(buff_speed, true);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 60*20, 1), true);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SATURATION, 60*20, 1), true);

        System.out.println("Plugin is started!");
        JunkPotion.setItemMeta(meta);
        JunkPotionManager.initRecipe(JunkPotion);
        return JunkPotion;
    }
}
