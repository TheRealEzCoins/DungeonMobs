package ezcoins.dungeonmobs.mobs;

import ezcoins.dungeonmobs.DungeonMobs;
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

public abstract class CustomEntity {
    @Getter
    private static HashMap<UUID, CustomEntity> activeBosses = new HashMap<>();
    @Getter
    private LivingEntity livingEntity;
    @Getter
    private final Player summoner;
    @Getter
    private final UUID uuid;

    public CustomEntity(EntityType entityType, Player summoner) {
        this.livingEntity = (LivingEntity) summoner.getWorld().spawnEntity(summoner.getLocation(), entityType);;
        this.summoner = summoner;
        this.uuid = UUID.randomUUID();
        Entity entityHandle = livingEntity;
        entityHandle.getPersistentDataContainer().set(new NamespacedKey(DungeonMobs.plugin, "UUID"), PersistentDataType.STRING, uuid.toString());

        activeBosses.put(summoner.getUniqueId(), this);
    }

    public static void killAllEntity() {
        for(CustomEntity customEntity : activeBosses.values()) {
            customEntity.getLivingEntity().remove();
        }
        HealthBar.removeAllHealthBars();
        activeBosses.clear();
    }

}
