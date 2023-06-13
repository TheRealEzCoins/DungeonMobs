package ezcoins.dungeonmobs.mobs;

import com.google.common.io.MoreFiles;
import dev.dbassett.skullcreator.SkullCreator;
import ezcoins.dungeonmobs.DungeonMobs;
import ezcoins.dungeonmobs.abilities.*;
import ezcoins.dungeonmobs.animations.SpawnAnimationCube;
import ezcoins.dungeonmobs.particles.ParticleCircle;
import ezcoins.dungeonmobs.particles.ParticleCircleUpwards;
import ezcoins.dungeonmobs.particles.ParticleCube;
import ezcoins.dungeonmobs.tasks.HealthBar;
import ezcoins.dungeonmobs.tasks.RotationSkullTask;
import ezcoins.dungeonmobs.utils.ArmorUtils;
import ezcoins.dungeonmobs.utils.AttributeUtils;
import ezcoins.dungeonmobs.utils.Utils;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class ZombieMob extends CustomEntity {
    @Getter
    private final Location location;

    @Getter
    private final Zombie zombie;

    @Getter
    private ZombieMob zombieMob;

    public ZombieMob(Location location, Player player) {
        super(EntityType.ZOMBIE, player);
        this.location = location;
        this.zombie = (Zombie) getLivingEntity();
        zombieMob = this;
        registerListener();

        zombie.setShouldBurnInDay(false);
        zombie.setAI(false);
        zombie.setAdult();
        zombie.setArrowCooldown(0);
    }

    public void spawnMob() {

        AttributeUtils.changeAttribute(zombie, Attribute.GENERIC_KNOCKBACK_RESISTANCE, 1.0);
        AttributeUtils.changeAttribute(zombie, Attribute.GENERIC_MAX_HEALTH, 2000);
        AttributeUtils.changeAttribute(zombie, Attribute.GENERIC_ATTACK_DAMAGE, 18);
        AttributeUtils.changeAttribute(zombie, Attribute.GENERIC_MOVEMENT_SPEED, 0.5);

        zombie.setHealth(zombie.getMaxHealth());

        SpawnAnimationCube spawnAnimationCube = new SpawnAnimationCube(getLivingEntity(), getSummoner());

        spawnAnimationCube.startAnimation();

        equipArmor();

        SphereAttack sphereAttack = new SphereAttack(zombie);
        sphereAttack.startEvent(12, 20);

        BeaconAbility beaconAbility = new BeaconAbility(zombie, getSummoner());
        beaconAbility.startEvent(10, 20);

        PetrifyingGaze petrifyingGaze = new PetrifyingGaze(zombie);
        petrifyingGaze.startEvent(15, 30);
    }




    public void equipArmor() {
        ItemStack[] armorContents = new ItemStack[] {
                new ItemStack(Material.DIAMOND_BOOTS),
                ArmorUtils.colorArmorPiece(new ItemStack(Material.LEATHER_LEGGINGS), Color.BLACK),
                ArmorUtils.colorArmorPiece(new ItemStack(Material.LEATHER_CHESTPLATE), Color.BLACK),
                SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTkzMzQ4NTMzNWZjOWMzOGFiMzJlNmJkMmNkNDgwYzNiYWY2MWIwZTNmNmJjYTYxZTBlM2NmMWY3NzQ3YzlkYSJ9fX0=")
        };

        new BukkitRunnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                if (currentIndex < armorContents.length) {
                    ItemStack armorPiece = armorContents[currentIndex];
                    switch (currentIndex) {
                        case 0:
                            zombie.getEquipment().setBoots(armorPiece);
                            break;
                        case 1:
                            zombie.getEquipment().setLeggings(armorPiece);
                            break;
                        case 2:
                            zombie.getEquipment().setChestplate(armorPiece);
                            break;
                        case 3:
                            zombie.getEquipment().setHelmet(armorPiece);
                            zombie.setInvisible(true);
                            break;
                    }
                    currentIndex++;
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(DungeonMobs.plugin, 20L, 20L);
        zombie.getEquipment().setItemInMainHand(new ItemStack(Material.GOLDEN_HOE));


    }

    private void registerListener() {
        DungeonMobs.getPluginManager().registerEvents(new ZombieAbilityListener(), DungeonMobs.plugin);
    }

    private class ZombieAbilityListener implements Listener {
        @EventHandler
        public void onZombieHit(EntityDamageByEntityEvent event) {
            Entity entity = event.getEntity();
            if (!entity.getPersistentDataContainer().has(new NamespacedKey(DungeonMobs.plugin, "UUID"))) return;
            String UUID = entity.getPersistentDataContainer().get(new NamespacedKey(DungeonMobs.plugin, "UUID"), PersistentDataType.STRING);
            String living = zombie.getPersistentDataContainer().get(new NamespacedKey(DungeonMobs.plugin, "UUID"), PersistentDataType.STRING);
            if (UUID.equals(living)) {
                if (event.getDamager().getType() == EntityType.ARROW) {
                    zombie.teleport(getSummoner());
                    event.setCancelled(true);
                }
            }
        }

    }
}
