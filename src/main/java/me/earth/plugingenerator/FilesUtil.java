package me.earth.plugingenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FilesUtil {

    public static void downloadFile(Path outputPath) {
        String repoUrl = "https://github.com/3arthh4ckDevelopment/Plugin-Templates/archive/refs/heads/master.zip";

        try {
            downloadRepository(repoUrl, outputPath);
            System.out.println("Repository downloaded successfully.");
        } catch (IOException e) {
            System.err.println("Error downloading repository: " + e.getMessage());
        }
    }

    private static void downloadRepository(String urlString, Path outputFilePath) throws IOException {
        URL website = new URL(urlString);
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(website.openStream());
             WritableByteChannel writableByteChannel = Files.newByteChannel(outputFilePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

            final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            while (readableByteChannel.read(buffer) != -1) {
                buffer.flip();
                writableByteChannel.write(buffer);
                buffer.compact();
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                writableByteChannel.write(buffer);
            }
        }
    }

    public static void unzip(Path source, Path target) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path entryPath = target.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    Files.copy(zis, entryPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void moveFilesAndDirectories(Path sourceDir) throws IOException {
        if (!Files.isDirectory(sourceDir)) {
            System.out.println("Source is not a directory.");
            return;
        }

        Path targetDir = sourceDir.getParent();
        if (targetDir == null) {
            System.out.println("Source directory does not have a parent.");
            return;
        }

        Files.walkFileTree(sourceDir, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                // Skip the source directory
                if (!dir.equals(sourceDir)) {
                    Files.createDirectories(targetDir.resolve(sourceDir.relativize(dir)));
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.move(file, targetDir.resolve(sourceDir.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (!dir.equals(sourceDir)) {
                    Files.delete(dir);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        Files.deleteIfExists(sourceDir);
    }

    public static void deleteDirectoryContents(Path directory) throws IOException {
        if (!Files.isDirectory(directory)) {
            System.out.println("The provided path is not a directory.");
            return;
        }

        Files.walkFileTree(directory, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (!dir.equals(directory)) { // Don't delete the root directory
                    Files.delete(dir);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        Files.delete(directory);
    }
}