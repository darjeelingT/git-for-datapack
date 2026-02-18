package io.github.darjeelingt.spigot.git4dp.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import io.github.darjeelingt.spigot.git4dp.Main;

final public class G4DList extends BaseCommand implements TabCompleter {
    public G4DList(Main plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean result = false;
        if (args.length > 0) {
            sender.sendMessage("Error: Usage: /dpl-list");
        } else {
            List<String> repositoryNames = this.getPlugin().getDatapackNames();
            
            sender.sendMessage(String.format("Connected Datapacks(%d):", repositoryNames.size()));
            if (repositoryNames.size() == 0){
                sender.sendMessage("No Connected Datapack");
            } else {
                for (String name : repositoryNames) {
                    sender.sendMessage(String.format("  - \"%s\"", name));
                }
            }
            result = true;
        }

        return result;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of();
    }
    
    @Override
    BaseCommand getInstance() {
        return this;
    }

    @Override
    public String getCommandName() {
        return "g4d-list";
    }
}
