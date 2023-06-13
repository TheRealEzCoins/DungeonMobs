package ezcoins.dungeonmobs.utils;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.Random;

public class Utils {
    public static int randomNumBetween(int min, int max) {
        Random rand = new Random();

        return rand.nextInt((max - min) + 1) + min;
    }

    public static ItemStack getSpawnEgg(EntityType entityType) {
        ItemStack spawnEgg = new ItemStack(Material.WOLF_SPAWN_EGG);
        ItemMeta itemMeta = spawnEgg.getItemMeta();

        if (itemMeta instanceof SpawnEggMeta) {
            SpawnEggMeta spawnEggMeta = (SpawnEggMeta) itemMeta;
            spawnEggMeta.setDisplayName(entityType.getName() + " Spawn Egg");
            spawnEggMeta.setLore(null);
            spawnEgg.setItemMeta(spawnEggMeta);
        }

        return spawnEgg;
    }
}
