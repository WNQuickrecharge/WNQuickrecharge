package com.weijing.materialanimatedswitch.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;


public class FileUtil {

    private static FileUtil mInstance;

    public static FileUtil getInstance() {
        if (mInstance == null) {
            mInstance = new FileUtil();
        }
        return mInstance;
    }

    public void deleteFileForPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        if (file.isDirectory()) {
            deleteFileDirectory(file);
        } else {
            file.delete();
        }

    }

    public void deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        if (file.isDirectory()) {
            File[] filesList = file.listFiles();
            for (int i = 0; i < filesList.length; i++) {
                filesList[i].delete();
            }
        }

    }

    private void deleteFileDirectory(File file) {
        File[] filesList = file.listFiles();
        for (int i = 0; i < filesList.length; i++) {
            filesList[i].delete();
        }
        if (filesList.length == 0) {
            file.delete();
        }
    }

    /**
     * 获取指定目录下的所有文件大小
     */
    public String getFileSizeForPath(String path) {

        if (TextUtils.isEmpty(path)) {
            return "0B";
        }

        long fileSize = 0;

        File file = new File(path);

        if (file.isDirectory()) {
            fileSize = getFileDirectorySize(file);
        } else {
            fileSize = getFileSize(file);
        }

        return getFileSizeFormat(fileSize);
    }

    private long getFileDirectorySize(File file) {
        long size = 0;
        File[] filelist = file.listFiles();
        for (int i = 0; i < filelist.length; i++) {
            if (filelist[i].isDirectory()) {
                size += getFileDirectorySize(filelist[i]);
            } else {
                size += getFileSize(filelist[i]);
            }
        }
        return size;
    }

    @SuppressWarnings("resource")
    private long getFileSize(File file) {
        long size = 0;

        if (file.exists()) {
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                try {
                    size = in.available();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return 0;
            }

        }
        return size;
    }

    private String getFileSizeFormat(Long size) {

        double fileSize = size;

        long size_KB = 1024;
        long size_MB = 1024 * size_KB;
        long size_GB = 1024 * size_MB;
        String formatString;
        if (size == 0) {
            return "0B";
        }
        if (size < size_KB) {
            formatString = DoubleDP(fileSize, "#.00") + "B";
        } else if (size < size_MB) {
            formatString = DoubleDP(fileSize / size_KB, "#.00") + "KB";
        } else if (size < size_GB) {
            formatString = DoubleDP(fileSize / size_MB, "#.00") + "MB";
        } else {
            formatString = DoubleDP(fileSize / size_GB, "#.00") + "GB";
        }
        return formatString;
    }


    public static String DoubleDP(double number, String fm) {
        StringBuffer buffer = new StringBuffer();
        DecimalFormat df = new DecimalFormat(fm);
        if (number < 1.0 && number != 0) {
            buffer.append("0");
        }
        buffer.append(df.format(number));
        return buffer.toString();

    }
}
