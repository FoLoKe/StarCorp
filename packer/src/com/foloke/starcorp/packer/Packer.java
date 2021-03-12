package com.foloke.starcorp.packer;

import com.foloke.starcorp.packer.Packers.ObjectsPacker;
import com.foloke.starcorp.packer.Packers.ObjectsPackerAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Packer {
    static String src = "D:\\Packer\\src";
    static String dst = "D:\\Packer\\packed";

    public static void main(String[] args) throws IOException {
        //PACK SHIPS
        String module = "ships";
        ObjectsPacker packer = new ObjectsPackerAdapter<>(PShip[].class);
        packModule(module, packer);

        module = "items";
        packer = new ObjectsPackerAdapter<>(PItem[].class);
        packModule(module, packer);
    }

    private static void packModule(String module, ObjectsPacker objectsPacker) throws IOException {
        File srcDir = new File(src + "\\" + module);
        if(!srcDir.exists()) {
            srcDir.mkdir();
            System.out.println("no " + module + " to pack (new dir created)");
        } else {
            File dstDir = new File(dst + "\\" + module);
            if(!dstDir.exists()) {
                dstDir.mkdir();
            }

            List<File> subModules = getSubFolders(srcDir);
            if(subModules.size() > 0) {
                for (File subModule : subModules) {
                    if(subModule.listFiles().length == 0) {
                        System.out.println("there is no " + module + " to pack in \"" + subModule.getName() + " \"");
                        continue;
                    }
                    File subModuleDst = new File(dstDir.getAbsoluteFile() + "\\" + subModule.getName());
                    if(subModuleDst.exists()) {
                        delete(subModuleDst);
                    }
                    subModuleDst.mkdir();

                    objectsPacker.pack(subModule, subModuleDst);
                }
            } else {
                System.out.println("there is nothing to pack in ships");
            }
        }
    }

    private static void delete(File file) throws IOException {

        for (File childFile : file.listFiles()) {

            if (childFile.isDirectory()) {
                delete(childFile);
            } else {
                if (!childFile.delete()) {
                    throw new IOException();
                }
            }
        }

        if (!file.delete()) {
            throw new IOException();
        }
    }

    private static List<File> getSubFolders(File file) {
        File[] subFiles = file.listFiles();
        List<File> folders = new ArrayList<>();

        for(File subFile : subFiles) {
            if (subFile.isDirectory()) {
                folders.add(subFile);
            }
        }

        return folders;
    }
}
