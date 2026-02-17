package io.github.darjeelingt.spigot.git4dp;


import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.darjeelingt.spigot.git4dp.commands.*;
import io.github.darjeelingt.spigot.git4dp.util.DatapackRepository;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Main extends JavaPlugin {
    public List<BaseCommand> commands;
    private FileConfiguration customConfig;
    private ArrayList<DatapackRepository> datapacks;
    private Path worldDataDirectory;

    @Override
    public void onEnable() {
        
        try {
            this.worldDataDirectory = Bukkit.getWorldContainer().toPath().toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            this.getLogger().info(String.format("Failed: Can't Read WorldFolder [%s]", e.getMessage()));
            this.onDisable();
        }

        this.commands = new ArrayList<BaseCommand>();
        this.commands.add(new G4DConnect(this));
        this.commands.add(new G4DDisconnect(this));
        this.commands.add(new G4DEdit(this));
        this.commands.add(new G4DInfo(this));
        this.commands.add(new G4DList(this));
        this.commands.add(new G4DReload(this));

        this.saveDefaultConfig();

        this.loadCustomConfig();
        this.loadDatapacks();

        this.getLogger().info("Plugin startup!");
    }

    @Override
    public void onDisable() {
        this.saveCustomConfig();

        this.getLogger().info("Plugin shutdown!");
    }

    public void loadCustomConfig() {
        this.customConfig = this.getConfig();
    }

    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    public void saveCustomConfig() {
        if (this.getCustomConfig() != null) {

            if (this.datapacks != null) {
                this.saveDataPacks();
            }

            this.saveConfig();
            this.getLogger().info("Saved Config File!");
        }
    }

    public ArrayList<DatapackRepository> getDatapacks() {
        return this.datapacks;
    }

    public void loadDatapacks() {
        if (this.getCustomConfig() == null) {
            return;
        }

        List<Map<?, ?>> packMapList = this.getCustomConfig().getMapList("datapacks");
        this.datapacks = new ArrayList<DatapackRepository>();

        for (Map<?, ?> packMap : packMapList) {
            try {
                DatapackRepository repository = DatapackRepository.toDPRepository(packMap);
                this.datapacks.add(repository);

                this.getLogger().info(String.format("Loaded \"%s\"!", repository.datapackname()));
            } catch (IllegalArgumentException e) {
                this.getLogger().info(String.format("Error: %s", e.getMessage()));
            }
        }
    }

    public void saveDataPacks() {
        List<Map<String, String>> packMapList = new ArrayList<Map<String, String>>();

        List<DatapackRepository> repositories = this.getDatapacks();

        for (DatapackRepository repository : repositories) {
            packMapList.add(repository.toMap());
        }
        this.getCustomConfig().set("datapacks", packMapList);
    }

    public Path getWorldDataDirectory() {
        return this.worldDataDirectory;
    }
}
