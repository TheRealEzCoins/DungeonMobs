package ezcoins.dungeonmobs.abilities;

import ezcoins.dungeonmobs.DungeonMobs;
import ezcoins.dungeonmobs.particles.ParticleCircle;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SphereAttack {

    private LivingEntity livingEntity;

    public SphereAttack(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }


    public void trigger() {
        livingEntity.setAI(false);
        ParticleCircle particleCircle = new ParticleCircle(livingEntity.getLocation(), Particle.CRIT ,10, 5);
        particleCircle.start();

        new BukkitRunnable() {
            private int time = 0;
            private List<Player> playerHurtList = new ArrayList<>();
            @Override
            public void run() {
                if(time >= 10) {
                    cancel();
                }
                time++;
                for(Player player : getNearbyPlayers(time)) {
                    if(!playerHurtList.contains(player)) {
                        player.setHealth(time);
                        player.damage(1, livingEntity);
                        player.setHurtDirection(1f);
                        playerHurtList.add(player);
                    }
                }
            }
        }.runTaskTimer(DungeonMobs.plugin, 10, 0);

        new BukkitRunnable() {

            @Override
            public void run() {
                livingEntity.setAI(true);
            }
        }.runTaskLater(DungeonMobs.plugin, 200);
    }

    private List<Player> getNearbyPlayers(int radius) {
        List<Entity> nearbyEntities = livingEntity.getNearbyEntities(radius, radius, radius);
        return nearbyEntities.stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .collect(Collectors.toList());
    }
}
