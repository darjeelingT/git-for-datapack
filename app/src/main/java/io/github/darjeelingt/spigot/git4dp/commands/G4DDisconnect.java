package io.github.darjeelingt.spigot.git4dp.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import io.github.darjeelingt.spigot.git4dp.Main;
import io.github.darjeelingt.spigot.git4dp.util.DatapackRepository;

final public class G4DDisconnect extends BaseCommand {
    public G4DDisconnect(Main plugin) {
        super(plugin);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean result = false;

        if (args.length == 1) {
            if (args[0] == null || args[0].isEmpty()) {
                sender.sendMessage("Error: Usage: /dpl-disconnect <datapack_name>");
            } else {
                int packid = -1;
                List<DatapackRepository> repositories = this.getPlugin().getDatapacks();

                for (int i = 0; i < repositories.size(); i++) {
                    if (repositories.get(i).datapackname().equals(args[0])) {
                        packid = i;
                        break;
                    }
                }

                if (packid == -1) {
                    sender.sendMessage(String.format("Error: Datapack \"%s\" Not Found", args[0]));
                } else {
                    repositories.remove(packid);
                    
                    result = true;
                    sender.sendMessage(String.format("Successfully Removed Remote Repository Connection \"%s\"!", args[0]));
                }
            }
        } else {
            sender.sendMessage("Error: Usage: /dpl-disconnect <datapack_name>");
        }

        return result;
    }
    
    @Override
    BaseCommand getInstance() {
        return this;
    }

    @Override
    public String getCommandName() {
        return "g4d-disconnect";
    }
}