package ezcoins.dungeonmobs.tasks;

import dev.dbassett.skullcreator.SkullCreator;
import ezcoins.dungeonmobs.DungeonMobs;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HealthBar implements Listener {

    private LivingEntity livingEntity;
    private ArmorStand armorStand;


    public HealthBar(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
        armorStand = livingEntity.getLocation().getWorld().spawn(livingEntity.getLocation(), ArmorStand.class);
        // Set the necessary armor stand properties
        armorStand.setBasePlate(false);
        armorStand.setVisible(false);
        armorStand.setSmall(true);
        armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        armorStand.setMetadata("interactable_armorstand", new FixedMetadataValue(DungeonMobs.plugin, true));
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName("Health: " + livingEntity.getHealth() + "/" + livingEntity.getMaxHealth());

        livingEntity.addPassenger(armorStand);

        registerListener();
    }

    private void registerListener() {
        DungeonMobs.getPluginManager().registerEvents(new ZombieHitListener(), DungeonMobs.plugin);
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
                armorStand.setCustomName("Health: " + bigDecimal + "/" + updated.getMaxHealth());
            }
        }

        @EventHandler
        public void onZombieHit(EntityDeathEvent event) {
            Entity entity = event.getEntity();
            if (!entity.getPersistentDataContainer().has(new NamespacedKey(DungeonMobs.plugin, "UUID"))) return;
            String UUID = entity.getPersistentDataContainer().get(new NamespacedKey(DungeonMobs.plugin, "UUID"), PersistentDataType.STRING);
            String living = livingEntity.getPersistentDataContainer().get(new NamespacedKey(DungeonMobs.plugin, "UUID"), PersistentDataType.STRING);
            if (UUID.equals(living)) {
                armorStand.remove();
            }
        }
    }

}
