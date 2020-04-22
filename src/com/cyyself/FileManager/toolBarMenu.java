package com.cyyself.FileManager;

import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class toolBarMenu extends JPopupMenu {
    public JMenuItem mkdir = new JMenuItem("新建文件夹");
    public JMenuItem cut = new JMenuItem("剪切");
    public JMenuItem copy = new JMenuItem("复制");
    public JMenuItem paste = new JMenuItem("粘贴");
    public JMenuItem delete = new JMenuItem("删除");
    static String copyFrom = "";
    static boolean isCut = false;
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
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String Path = MainFrame.dirView.getSelectedPath();
                if (!Path.isEmpty()) {
                    isCut = true;
                    copyFrom = Path;
                }
            }
        });
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String Path = MainFrame.dirView.getSelectedPath();
                if (!Path.isEmpty()) {
                    isCut = false;
                    copyFrom = Path;
                }
            }
        });
        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File src = new File(copyFrom);
                File dst = new File(MainFrame.dirView.getSelectedPath());
                if (!dst.isDirectory()) dst = MainFrame.cur_Folder;
                if (!dst.isDirectory()){
                    JOptionPane.showMessageDialog(null, "目标不存在");
                    return;
                }
                if (src.isFile()) {
                    try {
                        FileUtils.copyFileToDirectory(src,dst);
                        if (isCut) if (!FileUtils.deleteQuietly(src)) {
                            JOptionPane.showMessageDialog(null, "删除失败");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "复制失败");
                    }
                }
                else if (src.isDirectory()) {
                    dst = new File(dst.getAbsolutePath() + File.separator + src.getName());
                    try {
                        FileUtils.copyDirectory(src,dst);
                        if (isCut) if (!FileUtils.deleteQuietly(src)) {
                            JOptionPane.showMessageDialog(null, "删除失败");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "复制失败");
                    }
                }
                else JOptionPane.showMessageDialog(null, "源文件不存在");
                MainFrame.ChangeDirection(MainFrame.cur_Folder);
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String Path = MainFrame.dirView.getSelectedPath();
                if (!Path.isEmpty()) {
                    File toDelete = new File(Path);
                    if (!FileUtils.deleteQuietly(toDelete)) {
                        JOptionPane.showMessageDialog(null, "删除失败");
                    }
                }
                MainFrame.ChangeDirection(MainFrame.cur_Folder);
            }
        });
    }

}
