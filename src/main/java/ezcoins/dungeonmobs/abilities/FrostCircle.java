package ezcoins.dungeonmobs.abilities;

import ezcoins.dungeonmobs.DungeonMobs;
import ezcoins.dungeonmobs.mobs.CustomEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class FrostCircle implements Ability {
    private static Map<Location, Material> originalBlocks = new HashMap<>();

    private int radius;
    private Location center;
    private int durationInSeconds;
    private CustomEntity customEntity;
    public FrostCircle(int radius, int durationSeconds ,CustomEntity customEntity) {
        this.radius = radius;
        this.durationInSeconds = durationSeconds * 20;
        this.center = customEntity.getLivingEntity().getLocation().add(0, -1, 0);
        this.customEntity = customEntity;
    }

    @Override
    public void startEvent(long startInSeconds, long delayInSeconds) {

        new BukkitRunnable() {
            @Override
            public void run() {
                if (customEntity.getLivingEntity().isValid()) {
                    FrostCircle frostCircle = new FrostCircle(radius, durationInSeconds, customEntity);
                    frostCircle.createEvent();
                }
            }
        }.runTaskTimer(DungeonMobs.plugin, startInSeconds * 20, delayInSeconds * 20);

    }

    @Override
    public void createEvent() {
        World world = customEntity.getLivingEntity().getWorld();
        customEntity.getLivingEntity().getWorld().playSound(customEntity.getLivingEntity().getLocation(), Sound.BLOCK_GLASS_PLACE, 1f, 1f);
        BukkitRunnable createCircleTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (x * x + z * z <= radius * radius) {
                            Location blockLocation = center.clone().add(x, 0, z);
                            Block block = world.getBlockAt(blockLocation);
                            originalBlocks.put(blockLocation, block.getType());
                            block.setType(Material.PACKED_ICE);
                        }
                    }
                }
            }
        };

        BukkitRunnable removeCircleTask = new BukkitRunnable() {
            @Override
            public void run() {
                customEntity.getLivingEntity().getWorld().playSound(customEntity.getLivingEntity().getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 1f);
                forceClean();
            }
        };

        int delay = 20; // 1-second delay (20 ticks)
        int duration = 3 * 20; // 5 seconds duration (100 ticks)

        createCircleTask.runTaskLater(DungeonMobs.plugin, delay);
        removeCircleTask.runTaskLater(DungeonMobs.plugin, delay + duration);
    }

    public static void forceClean() {
        for (Map.Entry<Location, Material> entry : originalBlocks.entrySet()) {
            Location blockLocation = entry.getKey();
            Material originalType = entry.getValue();
            Block block = blockLocation.getWorld().getBlockAt(blockLocation);
            block.setType(originalType);
        }
        originalBlocks.clear();
    }
}
