package ezcoins.dungeonmobs.mobs;

import com.google.common.io.MoreFiles;
import dev.dbassett.skullcreator.SkullCreator;
import ezcoins.dungeonmobs.DungeonMobs;
import ezcoins.dungeonmobs.particles.ParticleCircle;
import ezcoins.dungeonmobs.particles.ParticleCircleUpwards;
import ezcoins.dungeonmobs.particles.ParticleCube;
import ezcoins.dungeonmobs.utils.ArmorUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ZombieMob {

    public ZombieMob() {

    }

    public void spawnMob(Location location) {

        Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.setShouldBurnInDay(false);
        zombie.setAI(false);
        zombie.setAdult();
        AttributeInstance knockbackResistance = zombie.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        AttributeInstance speed = zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (knockbackResistance != null) {
            knockbackResistance.setBaseValue(1.0);
        }
        if (speed != null) {
            speed.setBaseValue(0.5);
        }
        ParticleCircleUpwards particleCircleUpwards = new ParticleCircleUpwards(zombie, 2, 1, 4);
        particleCircleUpwards.start();
        Location square = zombie.getLocation();
        square.add(0, 1, 0);
        ParticleCube particleSquare = new ParticleCube(square, 1, 5, 4);
        particleSquare.start();
        ItemStack[] armorContents = new ItemStack[] {
                new ItemStack(Material.DIAMOND_BOOTS),
                ArmorUtils.colorArmorPiece(new ItemStack(Material.LEATHER_LEGGINGS), Color.BLACK),
                ArmorUtils.colorArmorPiece(new ItemStack(Material.LEATHER_CHESTPLATE), Color.BLACK),
                SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTkzMzQ4NTMzNWZjOWMzOGFiMzJlNmJkMmNkNDgwYzNiYWY2MWIwZTNmNmJjYTYxZTBlM2NmMWY3NzQ3YzlkYSJ9fX0=")
        };

        new BukkitRunnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                if (currentIndex < armorContents.length) {
                    ItemStack armorPiece = armorContents[currentIndex];
                    switch (currentIndex) {
                        case 0:
                            zombie.getEquipment().setBoots(armorPiece);
                            break;
                        case 1:
                            zombie.getEquipment().setLeggings(armorPiece);
                            break;
                        case 2:
                            zombie.getEquipment().setChestplate(armorPiece);
                            break;
                        case 3:
                            zombie.getEquipment().setHelmet(armorPiece);
                            break;
                    }
                    currentIndex++;
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(DungeonMobs.plugin, 20L, 20L);
        zombie.getEquipment().setItemInMainHand(new ItemStack(Material.GOLDEN_HOE));
        new BukkitRunnable() {
            @Override
            public void run() {
                if (zombie.isValid()) {
                    zombie.setSilent(false);
                    zombie.setAI(true); // Disable the zombie's AI
                    cancel(); // Cancel the timer
                }
            }
        }.runTaskLater(DungeonMobs.plugin, 4 * 20);
    }
}
