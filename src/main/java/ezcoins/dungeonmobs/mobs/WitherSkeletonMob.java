package ezcoins.dungeonmobs.mobs;

import ezcoins.dungeonmobs.abilities.RotationBeams;
import ezcoins.dungeonmobs.animations.SpawnAnimationCube;
import ezcoins.dungeonmobs.particles.Dome;
import org.bukkit.Location;
import org.bukkit.entity.*;

public class WitherSkeletonMob extends CustomEntity {


    private LivingEntity livingEntity;

    public WitherSkeletonMob(Player summoner) {
        super("Umbralrend, the Shrouded Enigma", EntityType.ENDERMAN, summoner);
        this.livingEntity = getLivingEntity();

        Enderman enderman = (Enderman) livingEntity;
        enderman.setCanPickupItems(false);
        SpawnAnimationCube animationCube = new SpawnAnimationCube(this, summoner);
        animationCube.startAnimation();


        Dome dome = new Dome(this, 3);
        dome.createDelayedEvent(10, 60);
    }


}
