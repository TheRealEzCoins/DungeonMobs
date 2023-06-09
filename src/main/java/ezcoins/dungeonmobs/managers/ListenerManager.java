package ezcoins.dungeonmobs.managers;

import ezcoins.dungeonmobs.DungeonMobs;
import ezcoins.dungeonmobs.mobs.ZombieMob;
import ezcoins.dungeonmobs.tasks.HealthBar;
import org.bukkit.event.Listener;

public class ListenerManager {
    public ListenerManager() {
    }

    private void addListener(Listener listener) {
        DungeonMobs.getPluginManager().registerEvents(listener, DungeonMobs.plugin);
    }
}
