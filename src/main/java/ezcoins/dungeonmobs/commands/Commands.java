package ezcoins.dungeonmobs.commands;

import ezcoins.dungeonmobs.mobs.ZombieMob;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("dungeonMobs")) {
            if (args.length == 0) {

            } else if (args[0].equalsIgnoreCase("spawn")) {
                Player player = (Player) sender;
                ZombieMob zombieMob = new ZombieMob();
                zombieMob.spawnMob(player.getLocation());
            }
        }
        return true;
    }
}
