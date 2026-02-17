package io.github.darjeelingt.spigot.git4dp.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import io.github.darjeelingt.spigot.git4dp.Main;
import io.github.darjeelingt.spigot.git4dp.util.DatapackRepository;

final public class G4DList extends BaseCommand {
    public G4DList(Main plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean result = false;
        if (args.length > 0) {
            sender.sendMessage("Error: Usage: /dpl-list");
        } else {
            List<DatapackRepository> repositories = this.getPlugin().getDatapacks();
            
            sender.sendMessage(String.format("Connected Datapacks(%d):", repositories.size()));
            if (repositories.size() == 0){
                sender.sendMessage("No Connected Datapack");
            } else {
                for (DatapackRepository repository : repositories) {
                    sender.sendMessage(String.format("  - \"%s\"", repository.datapackname()));
                }
            }
            result = true;
        }

        return result;
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
