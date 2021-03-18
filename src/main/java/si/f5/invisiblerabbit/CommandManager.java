package si.f5.invisiblerabbit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandManager {

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args,SpigotExtensions se) {
	if (cmd.getName().equalsIgnoreCase("se") || cmd.getName().equalsIgnoreCase("SpigotExtensions")) {
	    if (args.length != 0) {
		if (args[0].equalsIgnoreCase("reload")) {
		    if (sender.hasPermission("spigotextensions.admin.reload")) {
			sender.sendMessage("reload");
			se.reload();
		    }
		}
	    }
	}
	return false;
    }
}
