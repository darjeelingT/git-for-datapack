package io.github.darjeelingt.spigot.git4dp.commands;

import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import io.github.darjeelingt.spigot.git4dp.Main;
import io.github.darjeelingt.spigot.git4dp.util.DatapackRepository;

final public class G4DUpdate extends BaseCommand implements TabCompleter {
    final List<String> attributes = List.of("URI", "branch", "directory");

    public G4DUpdate(Main plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean result = false;

        if (args.length == 3) {
            List<DatapackRepository> repositories = this.getPlugin().getDatapacks();

            int packid = this.getPlugin().getDatapackNames().indexOf(args[0]);

            if (packid == -1) {
                sender.sendMessage(String.format("Error: Datapack \"%s\" Not Found", args[0]));
            } else {
                Map<String, String> newPackMap = repositories.get(packid).toMap();
                if (args[1].equals("name")) {
                    sender.sendMessage("Error: Can't Update Datapack Name");
                } else {
                    if (newPackMap.containsKey(args[1])) {
                        newPackMap.put(args[1], args[2]);
                        try {
                            DatapackRepository newPackRepository = DatapackRepository.toDPRepository(newPackMap);
                            repositories.set(packid, newPackRepository);
                            
                            result = true;
                            sender.sendMessage(String.format("Successfully Updated! Datapack \"%s.%s\" = \"%s\"", args[0], args[1], args[2]));
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage(String.format("Error: %s", e.getMessage()));
                        }
                    } else {
                        sender.sendMessage(String.format("Error: Not Found Attribute \"%s\"", args[1]));
                    }
                }
            }

        } else {
            sender.sendMessage("Error: Usage: /dpl-update <datapack_name> <attribute> <value>");
        }

        return result;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result;
        switch (args.length) {
            case 1:
                result = this.getPlugin().getDatapackNames();
                break;

            case 2:
                result = attributes;
                break;

            default:
                result = List.of();
                break;
        }
        return result;
    }

    @Override
    BaseCommand getInstance() {
        return this;
    }

    @Override
    public String getCommandName() {
        return "g4d-update";
    }
}