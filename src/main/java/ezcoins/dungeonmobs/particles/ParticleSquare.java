package ezcoins.dungeonmobs.particles;

import ezcoins.dungeonmobs.DungeonMobs;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ParticleSquare extends BukkitRunnable {
    private final Player player;
    private final int squareSize;
    private final double particleDensity;
    private final int durationTicks;
    private int elapsedTicks;

    public ParticleSquare(Player player, int squareSize, double particleDensity, int durationSeconds) {
        this.player = player;
        this.squareSize = squareSize;
        this.particleDensity = particleDensity;
        this.durationTicks = durationSeconds * 20; // Convert seconds to ticks
        this.elapsedTicks = 0;
    }

    @Override
    public void run() {
        if (elapsedTicks >= durationTicks) {
            this.cancel();
            return;
        }

        Location headLocation = player.getEyeLocation();

        // Calculate the corner locations of the square
        Location corner1 = headLocation.clone().add(-squareSize, 0, squareSize);
        Location corner2 = headLocation.clone().add(squareSize, 0, squareSize);
        Location corner3 = headLocation.clone().add(squareSize, 0, -squareSize);
        Location corner4 = headLocation.clone().add(-squareSize, 0, -squareSize);

        // Spawn particles along the edges of the square
        spawnLineParticles(corner1, corner2);
        spawnLineParticles(corner2, corner3);
        spawnLineParticles(corner3, corner4);
        spawnLineParticles(corner4, corner1);

        elapsedTicks++;
    }

    // Helper method to spawn particles along a line between two locations
    private void spawnLineParticles(Location start, Location end) {
        double distance = start.distance(end);
        double particles = distance * particleDensity;

        Vector direction = end.clone().subtract(start).toVector().normalize();
        double interval = distance / particles;

        for (double i = 0; i <= distance; i += interval) {
            Location particleLocation = start.clone().add(direction.clone().multiply(i));
            particleLocation.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, particleLocation, 1);
        }
    }

    public void start() {
        this.runTaskTimer(DungeonMobs.plugin, 0, 1); // Run the task every tick
    }
}