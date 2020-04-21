package com.cyyself.FileManager;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class toolBarMenu extends JPopupMenu {
    public JMenuItem mkdir = new JMenuItem("新建文件夹");
    public JMenuItem cut = new JMenuItem("剪切");
    public JMenuItem copy = new JMenuItem("复制");
    public JMenuItem paste = new JMenuItem("粘贴");
    public JMenuItem delete = new JMenuItem("删除");
    toolBarMenu() {
        add(mkdir);
        add(cut);
        add(copy);
        add(paste);
        add(delete);
        mkdir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String name = JOptionPane.showInputDialog("请输入要创建的文件夹名称：");
                if (name != null && !name.isEmpty()) {
                    String path = MainFrame.cur_Folder.getAbsolutePath() + File.separator + name;
                    if (!new File(path).mkdir()) JOptionPane.showMessageDialog(null, "创建失败");
                    else MainFrame.ChangeDirection(MainFrame.cur_Folder);
                }
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                TreePath tp = MainFrame.dirView.tree.getSelectionPath();
                if (tp != null) {
                    String filename = tp.getLastPathComponent().toString();
                    if (filename != null && !filename.isEmpty()) {
                        String newPath = MainFrame.cur_Folder.getAbsolutePath() + File.separator + filename;
                        if (!delete(new File(newPath))) {
                            JOptionPane.showMessageDialog(null, "删除失败");
                        }
                    }
                }
            }
        });
    }
    static boolean delete(File file) {
        if (!file.delete()) {
            //避免了unix中软链接被直接删除，与rm -rf的操作相符合
            if (file.isDirectory()) {
                for (File x:file.listFiles()) {
                    delete(x);
                }
                return file.delete();
            }
            else return false;
        }
        else return true;
    }
}
