package ezcoins.dungeonmobs.abilities;

import ezcoins.dungeonmobs.DungeonMobs;
import ezcoins.dungeonmobs.mobs.ZombieMob;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class BeaconAbility {

    private final Location location;
    private final Player summoner;
    private final Zombie mob;

    public BeaconAbility(ZombieMob zombieMob) {
        this.location = zombieMob.getZombie().getLocation();
        this.mob = zombieMob.getZombie();
        this.summoner = zombieMob.getSummoner();
    }

    public void startEvent() {
        createFallingBlock(location);
    }

    private void throwBeacon(Block block, Location beaconLocation) {
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
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1f, 3f);
                            cancel();
                            return;
                        }
                    }
                    if(timer >= 7) {
                        block.setType(Material.AIR);
                        mob.remove();
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
        Location targetLocation = findBeaconLocation(location, 5);
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
                throwBeacon(fallingBlock.getLocation().getBlock(), landedLocation);

                fallingBlock.remove();
                Bukkit.getScheduler().cancelTask(task);
            }
        }, 0L, 1L);
    }




}
