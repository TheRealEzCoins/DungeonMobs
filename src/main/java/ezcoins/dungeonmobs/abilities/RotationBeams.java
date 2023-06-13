package ezcoins.dungeonmobs.abilities;

import ezcoins.dungeonmobs.DungeonMobs;
import ezcoins.dungeonmobs.mobs.CustomEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RotationBeams implements Ability {
    private CustomEntity customEntity;
    public RotationBeams(CustomEntity customEntity) {
        this.customEntity = customEntity;
    }
    @Override
    public void startEvent(long startInSeconds, long delayInSeconds) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (customEntity.getLivingEntity().isValid()) {
                    RotationBeams rotationBeams = new RotationBeams(customEntity);
                    rotationBeams.createEvent();
                }
            }
        }.runTaskTimer(DungeonMobs.plugin, startInSeconds * 20, delayInSeconds * 20);
    }

    @Override
    public void createEvent() {
        new BukkitRunnable() {
            @Override
            public void run() {
                customEntity.getLivingEntity().setAI(false);

                // Spawn lasers
                Location loc = customEntity.getLivingEntity().getEyeLocation();
                spawnLasers(loc);

                // Stop lasers after 3 seconds
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        stopLasers(loc);
                        customEntity.getLivingEntity().setAI(true);
                    }
                }.runTaskLater(DungeonMobs.plugin, 100); // 3 seconds (20 ticks per second)

            }
        }.runTaskLater(DungeonMobs.plugin, 200L); // 10 seconds (20 ticks per second)
    }

    private void spawnLasers(Location loc) {
        int length = 5; // Length of each laser line

        // Spawn lasers as straight lines
        for (int i = 1; i <= length; i++) {
            loc.add(0, i, 0);
            spawnParticleLine(loc);
            loc.subtract(0, i, 0);
        }
        for (int i = 1; i <= length; i++) {
            loc.add(0, -i, 0);
            spawnParticleLine(loc);
            loc.subtract(0, -i, 0);
        }
        for (int i = 1; i <= length; i++) {
            loc.add(i, 0, 0);
            spawnParticleLine(loc);
            loc.subtract(i, 0, 0);
        }
        for (int i = 1; i <= length; i++) {
            loc.add(-i, 0, 0);
            spawnParticleLine(loc);
            loc.subtract(-i, 0, 0);
        }
    }

    private void spawnParticleLine(Location loc) {
        double radius = 4; // Distance between particles in the line

        // Spawn particles in a line
        for (double x = -radius; x <= radius; x += 0.1) {
            double y = loc.getY();
            double z = loc.getZ() + x;
            Location particleLoc = new Location(loc.getWorld(), loc.getX(), y, z);
            particleLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, particleLoc, 1);

            double x2 = loc.getX() + x;
            double y2 = loc.getY();
            double z2 = loc.getZ();
            Location particleLoc2 = new Location(loc.getWorld(), x2, y2, z2);
            particleLoc2.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, particleLoc2, 1);
        }
    }

    private void stopLasers(Location loc) {
        int length = 5; // Length of each laser line

        // Remove lasers by changing the block type
        for (int i = 1; i <= length; i++) {
            loc.add(0, i, 0);
            loc.subtract(0, i, 0);
        }
        for (int i = 1; i <= length; i++) {
            loc.add(0, -i, 0);
            loc.subtract(0, -i, 0);
        }
    }

}
