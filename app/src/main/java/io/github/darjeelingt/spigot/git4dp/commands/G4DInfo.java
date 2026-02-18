package io.github.darjeelingt.spigot.git4dp.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import io.github.darjeelingt.spigot.git4dp.Main;
import io.github.darjeelingt.spigot.git4dp.util.DatapackRepository;

final public class G4DInfo extends BaseCommand implements TabCompleter {
    public G4DInfo(Main plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean result = false;
        if (args.length == 1) {
            if (args[0] == null || args[0].isEmpty()) {
                sender.sendMessage("Error: Usage: /dpl-info <datapack_name>");
            } else {
                List<DatapackRepository> repositories = this.getPlugin().getDatapacks();
                boolean exist = false;
                for (DatapackRepository repository : repositories) {
                    if (repository.datapackname().equals(args[0])) {
                        exist = true;
                        sender.sendMessage(String.format("Datapack Name: \"%s\"", repository.datapackname()));
                        sender.sendMessage(String.format("Repository URL: \"%s\"", repository.remoteURI()));
                        sender.sendMessage(String.format("Repository Branch: \"%s\"", repository.branch()));
                        sender.sendMessage(String.format("Directory in Repository: \"%s\"", repository.directory()));
                        result = true;
                        break;
                    }
                }
                if (!exist) {
                    sender.sendMessage(String.format("Error: Datapack \"%s\" Not Found", args[0]));
                }
            }
        } else {
            sender.sendMessage("Error: Usage: /dpl-info <datapack_name>");
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
        return "g4d-info";
    }
}
