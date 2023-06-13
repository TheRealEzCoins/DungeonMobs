package ezcoins.dungeonmobs.tasks;

import ezcoins.dungeonmobs.DungeonMobs;
import ezcoins.dungeonmobs.mobs.CustomEntity;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HealthBar implements Listener {

    private static List<ArmorStand> activeHealthBar = new ArrayList<>();
    private static List<ArmorStand> activeNameBar = new ArrayList<>();
    private LivingEntity livingEntity;
    private ArmorStand healthStand;
    private ArmorStand nameStand;
    private CustomEntity customEntity;


    public HealthBar(CustomEntity customEntity) {
        this.livingEntity = customEntity.getLivingEntity();
        this.customEntity = customEntity;
        healthStand = livingEntity.getLocation().getWorld().spawn(livingEntity.getLocation(), ArmorStand.class);
        // Set the necessary armor stand properties
        healthStand.setBasePlate(false);
        healthStand.setVisible(false);
        healthStand.setGravity(false);
        healthStand.setSmall(true);
        healthStand.setMarker(true);
        healthStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        healthStand.setMetadata("interactable_armorstand", new FixedMetadataValue(DungeonMobs.plugin, true));
        healthStand.setCustomNameVisible(true);
        healthStand.setCustomName("§c" + livingEntity.getHealth() + "❤");

        nameStand = livingEntity.getLocation().getWorld().spawn(livingEntity.getLocation(), ArmorStand.class);
        nameStand.setBasePlate(false);
        nameStand.setVisible(false);
        nameStand.setGravity(false);
        nameStand.setSmall(true);
        nameStand.setMarker(true);
        nameStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        nameStand.setMetadata("interactable_armorstand", new FixedMetadataValue(DungeonMobs.plugin, true));
        nameStand.setCustomNameVisible(true);
        nameStand.setCustomName(customEntity.getName());

        activeHealthBar.add(healthStand);
        activeNameBar.add(nameStand);

        teleportHealthBar();
        registerListener();
    }

    private void registerListener() {
        DungeonMobs.getPluginManager().registerEvents(new ZombieHitListener(), DungeonMobs.plugin);
    }

    private void teleportHealthBar() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(livingEntity.isDead()) cancel();
                double y = 0.5;
                Location location = livingEntity.getEyeLocation().add(0, y, 0);
                if(livingEntity.getPassenger() != null) {
                    LivingEntity living = (LivingEntity) livingEntity.getPassenger();
                    location = living.getEyeLocation().add(0, y, 0);
                }
                healthStand.teleport(location);
            }
        }.runTaskTimer(DungeonMobs.plugin, 0,  1); // 100 ticks = 5 seconds

        new BukkitRunnable() {
            @Override
            public void run() {
                if(livingEntity.isDead()) cancel();
                double y = 0.75;
                Location location = livingEntity.getEyeLocation().add(0, y, 0);
                if(livingEntity.getPassenger() != null) {
                    LivingEntity living = (LivingEntity) livingEntity.getPassenger();
                    location = living.getEyeLocation().add(0, y, 0);
                }
                nameStand.teleport(location);
            }
        }.runTaskTimer(DungeonMobs.plugin, 0,  1); // 100 ticks = 5 seconds
    }

    public static void removeAllHealthBars() {
        for(ArmorStand activeArmorstand : activeHealthBar) {
            activeArmorstand.remove();
        }

        for(ArmorStand activeArmorstand : activeNameBar) {
            activeArmorstand.remove();
        }

        activeHealthBar.clear();
        activeNameBar.clear();
    }


    private class ZombieHitListener implements Listener {
        @EventHandler
        public void onZombieHit(EntityDamageByEntityEvent event) {
            Entity entity = event.getEntity();
            if (!entity.getPersistentDataContainer().has(new NamespacedKey(DungeonMobs.plugin, "UUID"))) return;
            String UUID = entity.getPersistentDataContainer().get(new NamespacedKey(DungeonMobs.plugin, "UUID"), PersistentDataType.STRING);
            String living = livingEntity.getPersistentDataContainer().get(new NamespacedKey(DungeonMobs.plugin, "UUID"), PersistentDataType.STRING);
            if (UUID.equals(living)) {
                LivingEntity updated = (LivingEntity) entity;
                BigDecimal bigDecimal = new BigDecimal(updated.getHealth() - event.getFinalDamage()).setScale(0, RoundingMode.CEILING);
                healthStand.setCustomName("§c" + bigDecimal + "❤");
                nameStand.setCustomName(customEntity.getName());
            }
        }

        @EventHandler
        public void onZombieHit(EntityDeathEvent event) {
            Entity entity = event.getEntity();
            if (!entity.getPersistentDataContainer().has(new NamespacedKey(DungeonMobs.plugin, "UUID"))) return;
            String UUID = entity.getPersistentDataContainer().get(new NamespacedKey(DungeonMobs.plugin, "UUID"), PersistentDataType.STRING);
            String living = livingEntity.getPersistentDataContainer().get(new NamespacedKey(DungeonMobs.plugin, "UUID"), PersistentDataType.STRING);
            if (UUID.equals(living)) {
                activeHealthBar.remove(healthStand);
                activeNameBar.remove(nameStand);
                nameStand.remove();
                healthStand.remove();
            }
        }
    }

}
