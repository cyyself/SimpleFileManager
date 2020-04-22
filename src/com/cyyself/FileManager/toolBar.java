package com.cyyself.FileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

class toolBar extends JPanel {
    public Button back_button = new Button("<");
    public Button forward_button = new Button(">");
    public Button ancestor_button = new Button("^");
    public Button cd_button = new Button("Browse");
    public JPanel button_group = new JPanel();
    public JTextField path_text;
    public JFrame rootFrame;
    public void setRootFrame(JFrame root) {
        rootFrame = root;
    }
    public toolBar(String path) {
        path_text = new JTextField(path);
        path_text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainFrame.ChangeDirection(new File(path_text.getText()));
            }
        });
        back_button.setEnabled(false);
        forward_button.setEnabled(false);
        ancestor_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String separator = File.separator;
                if (separator.equals("\\")) separator = "\\\\";
                String[] path = MainFrame.cur_Folder.getAbsolutePath().toString().split(separator);
                StringBuilder newPath = new StringBuilder();
                for (int i=0;i<path.length-1;i++) {
                    newPath.append(path[i]).append(File.separator);
                }
                MainFrame.reDirectTo(newPath.toString());
            }
        });
        cd_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(MainFrame.cur_Folder);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showOpenDialog(rootFrame) == JFileChooser.APPROVE_OPTION) {
                    MainFrame.ChangeDirection(chooser.getSelectedFile());
                }
            }
        });
        back_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File newFile = new File(MainFrame.history.get(MainFrame.history_idx - 1));
                if (newFile.isDirectory()) {
                    MainFrame.history_idx --;
                    MainFrame.ChangeDirection(newFile);
                }
            }
        });
        forward_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File newFile = new File(MainFrame.history.get(MainFrame.history_idx + 1));
                if (newFile.isDirectory()) {
                    MainFrame.history_idx ++;
                    MainFrame.ChangeDirection(newFile);
                }
            }
        });
        setLayout(new BorderLayout());
        button_group.setLayout(new GridLayout(1, 4));
        button_group.add(back_button);
        button_group.add(forward_button);
        button_group.add(ancestor_button);
        button_group.add(cd_button);
        add(button_group, BorderLayout.WEST);
        add(path_text, BorderLayout.CENTER);
    }
}