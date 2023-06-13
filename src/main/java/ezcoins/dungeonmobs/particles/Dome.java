package ezcoins.dungeonmobs.particles;

import ezcoins.dungeonmobs.DungeonMobs;
import ezcoins.dungeonmobs.abilities.PetrifyingGaze;
import ezcoins.dungeonmobs.mobs.CustomEntity;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public class Dome {

    private Location center;
    private long durationSeconds;
    private CustomEntity customEntity;
    public Dome(CustomEntity customEntity, long durationSeconds) {
        this.durationSeconds = durationSeconds;
        this.customEntity = customEntity;
        this.center = customEntity.getLivingEntity().getLocation();
    }

    public void createDelayedEvent(long startInSeconds, long delayInSeconds) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(customEntity.getLivingEntity().isDead()) cancel();
                Dome dome = new Dome(customEntity, durationSeconds);
                dome.createDomeParticles(customEntity.getLivingEntity().getWidth());
            }
        }.runTaskTimer(DungeonMobs.plugin, startInSeconds * 20, delayInSeconds * 20); // 100 ticks = 5 seconds
    }
    private void createDomeParticles(double radius) {
        new BukkitRunnable() {
            private int timePassed = 0;

            @Override
            public void run() {
                if (timePassed >= durationSeconds * 20) {
                    // Cancel the task after the specified duration
                    cancel();
                    return;
                }

                double y = center.getY() + radius;
                double theta = 0;
                while (theta <= 2 * Math.PI) {
                    double x = center.getX() + radius * Math.cos(theta);
                    double z = center.getZ() + radius * Math.sin(theta);
                    Location particleLocation = new Location(center.getWorld(), x, y, z);
                    center.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, particleLocation, 1);

                    theta += Math.PI / 60;
                }

                timePassed++;
            }
        }.runTaskTimer(DungeonMobs.plugin, 0, 1); // Run the task every tick
    }

}
