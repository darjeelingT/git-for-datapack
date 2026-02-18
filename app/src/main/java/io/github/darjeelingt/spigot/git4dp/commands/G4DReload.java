package io.github.darjeelingt.spigot.git4dp.commands;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import io.github.darjeelingt.spigot.git4dp.Main;
import io.github.darjeelingt.spigot.git4dp.util.CloneDirectoryFileVisitor;
import io.github.darjeelingt.spigot.git4dp.util.DatapackRepository;
import io.github.darjeelingt.spigot.git4dp.util.RemoveDirectoryFileVisitor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

final public class G4DReload extends BaseCommand implements TabCompleter {

    public G4DReload(Main plugin) {
        super(plugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean result = false;
        Path worlds = this.getPlugin().getWorldDataDirectory();
        FileConfiguration config = this.getPlugin().getCustomConfig();

        List<DatapackRepository> repositories = this.getPlugin().getDatapacks();

        Path workspacepath = worlds.resolve(
                config.getString("workspace-path")
            ).normalize();
        
        Path datapackspath = worlds.resolve(
                config.getString("datapacks-path")
            ).normalize();
        

        if (args.length == 0) {
            String token = System.getenv(
                    config.getString("token-env")
                );
            for (DatapackRepository repository : repositories) {
                try {
                    reloadDatapack(repository, workspacepath, datapackspath, token);
                    
                    sender.sendMessage(String.format("Reloaded \"%s\"!", repository.datapackname()));
                } catch (RuntimeException e) {
                    sender.sendMessage(String.format("Error: %s", e.getMessage()));
                    e.printStackTrace();
                }
            }
            
            sender.sendMessage("Reloading World Data...");
            this.getPlugin().getServer().reloadData();

            result = true;
        } else if (args[0] == null || args[0].isEmpty()) {
            sender.sendMessage("Error: Usage: /dpl-reload [datapack_name]");
        } else {
            String token = System.getenv(
                    config.getString("token-env")
                );

            for (String packName : args) {
                int packid = this.getPlugin().getDatapackNames().indexOf(packName);

                if (packid == -1) {
                    sender.sendMessage(String.format("Error: Datapack \"%s\" Not Found", packName));
                } else {
                    DatapackRepository repository = repositories.get(packid);
                    try {
                        reloadDatapack(repository, workspacepath, datapackspath, token);
                        
                        sender.sendMessage(String.format("Reloaded \"%s\"!", repository.datapackname()));

                    } catch (RuntimeException e) {
                        sender.sendMessage(String.format("Error: %s", e.getMessage()));
                    }
                }
            }
            sender.sendMessage("Reloading World Data...");
            this.getPlugin().getServer().reloadData();

            result = true;
        }

        return result;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return this.getPlugin().getDatapackNames();
    }

    void reloadDatapack(DatapackRepository repository, Path workspacePath, Path datapacksPath, String token) {
        Path repositorypath = workspacePath.resolve(repository.getRepositoryName());

        try {
            loadRepository(repository.remoteURI(), repositorypath, repository.branch(), token);
        } catch (RuntimeException e) {
            throw e;
        }

        Path packFolderDir = repositorypath.resolve(repository.directory()).normalize();

        Path datapackDir = datapacksPath.resolve(repository.datapackname()).normalize();

        try {
            cloneFolder(packFolderDir, datapackDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed Copying Datapack!", e);
        }
    }

    void loadRepository(URI remoteURI, Path repositorypath, String branch, String token)
        throws RuntimeException
    {
        File repositorydir = repositorypath.toFile();

        File gitDir = repositorypath.resolve(Constants.DOT_GIT).toFile();

        try {
            URIish uri = new URIish(remoteURI.toURL());
            
            Git git;
            if (Files.notExists(repositorypath)) {// ワークスペースがない場合は git clone
                repositorydir.mkdirs();
                git = Git.cloneRepository()
                    .setDirectory(repositorydir)
                    .setURI(uri.toString())
                    .setBranch(branch)
                    .setCredentialsProvider(
                        new UsernamePasswordCredentialsProvider(token, "")
                    )
                    .call();// throws GitAPIException
                
            } else {// ワークスペースがある場合は取得する
                Repository repo = 
                    new FileRepositoryBuilder()
                        .setGitDir(gitDir)
                        .readEnvironment()
                        .findGitDir()
                        .build();// throws IOException
                git = new Git(repo);

                try {
                    List<RemoteConfig> remotes = 
                        git.remoteList()
                            .call();// throws GitAPIException

                    if (remotes.isEmpty()) {
                        git.remoteAdd()
                            .setName(Constants.DEFAULT_REMOTE_NAME)
                            .setUri(uri)
                            .call();// throws GitAPIException
                    }


                    git.pull()
                        .setRemote(Constants.DEFAULT_REMOTE_NAME)
                        .setRemoteBranchName(branch)
                        .setCredentialsProvider(
                            new UsernamePasswordCredentialsProvider(token, "")
                        )
                        .call();
                    
                } catch (GitAPIException e) {
                    throw new RuntimeException("Failed Requesting Git!", e);
                } finally {
                    git.close();
                }
            }
            
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URI!", e);

        } catch (IllegalStateException e) {
            throw new RuntimeException("Failed Calling API!", e);
        } catch (IOException e) {
            throw new RuntimeException("Failed Connecting Workspace!", e);
        } catch (GitAPIException e) {
            throw new RuntimeException("Failed Requesting Git!", e);
        }
    }

    void cloneFolder(Path source, Path target)
        throws IOException
    {
        if (Files.exists(target)) {
            Files.walkFileTree(target, new RemoveDirectoryFileVisitor());
        }
        Files.createDirectory(target);
        Files.walkFileTree(source, new CloneDirectoryFileVisitor(source, target));
    }
    
    @Override
    BaseCommand getInstance() {
        return this;
    }

    @Override
    public String getCommandName() {
        return "g4d-reload";
    }
}