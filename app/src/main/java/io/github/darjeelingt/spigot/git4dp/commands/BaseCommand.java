package io.github.darjeelingt.spigot.git4dp.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import io.github.darjeelingt.spigot.git4dp.Main;

public abstract class BaseCommand implements CommandExecutor {
    protected static Main PLUGIN = null;

    public BaseCommand(Main plugin) {
        if (this.getPlugin() == null) {
            this.setPlugin(plugin);
        }
        if (this.getInstance() == null) {
            throw new NullPointerException("Instance is null");
        }
        if (this.getCommandName() == null) {
            throw new NullPointerException("CommandName is null");
        }

        this.register();
    }

    final Main getPlugin() {
        return BaseCommand.PLUGIN;
    }

    final void setPlugin(Main plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Argument \"plugin\" is null");
        }
        BaseCommand.PLUGIN = plugin;
    }

    public void register() {
        PluginCommand c = this.getPlugin().getCommand(this.getCommandName());
        if (c != null) {
            c.setExecutor(this.getInstance());
            if (this.getInstance() instanceof TabCompleter)
                c.setTabCompleter((TabCompleter)this.getInstance());
        }
    }

    abstract BaseCommand getInstance();

    public abstract String getCommandName();
}
