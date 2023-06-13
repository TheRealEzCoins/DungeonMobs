package ezcoins.dungeonmobs.abilities;

import ezcoins.dungeonmobs.DungeonMobs;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BeaconAbility implements Ability {

    private static List<Block> activeBeaconAbility = new ArrayList<>();
    private final Player summoner;
    private final LivingEntity mob;

    public BeaconAbility(LivingEntity livingEntity, Player player) {
        this.mob = livingEntity;
        this.summoner = player;
    }

    @Override
    public void startEvent(long startInSeconds, long delayInSeconds) {
        new BukkitRunnable() {
            @Override
            public void run() {
                BeaconAbility beaconAbility = new BeaconAbility(mob, summoner.getPlayer());
                beaconAbility.createFallingBlock(mob.getLocation());
            }
        }.runTaskTimer(DungeonMobs.plugin, startInSeconds * 20, delayInSeconds * 20); // 100 ticks = 5 seconds
    }

    @Override
    public void createEvent(Block block, Location beaconLocation) {
        activeBeaconAbility.add(block);
        block.setType(Material.BEACON);
        block.getWorld().strikeLightning(beaconLocation);
        new BukkitRunnable() {
            int timer = 0;
            @Override
            public void run() {
                timer++;
                if(mob.isDead()) return;
                summoner.playSound(summoner.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 10f);
                if (block.getType() == Material.BEACON) {
                    for (Player player : beaconLocation.getNearbyPlayers(20)) {
                        Location playerLocation = player.getLocation();
                        if (playerLocation.distance(beaconLocation) <= 2) {
                            block.setType(Material.AIR);
                            activeBeaconAbility.remove(block);
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1f, 3f);
                            cancel();
                            return;
                        }
                    }
                    if(timer >= 7) {
                        block.setType(Material.AIR);
                        activeBeaconAbility.remove(block);
                        summoner.setHealth(0);
                        cancel();
                    }

                }
            }
        }.runTaskTimer(DungeonMobs.plugin, 1, 20); // 100 ticks = 5 seconds
    }

    private Location findBeaconLocation(Location centerLocation, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location location = centerLocation.clone().add(x, 0, z);
                Block block = location.getBlock();
                if (block.getType() == Material.AIR && block.getRelative(BlockFace.UP).getType() == Material.AIR) {
                    return location;
                }
            }
        }
        return null;
    }

    private int task;
    private void createFallingBlock(Location startLocation) {
        Location newLocation = mob.getLocation().clone();
        if(mob.isDead()) return;
        Location targetLocation = findBeaconLocation(newLocation, 5);
        startLocation.add(0, 2, 0);
        World world = startLocation.getWorld();

        FallingBlock fallingBlock = world.spawnFallingBlock(startLocation, Material.BEACON.createBlockData());

        fallingBlock.setGravity(true);
        fallingBlock.setDropItem(false);
        fallingBlock.setHurtEntities(false);

        Vector direction = targetLocation.toVector().subtract(startLocation.toVector()).normalize();

        fallingBlock.setVelocity(direction);

        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(DungeonMobs.plugin, () -> {
            if (fallingBlock.isOnGround()) {
                Location landedLocation = fallingBlock.getLocation();
                createEvent(fallingBlock.getLocation().getBlock(), landedLocation);

                fallingBlock.remove();
                Bukkit.getScheduler().cancelTask(task);
            }
        }, 0L, 1L);
    }

    public static void destroyAllBeacons() {
        for(Block block : activeBeaconAbility) {
            if(block.getType().equals(Material.BEACON)) {
                block.setType(Material.AIR);
            }
        }
    }


}
