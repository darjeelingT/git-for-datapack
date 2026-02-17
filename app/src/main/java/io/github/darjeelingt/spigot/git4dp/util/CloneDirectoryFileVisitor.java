package io.github.darjeelingt.spigot.git4dp.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class CloneDirectoryFileVisitor extends SimpleFileVisitor<Path> {
    public Path source;
    public Path target;
    public CloneDirectoryFileVisitor(Path sourcepath, Path targetpath) {
        super();
        this.source = sourcepath;
        this.target = targetpath;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
        throws IOException
    {
        Path targetdir = target.resolve(source.relativize(dir));

        try {
            Files.copy(dir, targetdir);
        } catch (FileAlreadyExistsException e) {
            if (!Files.isDirectory(targetdir)) {
                throw e;
            }
        }
        return FileVisitResult.CONTINUE;
    }

    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
        throws IOException
    {
        Files.copy(file, target.resolve(source.relativize(file)));
        return FileVisitResult.CONTINUE;
    }
}
