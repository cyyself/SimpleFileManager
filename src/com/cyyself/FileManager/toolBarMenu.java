package com.cyyself.FileManager;

import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class toolBarMenu extends JPopupMenu {
    public JMenuItem mkdir = new JMenuItem("新建文件夹");
    public JMenuItem cut = new JMenuItem("剪切");
    public JMenuItem copy = new JMenuItem("复制");
    public JMenuItem paste = new JMenuItem("粘贴");
    public JMenuItem delete = new JMenuItem("删除");
    public JMenuItem encrypt = new JMenuItem("加密");
    public JMenuItem decrypt = new JMenuItem("解密");
    static String copyFrom = "";
    static boolean isCut = false;
    toolBarMenu() {
        add(mkdir);
        add(cut);
        add(copy);
        add(paste);
        add(delete);
        add(encrypt);
        add(decrypt);
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
        encrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File toEncrypt = new File(MainFrame.dirView.getSelectedPath());
                try {
                    byte[] fileBytes = Files.readAllBytes(toEncrypt.toPath());
                    String passphrase = JOptionPane.showInputDialog("请输入用于加密的密码：");
                    if (passphrase != null) {
                        byte[] key = passphrase.getBytes(StandardCharsets.UTF_8);
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        md.update(fileBytes);
                        byte[] hash = md.digest();
                        byte[] toWrite = new byte[hash.length + fileBytes.length];
                        System.arraycopy(hash,0,toWrite,0,16);
                        System.arraycopy(fileBytes,0,toWrite,16,fileBytes.length);
                        //简单异或加密，加入md5校验以检验密码是否正确
                        for (int i=0;i<toWrite.length;i++) toWrite[i] ^= key[i % key.length];
                        File EncryptDst = new File(toEncrypt.getAbsolutePath() + ".encrypted");
                        Files.write(EncryptDst.toPath(),toWrite);
                        FileUtils.deleteQuietly(toEncrypt);
                    }
                } catch (IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "加密失败");
                }
                MainFrame.ChangeDirection(MainFrame.cur_Folder);
            }
        });
        decrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File toDecrypt = new File(MainFrame.dirView.getSelectedPath());
                try {
                    byte[] Encrypted = Files.readAllBytes(toDecrypt.toPath());
                    String passphrase = JOptionPane.showInputDialog("请输入用于解密的密码：");
                    if (passphrase != null) {
                        byte[] key = passphrase.getBytes(StandardCharsets.UTF_8);
                        //解密
                        for (int i=0;i<Encrypted.length;i++) Encrypted[i] ^= key[i % key.length];
                        byte[] fileBytes = new byte[Encrypted.length-16];
                        byte[] md5sum = new byte[16];
                        System.arraycopy(Encrypted,0,md5sum,0,16);
                        System.arraycopy(Encrypted,16,fileBytes,0,fileBytes.length);
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        md.update(fileBytes);
                        if (Arrays.equals(md5sum, md.digest())) {
                            String outFilename = toDecrypt.getAbsolutePath();
                            outFilename = outFilename.substring(0,outFilename.lastIndexOf("."));
                            File EncryptDst = new File(outFilename);
                            Files.write(EncryptDst.toPath(),fileBytes);
                            FileUtils.deleteQuietly(toDecrypt);
                        }
                        else JOptionPane.showMessageDialog(null, "密码不正确");
                    }
                } catch (IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "解密失败");
                }
                MainFrame.ChangeDirection(MainFrame.cur_Folder);
            }
        });
    }

}
