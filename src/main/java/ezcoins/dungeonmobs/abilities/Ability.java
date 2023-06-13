package ezcoins.dungeonmobs.abilities;

import ezcoins.dungeonmobs.DungeonMobs;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.ParametersAreNonnullByDefault;

public interface Ability {

    @ParametersAreNonnullByDefault
    default void startEvent(long startInSeconds, long delayInSeconds) {
    }

    @ParametersAreNonnullByDefault
    default void createEvent() {
    }

    @ParametersAreNonnullByDefault
    default void createEvent(Block block, Location beaconLocation) {
    }

}
