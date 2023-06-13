package ezcoins.dungeonmobs;

import ezcoins.dungeonmobs.abilities.FrostCircle;
import ezcoins.dungeonmobs.commands.Commands;
import ezcoins.dungeonmobs.managers.ListenerManager;
import ezcoins.dungeonmobs.mobs.CustomEntity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DungeonMobs extends JavaPlugin {
    public static DungeonMobs plugin;
    @Override
    public void onEnable() {
        plugin = this;
        ListenerManager listenerManager = new ListenerManager();

        getCommand("dungeonMobs").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {
        CustomEntity.killAllEntity();
    }

    public static PluginManager getPluginManager() {
        return plugin.getServer().getPluginManager();
    }
}
