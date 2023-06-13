package ezcoins.dungeonmobs.mobs;

import ezcoins.dungeonmobs.DungeonMobs;
import ezcoins.dungeonmobs.abilities.FrostCircle;
import ezcoins.dungeonmobs.tasks.HealthBar;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public abstract class CustomEntity  {
    @Getter
    private static HashMap<UUID, CustomEntity> activeBosses = new HashMap<>();
    @Getter
    private LivingEntity livingEntity;
    @Getter
    private final Player summoner;
    @Getter
    private final UUID uuid;
    @Getter
    private String name;


    public CustomEntity(String name, EntityType entityType, Player summoner) {
        this.livingEntity = (LivingEntity) summoner.getWorld().spawnEntity(summoner.getLocation(), entityType);
        this.name = name;
        this.summoner = summoner;
        this.uuid = UUID.randomUUID();
        Entity entityHandle = livingEntity;
        entityHandle.getPersistentDataContainer().set(new NamespacedKey(DungeonMobs.plugin, "UUID"), PersistentDataType.STRING, uuid.toString());

        activeBosses.put(summoner.getUniqueId(), this);
    }

    public static void killAllEntity() {
        for(CustomEntity customEntity : activeBosses.values()) {
            for(Entity living : customEntity.getLivingEntity().getPassengers()) {
                living.remove();
            }
            customEntity.getLivingEntity().remove();
        }
        FrostCircle.forceClean();
        HealthBar.removeAllHealthBars();
        activeBosses.clear();
    }

}
