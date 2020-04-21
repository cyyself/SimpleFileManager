package com.cyyself.FileManager;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.event.*;
import java.io.File;

class FileListPane extends JScrollPane {
    public JTree tree;
    public toolBarMenu menu = new toolBarMenu();
    FileListPane(File dir) {
        cd(dir);
    }
    void cd(File dir) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(dir.getAbsolutePath());
        for (File x:dir.listFiles()) {
            top.add(new FileItem(x));
        }
        tree = new JTree(top);
        tree.setRootVisible(false);
        JScrollPane that = this;
        tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent treeExpansionEvent) {
                String DirName = tree.getSelectionPath().getLastPathComponent().toString();
                String newPath = MainFrame.cur_Folder.getAbsolutePath() + File.separator + DirName;
                MainFrame.ChangeDirection(new File(newPath));
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent treeExpansionEvent) {

            }
        });
        tree.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    menu.show(tree,mouseEvent.getX(),mouseEvent.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
        setViewportView(tree);
    }
}