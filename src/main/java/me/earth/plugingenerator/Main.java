package me.earth.plugingenerator;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (System.console() != null) {
            validateInput(args);
        } else {
            GeneratorUI ui = new GeneratorUI();
            ui.initializeUI();
        }
    }

    public static void validateInput(String[] args) {
        String name = args[0].substring(0, 1).toUpperCase() + args[0].substring(1);
        if (name.isEmpty() || !name.matches("^[A-z][A-z0-9-_]{1,63}$")) {
            throw new IllegalArgumentException("Plugin name cannot be empty or contain spaces.");
        }

        String modID = args[1];
        if (modID == null || !modID.matches("^[a-z][a-z0-9-_]{1,63}$")) {
            throw new IllegalArgumentException("Mod ID doesn't match the regex.");
        }

        String version = args[2];
        if (version == null || version.isEmpty()
                || !version.matches("^(?<major>0|[1-9]\\d*)\\.(?<minor>0|[1-9]\\d*)\\.(?<patch>0|[1-9]\\d*)(?:-(?<prerelease>(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+(?<buildmetadata>[0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$")) {
            throw new IllegalArgumentException("Version must respect SemVer.");
        }

        String group = args[3].toLowerCase();
        if (!group.contains(".") || group.matches("^[a-z._]+$")) {
            throw new IllegalArgumentException("Group must be a valid package name.");
        }

        String path = args[4];
        if (path == null || path.isEmpty() || !Files.exists(Paths.get(path))) {
            throw new IllegalArgumentException("Invalid output path.");
        }

        boolean accessWidener = Boolean.parseBoolean(args[5]);
        boolean mixin = Boolean.parseBoolean(args[6]);
        boolean github = Boolean.parseBoolean(args[7]);

        generatePlugin(name, modID, version, group, path, accessWidener, mixin, github);
    }

    public static void generatePlugin(String name, String modID, String version, String group, String basePath, boolean accessWidener, boolean mixin, boolean github) {
        basePath += "/" + name + "/";

        try {
            if (Files.exists(Path.of(basePath))) {
                throw new IllegalArgumentException("Plugin already exists.");
            }
        } catch (IllegalArgumentException e) {
            showStackTrace(e);
            return;
        }

        // Create the plugin directory
        try {
            Files.createDirectories(Path.of(basePath));
        } catch (IOException e) {
            showStackTrace(e);
            return;
        }

        // Download the template
        Path zipPath = Path.of(basePath + "/Plugin-Templates-1.13.zip");
        FilesUtil.downloadFile(zipPath);

        // Unzip the template
        FilesUtil.unzip(zipPath, Path.of(basePath));

        try {
            FilesUtil.moveFilesAndDirectories(Path.of(basePath + "/Plugin-Templates-1.13-"));
            Files.delete(zipPath);
        } catch (IOException e) {
            showStackTrace(e);
            return;
        }

        // Delete GitHud related files
        deleteGitHubFiles(basePath, github);

        // Edit the gradle.properties file
        editGradlePropeties(name, modID, version, group, basePath);

        // Edit the build.gradle file
        editBuildGradle(basePath, accessWidener, mixin);

        // Edit the ExamplePluginConfig.json file
        editConfigFile(name, modID, group, basePath, accessWidener, mixin);

        // Edit the mixin file
        editMixinJson(modID, group, basePath, mixin);

        // Edit the accesswidener file
        editAccessWidener(modID, basePath, accessWidener);

        // Edit the fabric.mod.json file
        editFabricModJson(modID, basePath, accessWidener);

        // Refactor
        refactor(name, modID, group, basePath);

        System.out.println("Plugin generated successfully.");
    }

    private static void deleteGitHubFiles(String basePath, boolean github) {
        if (!github) {
            try {
                Files.delete(Path.of(basePath + "README.md"));
                Files.delete(Path.of(basePath + ".gitignore"));
                FilesUtil.deleteDirectoryContents(Path.of(basePath + ".github"));
            } catch (IOException e) {
                showStackTrace(e);
            }
        }
    }

    private static void editGradlePropeties(String name, String modID, String version, String group, String path) {
        try {
            Path propertiesFile = Paths.get(path, "gradle.properties");
            List<String> lines = Files.readAllLines(propertiesFile);

            for (String line : lines) {
                switch (line) {
                    case "plugin_version=1.0.0":
                        lines.set(lines.indexOf(line), "plugin_version=" + version);
                        break;
                    case "plugin_group=me.earth":
                        lines.set(lines.indexOf(line), "plugin_group=" + group);
                        break;
                    case "plugin_id=exampleplugin":
                        lines.set(lines.indexOf(line), "plugin_id=" + name);
                        break;
                    case "plugin_name=ExamplePlugin":
                        lines.set(lines.indexOf(line), "plugin_name=" + modID);
                        break;
                }
            }

            Files.write(propertiesFile, lines);
        } catch (IOException e) {
            showStackTrace(e);
        }
    }

    private static void editBuildGradle(String path, boolean accessWidener, boolean mixin) {
        try {
            Path buildFile = Paths.get(path, "build.gradle");
            List<String> lines = Files.readAllLines(buildFile);
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                if (!mixin && line.contains("MixinConfigs")) {
                    continue;
                } else if (!accessWidener && line.contains("accessWidenerPath")) { //TODO: remove the empty line
                    continue;
                }
                updatedLines.add(line);
            }

            Files.write(buildFile, updatedLines);
        } catch (IOException e) {
            showStackTrace(e);
        }
    }

    private static void editConfigFile(String name, String modID, String group, String path, boolean accessWidener, boolean mixin) {
        Path configFilePath = Paths.get(path, "src/main/resources/ExamplePluginConfig.json");

        // Edit the config file
        try {
            List<String> lines = Files.readAllLines(configFilePath);
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                if (line.contains("ExamplePlugin")) {
                    updatedLines.add(line.replace("ExamplePlugin", name));
                } else if (line.contains("me.earth.exampleplugin.PluginExample")) {
                    updatedLines.add(line.replace("me.earth.exampleplugin.PluginExample", group + "." + modID + "." + name));
                } else if (mixin && line.contains("mixins.exampleplugin.json")) {
                    updatedLines.add(line.replace("mixins.exampleplugin.json", "mixins." + modID + ".json"));
                } else if (accessWidener && line.contains("exampleplugin.accesswidener")) {
                    updatedLines.add(line.replace("exampleplugin.accesswidener", modID + ".accesswidener"));
                } else if (line.contains("{") || line.contains("}")) {
                    updatedLines.add(line);
                }
            }

            for (int i = 0; i < updatedLines.size(); i++) {
                if (i == updatedLines.size() - 2) {
                    updatedLines.set(i, updatedLines.get(i).replaceAll("\",", "\""));
                }
            }

            Files.write(configFilePath, updatedLines);
        } catch (IOException e) {
            showStackTrace(e);
        }

        // rename the file
        try {
            Files.move(configFilePath, configFilePath.resolveSibling(name + "Config.json"));
        } catch (IOException e) {
            showStackTrace(e);
        }
    }

    private static void editMixinJson(String modID, String group, String path, boolean mixin) {
        Path mixinPath = Paths.get(path, "src/main/resources/mixins.exampleplugin.json");
        try {
            if (mixin) {
                List<String> lines = Files.readAllLines(mixinPath);
                for (String line : lines) {
                    if (line.contains("me.earth.exampleplugin.mixin")) {
                        lines.set(lines.indexOf(line), line.replace("me.earth.exampleplugin.mixin", group + "." + modID + ".mixin"));
                    } else if (line.contains("mixins.exampleplugin.refmap.json")) {
                        lines.set(lines.indexOf(line), line.replace("mixins.exampleplugin.refmap.json", "mixins." + modID + ".refmap.json"));
                    }
                }

                Files.write(mixinPath, lines);
                Files.move(mixinPath, mixinPath.resolveSibling("mixins." + modID + ".json"));
            } else {
                Files.deleteIfExists(mixinPath);
            }
        } catch (IOException e) {
            showStackTrace(e);
        }
    }

    private static void editAccessWidener(String modID, String path, boolean accessWidener) {
        Path accessWidenerPath = Paths.get(path, "src/main/resources/exampleplugin.accesswidener");
        try {
            if (accessWidener) {
                Files.move(accessWidenerPath, accessWidenerPath.resolveSibling(modID + ".accesswidener"));
            } else {
                Files.deleteIfExists(accessWidenerPath);
            }
        } catch (IOException e) {
            showStackTrace(e);
        }
    }

    private static void editFabricModJson(String modID, String path, boolean accessWidener) {
        Path fabricModPath = Paths.get(path, "src/main/resources/fabric.mod.json");
        try {
            List<String> lines = Files.readAllLines(fabricModPath);
            for (String line : lines) {
                if (line.contains("\"id\": \"exampleplugin\"")) {
                    lines.set(lines.indexOf(line), line.replace("exampleplugin", modID));
                } else if (line.contains("accessWidener")) {
                    if (accessWidener) {
                        lines.set(lines.indexOf(line), line.replace("exampleplugin.accesswidener", modID + ".accesswidener"));
                    } else {
                        lines.set(lines.indexOf(line), "");
                    }
                }
            }

            Files.write(fabricModPath, lines.stream().filter(line -> !line.isEmpty()).toList());
        } catch (IOException e) {
            showStackTrace(e);
        }
    }

    private static void refactor(String name, String modID, String group, String path) {
        try {
            List<Path> javaFiles = new LinkedList<>();
            Files.walk(Paths.get(path, "src/main/java")).filter(Files::isRegularFile).forEach(javaFiles::add);

            for (Path javaFile : javaFiles) {
                List<String> lines = Files.readAllLines(javaFile);

                for (String line : lines) {
                    if (line.contains("me.earth.exampleplugin")) {
                        lines.set(lines.indexOf(line), line.replace("me.earth.exampleplugin", group + "." + modID));
                    } else if (line.contains("ExamplePlugin")) {
                        lines.set(lines.indexOf(line), line.replace("ExamplePlugin", name));
                    }
                }

                Files.write(javaFile, lines);
            }
        } catch (IOException e) {
            showStackTrace(e);
        }

        // change the package name
        try {
            final Path dirPath = Paths.get(path, "src/main/java/" + group.replace(".", "/") + "/" + modID);
            Files.createDirectories(dirPath);
            Files.move(Paths.get(path, "src/main/java/me/earth/exampleplugin"), dirPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            showStackTrace(e);
        }

        // Rename the PluginExample.java file
        try {
            Files.move(Paths.get(path, "src/main/java/" + group.replace(".", "/") + "/" + modID + "/PluginExample.java"), Paths.get(path, "src/main/java/" + group.replace(".", "/") + "/" + modID + "/" + name + ".java"));
        } catch (IOException e) {
            showStackTrace(e);
        }
    }

    private static void showStackTrace(Exception e) {
        if (System.console() != null) {
            e.printStackTrace();
        } else {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}