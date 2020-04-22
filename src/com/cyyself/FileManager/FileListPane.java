package com.cyyself.FileManager;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

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
                MainFrame.reDirectTo(getSelectedPath());
            }
            @Override
            public void treeCollapsed(TreeExpansionEvent treeExpansionEvent) {

            }
        });
        tree.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    boolean selected = MainFrame.dirView.tree.getSelectionPath() != null;
                    menu.open.setEnabled(selected);
                    menu.cut.setEnabled(selected);
                    menu.copy.setEnabled(selected);
                    menu.delete.setEnabled(selected);
                    menu.paste.setEnabled(!menu.copyFrom.isEmpty());
                    menu.encrypt.setEnabled(false);
                    menu.decrypt.setEnabled(false);
                    menu.unzip.setEnabled(false);
                    menu.zip.setEnabled(false);
                    if (selected) {
                        menu.zip.setEnabled(true);
                        File selFile = new File(getSelectedPath());
                        String filename = selFile.getName();
                        if (selFile.isFile()) {
                            String format = filename.substring(filename.lastIndexOf("."));
                            if (format.equals(".encrypted")) menu.decrypt.setEnabled(true);
                            else menu.encrypt.setEnabled(true);
                            if (format.equals(".zip")) menu.unzip.setEnabled(true);
                        }
                    }
                    menu.show(tree,mouseEvent.getX(),mouseEvent.getY());
                }
                else if (SwingUtilities.isLeftMouseButton(mouseEvent) && mouseEvent.getClickCount() == 2) {
                    File sel = new File(MainFrame.dirView.getSelectedPath());
                    if (sel.isFile()) {
                        try {
                            Desktop.getDesktop().open(sel);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("打开失败");
                        }
                    }
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
    String getSelectedPath() {
        TreePath tp = MainFrame.dirView.tree.getSelectionPath();
        if (tp == null) return "";
        String filename = tp.getLastPathComponent().toString();
        if (filename != null && !filename.isEmpty()) {
            String newPath = MainFrame.cur_Folder.getAbsolutePath() + File.separator + filename;
            return newPath;
        }
        return "";
    }
}