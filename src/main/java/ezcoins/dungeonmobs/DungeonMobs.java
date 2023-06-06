package ezcoins.dungeonmobs;

import ezcoins.dungeonmobs.commands.Commands;
import org.bukkit.plugin.java.JavaPlugin;

public final class DungeonMobs extends JavaPlugin {
    public static DungeonMobs plugin;
    @Override
    public void onEnable() {
        plugin = this;

        getCommand("dungeonMobs").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
