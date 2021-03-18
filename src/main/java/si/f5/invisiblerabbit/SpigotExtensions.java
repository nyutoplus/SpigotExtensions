package si.f5.invisiblerabbit;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import si.f5.invisiblerabbit.extend.entity.EntityExtends;
import si.f5.invisiblerabbit.extend.world.WorldExtends;
import si.f5.invisiblerabbit.util.FunctionHelper;

public class SpigotExtensions extends JavaPlugin {

    CommandManager cm;

    FunctionHelper[] fh;

    @Override
    public void onLoad() {
	cm = new CommandManager();
	FunctionHelper[] fhtmp = { new WorldExtends(), new EntityExtends() };
	fh = fhtmp;
    }

    @Override
    public void onEnable() {
	saveDefaultConfig();
	FileConfiguration config = getConfig();
	FileConfiguration perWorldConfig = new YamlConfiguration();
	try {
	    perWorldConfig.load(new File(getDataFolder() + File.separator + "perWorldConfig.yml"));
	} catch (Exception e) {
	    System.out.println(e);
	}
	for (FunctionHelper fhl : fh) {
	    fhl.load();
	    fhl.setConfig(config, perWorldConfig);

	    Listener[] lis = fhl.getListener();
	    if (lis != null && lis.length != 0) {
		for (Listener l : lis) {
		    getServer().getPluginManager().registerEvents(l, this);
		}
	    }
	}
    }

    @Override
    public void onDisable() {
	for (FunctionHelper i : fh) {
	    i.unload();
	}
    }

    @Override
    public void reloadConfig() {
	super.reloadConfig();
	FileConfiguration perWorldConfig = new YamlConfiguration();
	try {
	    perWorldConfig.load(new File(getDataFolder() + File.separator + "perWorldConfig.yml"));
	} catch (Exception e) {
	    System.out.println(e);
	}
	for (FunctionHelper i : fh) {
	    i.setConfig(getConfig(), perWorldConfig);
	}
    }

    @Override
    public void saveDefaultConfig() {
	super.saveDefaultConfig();
	File perWorldConfig = new File(getDataFolder(), "perWorldConfig.yml");
	if (!perWorldConfig.exists()) {
	    saveResource("perWorldConfig.yml", false);
	}

    }

    public void reload() {
	reloadConfig();
	for (FunctionHelper fhl : fh) {
	    fhl.reload(getServer());
	}
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
	return cm.onCommand(sender, cmd, commandLabel, args, this);
    }
}
