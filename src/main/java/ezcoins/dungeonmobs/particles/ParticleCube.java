package ezcoins.dungeonmobs.particles;

import ezcoins.dungeonmobs.DungeonMobs;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleCube extends BukkitRunnable {
    private final Location location;
    private final int cubeSize;
    private final double particleDensity;
    private final int durationTicks;
    private int elapsedTicks;

    public ParticleCube(Player player, int cubeSize, double particleDensity, int durationSeconds) {
        this.location = player.getLocation();
        this.cubeSize = cubeSize;
        this.particleDensity = particleDensity;
        this.durationTicks = durationSeconds * 20; // Convert seconds to ticks
        this.elapsedTicks = 0;
    }

    public ParticleCube(Location location, int cubeSize, double particleDensity, int durationSeconds) {
        this.location = location;
        this.cubeSize = cubeSize;
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

        Location centerLocation = location;

        // Calculate the corner locations of the cube
        Location corner1 = centerLocation.clone().add(-cubeSize, cubeSize, -cubeSize);
        Location corner2 = centerLocation.clone().add(cubeSize, cubeSize, -cubeSize);
        Location corner3 = centerLocation.clone().add(cubeSize, -cubeSize, -cubeSize);
        Location corner4 = centerLocation.clone().add(-cubeSize, -cubeSize, -cubeSize);
        Location corner5 = centerLocation.clone().add(-cubeSize, cubeSize, cubeSize);
        Location corner6 = centerLocation.clone().add(cubeSize, cubeSize, cubeSize);
        Location corner7 = centerLocation.clone().add(cubeSize, -cubeSize, cubeSize);
        Location corner8 = centerLocation.clone().add(-cubeSize, -cubeSize, cubeSize);

        // Spawn particles along the edges of the cube
        spawnLineParticles(corner1, corner2);
        spawnLineParticles(corner2, corner3);
        spawnLineParticles(corner3, corner4);
        spawnLineParticles(corner4, corner1);

        spawnLineParticles(corner5, corner6);
        spawnLineParticles(corner6, corner7);
        spawnLineParticles(corner7, corner8);
        spawnLineParticles(corner8, corner5);

        spawnLineParticles(corner1, corner5);
        spawnLineParticles(corner2, corner6);
        spawnLineParticles(corner3, corner7);
        spawnLineParticles(corner4, corner8);

        elapsedTicks++;
    }

    // Helper method to spawn particles along a line between two locations
    private void spawnLineParticles(Location start, Location end) {
        double distance = start.distance(end);
        double particles = distance * particleDensity;

        double interval = distance / particles;

        Location currentLocation = start.clone();

        for (double i = 0; i <= distance; i += interval) {
            Location particleLocation = currentLocation.clone();
            particleLocation.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, particleLocation, 1);
            currentLocation.add(end.clone().subtract(start).multiply(interval / distance));
        }
    }

    public void start() {
        this.runTaskTimer(DungeonMobs.plugin, 0, 1); // Run the task every tick
    }
}