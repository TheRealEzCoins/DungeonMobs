package ezcoins.dungeonmobs.abilities;

import dev.dbassett.skullcreator.SkullCreator;
import ezcoins.dungeonmobs.DungeonMobs;
import ezcoins.dungeonmobs.utils.AttributeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class PetrifyingGaze implements Listener {
    private List<UUID> petrifiedPlayers = new ArrayList<>();

    private LivingEntity livingEntity;
    private int petrifyDuration = 5; // Adjust the duration of petrification in seconds
    public PetrifyingGaze(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;

        registerListener();
    }

    private boolean isPlayerLookingAtHead(Player player) {
        Location playerEyeLocation = player.getEyeLocation();
        Location entityLocation = livingEntity.getEyeLocation();

        Vector toEntity = entityLocation.toVector().subtract(playerEyeLocation.toVector());
        Vector direction = playerEyeLocation.getDirection();

        double dotProduct = toEntity.normalize().dot(direction);

        return dotProduct > 0.99; // Adjust the dot product threshold as needed for accuracy
    }

    public void startEvent(long startInSeconds, long delayInSeconds) {
        new BukkitRunnable() {
            @Override
            public void run() {
                PetrifyingGaze petrifyingGaze = new PetrifyingGaze(livingEntity);
                petrifyingGaze.startAbility();
            }
        }.runTaskTimer(DungeonMobs.plugin, startInSeconds * 20, delayInSeconds * 20); // 100 ticks = 5 seconds
    }


    private void startAbility() {
        livingEntity.getEquipment().setHelmet(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzE0NWZiMWJiYWFjNWY4NzhjMjlmOWM4NzEyMzE5ODYzNDA2MjM2MWM4NmFjNGFiMTExZGUxOWEzNzliYWEwNiJ9fX0="));
        AttributeUtils.changeAttribute(livingEntity, Attribute.GENERIC_ATTACK_DAMAGE, 36);
        new BukkitRunnable() {
            private int time = 0;
            @Override
            public void run() {
                if(time >= 100) {
                    livingEntity.getEquipment().setHelmet(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTkzMzQ4NTMzNWZjOWMzOGFiMzJlNmJkMmNkNDgwYzNiYWY2MWIwZTNmNmJjYTYxZTBlM2NmMWY3NzQ3YzlkYSJ9fX0="));
                    DungeonMobs.plugin.getLogger().info("Ending Ability");
                    AttributeUtils.changeAttribute(livingEntity, Attribute.GENERIC_ATTACK_DAMAGE, 18);
                    cancel();
                }
                if (livingEntity.isValid()) {
                    for(Player nearbyPlayer : getNearbyPlayers(20)) {
                        if(isPlayerLookingAtHead(nearbyPlayer)) {
                            petrifyPlayer(nearbyPlayer);
                        }
                    }
                }
                time++;
            }
        }.runTaskTimer(DungeonMobs.plugin, 0,1);
    }

    private void petrifyPlayer(Player player) {
        if(petrifiedPlayers.contains(player.getUniqueId())) return;
        petrifiedPlayers.add(player.getUniqueId());
        player.sendMessage("You've been petrified!");

        Bukkit.getScheduler().runTaskLater(DungeonMobs.plugin, () -> removePetrification(player), petrifyDuration * 20L);
    }

    private void removePetrification(Player player) {
        petrifiedPlayers.remove(player.getUniqueId());
        player.sendMessage("You've been unpetrified!");
        player.removePotionEffect(PotionEffectType.WEAKNESS);
    }


    private List<Player> getNearbyPlayers(int radius) {
        List<Entity> nearbyEntities = livingEntity.getNearbyEntities(radius, radius, radius);
        return nearbyEntities.stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .collect(Collectors.toList());
    }

    private void registerListener() {
        DungeonMobs.getPluginManager().registerEvents(new PetrifyingListener(), DungeonMobs.plugin);
    }

    private class PetrifyingListener implements Listener {
        @EventHandler
        private void onMove(PlayerMoveEvent event) {
            if(!petrifiedPlayers.contains(event.getPlayer().getUniqueId())) return;
            event.setCancelled(true);
        }
    }

}
