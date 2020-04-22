package com.cyyself.FileManager;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class MainFrame extends JFrame{
    static File cur_Folder = new File(System.getProperty("user.dir"));
    static toolBar topBar = new toolBar(cur_Folder.getAbsolutePath());
    static FileListPane dirView = new FileListPane(cur_Folder);
    static ArrayList<String> history = new ArrayList<String>();
    static int history_idx = 0;
    public static boolean ChangeDirection(File new_direction) {
        if (new_direction.isDirectory()) {
            cur_Folder = new_direction;
            topBar.path_text.setText(cur_Folder.getAbsolutePath());
            dirView.cd(cur_Folder);
            topBar.back_button.setEnabled(history_idx > 0);
            topBar.forward_button.setEnabled(history_idx < history.size() - 1);
            return true;
        }
        else {
            topBar.path_text.setText(cur_Folder.getAbsolutePath());
            System.out.println("ERROR: new_direction is not a directory.");
            return false;
        }
    }
    public static boolean reDirectTo(String newPath) {
        while (history.size() > history_idx + 1) {
            history.remove(history.size() - 1);
        }
        File newFile = new File(newPath);
        if (newFile.isDirectory()) {
            history.add(newFile.getAbsolutePath());
            history_idx++;
            ChangeDirection(newFile);
            return true;
        }
        return false;
    }
    public MainFrame() {
        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);
        add(dirView, BorderLayout.CENTER);
        history.add(cur_Folder.getAbsolutePath());
        topBar.setRootFrame(this);
    }
    public static void main(String[] args) {
        MainFrame fm = new MainFrame();
        fm.setTitle("文件管理器");
        fm.setSize(800, 480);
        fm.setVisible(true);
    }
}


