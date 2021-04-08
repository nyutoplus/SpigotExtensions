package si.f5.invisiblerabbit.extend.entity;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import si.f5.invisiblerabbit.util.FunctionHelperAdapter;

public class EntityExtends extends FunctionHelperAdapter {

    EntitySpawnAccess esa;

    public EntityExtends() {
	esa = new EntitySpawnAccess();
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {
	esa.unload();
    }

    @Override
    public void setConfig(FileConfiguration fileConfig, FileConfiguration perWorldConfig) {
	esa.setConfig(fileConfig, perWorldConfig);
    }

    @Override
    public Listener[] getListener() {
	Listener[] tmp = { esa };
	return tmp;
    }

    @Override
    public void reload(Server server) {
    }

}
