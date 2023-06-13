package ezcoins.dungeonmobs.animations;

import ezcoins.dungeonmobs.DungeonMobs;
import ezcoins.dungeonmobs.particles.ParticleCircleUpwards;
import ezcoins.dungeonmobs.particles.ParticleCube;
import ezcoins.dungeonmobs.tasks.HealthBar;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnAnimationCube {
    private LivingEntity livingEntity;

    private Player player;
    public SpawnAnimationCube(LivingEntity livingEntity, Player player) {
        this.livingEntity = livingEntity;
        this.player = player;
    }

    public void startAnimation() {
        ParticleCircleUpwards particleCircleUpwards = new ParticleCircleUpwards(livingEntity, 2, 1, 4);
        particleCircleUpwards.start();
        Location square = livingEntity.getLocation();
        square.add(0, 1, 0);
        ParticleCube particleSquare = new ParticleCube(square, 1, 5, 4);
        particleSquare.start();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (livingEntity.isValid()) {
                    player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1f, 5f);
                    HealthBar healthBar = new HealthBar(livingEntity);
                    livingEntity.setSilent(false);
                    livingEntity.setAI(true);
                }
            }
        }.runTaskLater(DungeonMobs.plugin, 4 * 20);
    }
}
