package io.github.darjeelingt.spigot.git4dp.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import io.github.darjeelingt.spigot.git4dp.Main;
import io.github.darjeelingt.spigot.git4dp.util.DatapackRepository;

final public class G4DConnect extends BaseCommand {
    public G4DConnect(Main plugin) {
        super(plugin);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean result = false;

        Map<String, String> newPack = new HashMap<String, String>();
        switch (args.length) {
            case 2:
                newPack.put("name", args[0]);
                newPack.put("URI", args[1]);
                newPack.put(
                    "branch", 
                    this.getPlugin()
                        .getCustomConfig()
                        .getString("default.branch")
                );
                newPack.put(
                    "directory", 
                    this.getPlugin()
                        .getCustomConfig()
                        .getString("default.directory")
                );
                break;
            case 4:
                newPack.put("name", args[0]);
                newPack.put("URI", args[1]);
                newPack.put("branch", args[2]);
                newPack.put("directory", args[3]);
                break;
            default:
                sender.sendMessage("Error: Usage: /dpl-connect <datapack_name> <URL> [branch] [directory]");
                break;
        }
        if (!newPack.isEmpty()) {
            try {
                DatapackRepository newRepository = DatapackRepository.toDPRepository(newPack);

                List<DatapackRepository> repositories = this.getPlugin().getDatapacks();

                boolean exist = false;
                for (DatapackRepository repository : repositories) {
                    if (repository.datapackname().equals(newRepository.datapackname())) {
                        exist = true;
                    }
                }

                if (exist) {
                    sender.sendMessage(String.format("Error: Datapack \"%s\" is already exists", newRepository.datapackname()));
                } else {

                    this.getPlugin().getDatapacks().add(newRepository);

                    result = true;
                    sender.sendMessage(String.format("Successfully Added Datapack Repository \"%s\"!", newRepository.datapackname()));
                }
            } catch (IllegalArgumentException e) {
                sender.sendMessage(String.format("Error: %s", e.getMessage()));
            }
        }

        return result;
    }
    
    @Override
    BaseCommand getInstance() {
        return this;
    }

    @Override
    public String getCommandName() {
        return "g4d-connect";
    }
}
