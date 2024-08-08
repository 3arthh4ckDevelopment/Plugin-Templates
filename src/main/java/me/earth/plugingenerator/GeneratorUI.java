package me.earth.plugingenerator;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;

//TODO: rework the gui look
public class GeneratorUI {
    private Path outputPath = Path.of(System.getProperty("user.home") + File.separator + "Desktop");

    public void initializeUI() {
        FlatDarkLaf.setup();

        JFrame frame = new JFrame("Plugin Generator");
        frame.setSize(600, 400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        frame.setLocation(width / 4 - frame.getWidth() / 2, height / 4 - frame.getHeight() / 2);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createPanel(frame);
    }

    private void createPanel(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        // Output path
        JTextField pathField = new JTextField();
        pathField.setBounds(20, 20, 150, 25);
        pathField.setText(outputPath.toString());
        panel.add(pathField);

        JButton browseButton = new JButton("Browse");
        browseButton.setBounds(180, 20, 80, 25);
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setCurrentDirectory(new File(outputPath.toString()));
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                outputPath = fileChooser.getSelectedFile().toPath();
                pathField.setText(outputPath.toString());
            }
        });
        panel.add(browseButton);

        // Plugin name
        JLabel nameLabel = new JLabel("Plugin Name");
        nameLabel.setBounds(20, 60, 100, 25);
        panel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(120, 60, 150, 25);
        nameField.setText("ExamplePlugin");
        panel.add(nameField);

        // Plugin id
        JLabel idLabel = new JLabel("Plugin ID");
        idLabel.setBounds(20, 100, 100, 25);
        panel.add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(120, 100, 150, 25);
        idField.setText("exampleplugin");
        panel.add(idField);

        // Version
        JLabel versionLabel = new JLabel("Version");
        versionLabel.setBounds(20, 140, 100, 25);
        panel.add(versionLabel);

        JTextField versionField = new JTextField();
        versionField.setBounds(120, 140, 150, 25);
        versionField.setText("1.0.0");
        panel.add(versionField);

        // Group
        JLabel groupLable = new JLabel("Group");
        groupLable.setBounds(20, 180, 100, 25);
        panel.add(groupLable);

        JTextField groupField = new JTextField();
        groupField.setBounds(120, 180, 150, 25);
        groupField.setText("me.earth");
        panel.add(groupField);

        // Accesswidener
        JLabel accesswidenerLabel = new JLabel("AccessWidener");
        accesswidenerLabel.setBounds(20, 220, 100, 25);
        panel.add(accesswidenerLabel);

        JCheckBox accesswidenerCheckBox = new JCheckBox();
        accesswidenerCheckBox.setBounds(120, 220, 25, 25);
        panel.add(accesswidenerCheckBox);

        // Mixins
        JLabel mixinsLabel = new JLabel("Mixins");
        mixinsLabel.setBounds(20, 255, 100, 25);
        panel.add(mixinsLabel);

        JCheckBox mixinsCheckBox = new JCheckBox();
        mixinsCheckBox.setBounds(120, 255, 25, 25);
        panel.add(mixinsCheckBox);

        // External GitHub repo
        JLabel githubLabel = new JLabel("External GitHub repo");
        githubLabel.setBounds(160, 220, 150, 25);
        panel.add(githubLabel);

        JCheckBox githubField = new JCheckBox();
        githubField.setBounds(280, 220, 150, 25);
        panel.add(githubField);

        // Generate button
        JButton generateButton = new JButton("Generate");
        generateButton.setBounds(20, 295, 100, 25);
        generateButton.addActionListener(e -> {
            String name = nameField.getText();
            String id = idField.getText();
            String version = versionField.getText();
            String group = groupField.getText();
            boolean accessWidener = accesswidenerCheckBox.isSelected();
            boolean mixin = mixinsCheckBox.isSelected();
            boolean github = githubField.isSelected();

            Main.generatePlugin(name, id, version, group, outputPath.toString(), accessWidener, mixin, github);
        });
        panel.add(generateButton);

        // Dark mode toggle
        JToggleButton darkModeToggle = new JToggleButton("Dark Mode");
        darkModeToggle.setBounds(300, 20, 120, 25);
        panel.add(darkModeToggle);

        darkModeToggle.addActionListener(e -> {
            if (darkModeToggle.isSelected()) {
                darkModeToggle.setText("Light Mode");
                FlatLightLaf.setup();
            } else {
                darkModeToggle.setText("Dark Mode");
                FlatDarkLaf.setup();
            }
            SwingUtilities.updateComponentTreeUI(frame);
            frame.repaint();
        });

        frame.setVisible(true);
    }
}
