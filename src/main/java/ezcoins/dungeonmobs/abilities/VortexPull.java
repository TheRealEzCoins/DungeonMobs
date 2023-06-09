package ezcoins.dungeonmobs.abilities;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VortexPull {

    private Player player;
    private Location location;
    private double pullRadius;
    private double pullStrength;

    public VortexPull(Player player, Location center, double pullRadius, double pullStrength) {
        this.player = player;
        this.location = center;
        this.pullRadius = pullRadius;
        this.pullStrength = pullStrength;
    }

    public void pull() {
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
