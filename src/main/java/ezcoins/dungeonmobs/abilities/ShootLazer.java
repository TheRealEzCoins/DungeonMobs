package ezcoins.dungeonmobs.abilities;

import com.google.common.io.MoreFiles;
import ezcoins.dungeonmobs.DungeonMobs;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class ShootLazer {

    private LivingEntity shooter;
    private LivingEntity target;
    private Location startLocation;
    private Location targetLocation;

    public ShootLazer(LivingEntity shooter, LivingEntity target) {
        this.shooter = shooter;
        this.target = target;

        startLocation = shooter.getEyeLocation();
        targetLocation = target.getEyeLocation();

    }


    public void shoot() {
        double maxDistance = startLocation.distance(targetLocation);
        double step = 0.1;
        double progress = step;
        DungeonMobs.plugin.getLogger().info("Attempting to shoot...");

        while(progress < maxDistance) {
            DungeonMobs.plugin.getLogger().info("Charging... " + progress);
            Location currentLocation = startLocation.clone().add(targetLocation.subtract(startLocation).multiply(progress / maxDistance));
            currentLocation.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, currentLocation, 1, 0, 0, 0, 0, null, true);

            if(currentLocation.distance(targetLocation) < 1) {
                DungeonMobs.plugin.getLogger().info("Hit!");
                target.damage(5);
                break;
            }

            progress += step;
        }
    }
}
