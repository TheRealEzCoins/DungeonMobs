package ezcoins.dungeonmobs.tasks;

import dev.dbassett.skullcreator.SkullCreator;
import ezcoins.dungeonmobs.DungeonMobs;
import io.papermc.paper.math.Rotations;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pose;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

public class RotationSkullTask {

    @Getter
    private ArmorStand armorStand;
    private static BukkitRunnable animationTask;

    private final LivingEntity livingEntity;
    private Location location;
    public RotationSkullTask(LivingEntity livingEntity) {;
        this.livingEntity = livingEntity;
        this.location = livingEntity.getLocation();
        location.add(0, 1, 0);
        armorStand = location.getWorld().spawn(location, ArmorStand.class);

        // Set the necessary armor stand properties
        armorStand.setBasePlate(false);
        armorStand.setVisible(false);
        armorStand.setSmall(true);
        armorStand.setItem(EquipmentSlot.HEAD, SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjI0MWNjM2NjODM1YjczNGNlMmMxNDg3YWE1NjVhNzc2YjdiNTAyN2QzMmEyN2EwMTNjMGViNjUwMjE2MjQwNSJ9fX0="));
        armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        armorStand.setMetadata("interactable_armorstand", new FixedMetadataValue(DungeonMobs.plugin, true));

        livingEntity.addPassenger(armorStand);
    }
    public void startAnimationTask() {
        animationTask = new BukkitRunnable() {
            private int time = 2000;

            @Override
            public void run() {



                time--;
                // Stop the animation after 10 seconds
                if (time == 0) { // Approximately 10 seconds (20 ticks per second)
                    armorStand.remove();
                    stopRotationTask();
                }

            }
        };

        // Schedule the animation task to run every tick
        animationTask.runTaskTimer(DungeonMobs.plugin, 0L, 1L);
    }

    private static void stopRotationTask() {
        // Cancel the rotation task
        if (animationTask != null) {
            animationTask.cancel();
            animationTask = null;
        }
    }

}
