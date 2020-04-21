package com.cyyself.FileManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;

class MainFrame extends JFrame{
    static File cur_Folder = new File(System.getProperty("user.dir"));
    static FileListPane dirView = new FileListPane(cur_Folder);
    static toolBar topBar = new toolBar(cur_Folder.getAbsolutePath());

    public static boolean ChangeDirection(File new_direction) {
        if (new_direction.isDirectory()) {
            cur_Folder = new_direction;
            topBar.path_text.setText(cur_Folder.getAbsolutePath());
            dirView.cd(new_direction);
            return true;
        }
        else {
            topBar.path_text.setText(cur_Folder.getAbsolutePath());
            System.out.println("ERROR: new_direction is not a directory.");
            return false;
        }
    }
    public MainFrame() {
        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);
        add(dirView, BorderLayout.CENTER);

        topBar.setRootFrame(this);
    }
    public static void main(String[] args) {
        MainFrame fm = new MainFrame();
        fm.setTitle("文件管理器");
        fm.setSize(800, 480);
        fm.setVisible(true);
    }
}


