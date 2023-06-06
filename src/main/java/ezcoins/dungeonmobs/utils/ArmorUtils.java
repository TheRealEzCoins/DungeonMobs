package ezcoins.dungeonmobs.utils;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ArmorUtils {

    public static ItemStack colorArmorPiece(ItemStack itemStack, Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        leatherArmorMeta.setColor(color);
        itemStack.setItemMeta(leatherArmorMeta);
        return itemStack;
    }
}
