package io.github.darjeelingt.spigot.git4dp.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import io.github.darjeelingt.spigot.git4dp.Main;
import io.github.darjeelingt.spigot.git4dp.util.DatapackRepository;

final public class G4DConnect extends BaseCommand implements TabCompleter {
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
            case 3:
                newPack.put("name", args[0]);
                newPack.put("URI", args[1]);
                newPack.put("branch", args[2]);
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

                if (this.getPlugin().getDatapackNames().indexOf(newRepository.datapackname()) != -1) {// 同名のデータパックが登録されているかチェック
                    sender.sendMessage(String.format("Error: Datapack \"%s\" is already exists", newRepository.datapackname()));
                } else {


                    List<DatapackRepository> repositories = this.getPlugin().getDatapacks();
                    repositories.add(newRepository);

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
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of();
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
