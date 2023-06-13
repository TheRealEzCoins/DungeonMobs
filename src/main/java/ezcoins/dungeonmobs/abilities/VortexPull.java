package ezcoins.dungeonmobs.abilities;

import ezcoins.dungeonmobs.DungeonMobs;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class VortexPull implements Ability {

    private Player player;
    private Location location;
    private double pullRadius;
    private double pullStrength;
    private LivingEntity livingEntity;

    public VortexPull(Player player, LivingEntity livingEntity, double pullRadius, double pullStrength) {
        this.player = player;
        this.livingEntity = livingEntity;
        this.location = livingEntity.getLocation();
        this.pullRadius = pullRadius;
        this.pullStrength = pullStrength;
    }

    @Override
    public void startEvent(long startInSeconds, long delayInSeconds) {
        new BukkitRunnable() {
            @Override
            public void run() {
                VortexPull vortexPull = new VortexPull(player, livingEntity, 50, 5);
                vortexPull.createEvent();

            }
        }.runTaskTimer(DungeonMobs.plugin, startInSeconds * 20, delayInSeconds * 20); // 100 ticks = 5 seconds
    }

    @Override
    public void createEvent() {
        if(livingEntity.isDead()) return;
        DungeonMobs.plugin.getLogger().info("Pulling!");
        Location playerLocation = player.getLocation();
        Vector direction = location.toVector().subtract(playerLocation.toVector()).normalize();

        double distance = location.distance(playerLocation);
        if (distance > pullRadius) {
            return; // Player is outside the vortex pull range
        }

        double strength = pullStrength * (1 - (distance / pullRadius));

        Vector velocity = player.getVelocity();
        velocity.add(direction.multiply(strength));
        player.setVelocity(velocity);
    }

}
