package ezcoins.dungeonmobs.particles;

import ezcoins.dungeonmobs.DungeonMobs;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleCircleUpwards extends BukkitRunnable {
    private final LivingEntity livingEntity;
    private final double height;
    private final double radius;
    private final int durationTicks;
    private int elapsedTicks;

    public ParticleCircleUpwards(LivingEntity livingEntity, double height, double radius, int durationSeconds) {
        this.livingEntity = livingEntity;
        this.height = height;
        this.radius = radius;
        this.durationTicks = durationSeconds * 20;
        this.elapsedTicks = 0;
    }

    @Override
    public void run() {
        if (elapsedTicks >= durationTicks) {
            this.cancel();
            return;
        }

        double progress = (double) elapsedTicks / durationTicks;
        double yOffset = height * progress;

        Location zombieLocation = livingEntity.getLocation();
        Location particleLocation = new Location(zombieLocation.getWorld(), zombieLocation.getX(), zombieLocation.getY() + yOffset, zombieLocation.getZ());

        double angle = 0;
        while (angle < 2 * Math.PI) {
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);

            Location haloLocation = particleLocation.clone().add(x, 0, z);

            haloLocation.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, haloLocation, 1);

            angle += Math.PI / 8;
        }

        if(elapsedTicks % (durationTicks / 10) == 0) {
            livingEntity.getWorld().playSound(zombieLocation, Sound.BLOCK_PISTON_EXTEND, 1f, -2f);
            livingEntity.getWorld().playSound(zombieLocation, Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 1f, -2f);
        }

        elapsedTicks++;
    }

    public void start() {
        this.runTaskTimer(DungeonMobs.plugin, 0, 1);
    }
}
