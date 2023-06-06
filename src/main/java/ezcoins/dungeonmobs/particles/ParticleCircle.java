package ezcoins.dungeonmobs.particles;

import ezcoins.dungeonmobs.DungeonMobs;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleCircle extends BukkitRunnable {
    private final Location location;
    private double radius;
    private double maxRadius;
    private final int durationTicks;
    private final Particle particle;

    public ParticleCircle(Player player, double maxRadius, int durationSeconds) {
        this.location = player.getLocation();
        this.maxRadius = maxRadius;
        this.durationTicks = durationSeconds * 20; // Convert seconds to ticks (assuming 20 ticks per second)
        this.radius = 0.0;
        this.particle = Particle.CLOUD;
    }

    public ParticleCircle(Location location, double maxRadius, int durationSeconds) {
        this.location = location;
        this.maxRadius = maxRadius;
        this.durationTicks = durationSeconds * 20; // Convert seconds to ticks (assuming 20 ticks per second)
        this.radius = 0.0;
        this.particle = Particle.CLOUD;
    }

    public ParticleCircle(Location location, Particle particle, double maxRadius, int durationSeconds) {
        this.location = location;
        this.maxRadius = maxRadius;
        this.durationTicks = durationSeconds * 20; // Convert seconds to ticks (assuming 20 ticks per second)
        this.radius = 0.0;
        this.particle = particle;
    }

    @Override
    public void run() {
        if (radius > maxRadius) {
            this.cancel();
            return;
        }

        Location center = location;
        for (double angle = 0; angle < 360; angle += 5) {
            double radians = Math.toRadians(angle);
            double x = center.getX() + (radius * Math.cos(radians));
            double y = center.getY();
            double z = center.getZ() + (radius * Math.sin(radians));
            Location particleLocation = new Location(center.getWorld(), x, y, z);
            center.getWorld().spawnParticle(this.particle, particleLocation, 1);
        }

        radius += (maxRadius / durationTicks) * 3; // Increase radius by 3 blocks per second
    }

    public void start() {
        this.runTaskTimer(DungeonMobs.plugin, 0, 1); // Run the task every tick
    }
}
