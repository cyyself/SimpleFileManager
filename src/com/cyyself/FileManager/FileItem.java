package com.cyyself.FileManager;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

class FileItem extends DefaultMutableTreeNode {
    public File file;
    FileItem(File newFile) {
        super(newFile.getName());
        file = newFile;
        if (file.isDirectory()) {
            add(new DefaultMutableTreeNode(""));
        }
    }
}