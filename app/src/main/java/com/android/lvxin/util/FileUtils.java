package com.android.lvxin.util;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件相关工具类
 */
public class FileUtils {
    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                if (file.isDirectory()) {
                    File[] childFile = file.listFiles();
                    if ((childFile != null) && (childFile.length != 0)) {
                        for (File f : childFile) {
                            deleteFile(f);
                        }
                    }
                    file.delete();
                }
            }
        }
    }

    /**
     * 解压
     *
     * @param zipFilePath
     * @param targetDir
     * @return
     */
    public static boolean unzip(String zipFilePath, String targetDir) {
        try {
            FileInputStream is = new FileInputStream(zipFilePath);
            boolean b = unzip(is, targetDir);
            if (is != null) {
                is.close();
            }
            return b;
        } catch (Exception cwj) {
            return false;
        }
    }

    /**
     * 解压
     *
     * @param is
     * @param targetDir
     * @return
     */
    public static boolean unzip(InputStream is, String targetDir) {
        int bufferSize = 4096;
        new File(targetDir).mkdirs();

        try {
            ZipEntry entry;
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
            byte[] data = new byte[bufferSize];

            while ((entry = zis.getNextEntry()) != null) {

                try {
                    if (entry.isDirectory()) {
                        File folder = new File(
                                targetDir + File.separator + entry.getName());
                        folder.mkdirs();

                    } else {
                        File entryFile = new File(
                                targetDir + File.separator + entry.getName());

                        if (!entryFile.exists()) {
                            // entryFile.createNewFile();
                            FileOutputStream fos = new FileOutputStream(entryFile);
                            BufferedOutputStream dest = new BufferedOutputStream(fos, bufferSize);

                            int count;
                            while ((count = zis.read(data, 0, bufferSize)) != -1) {
                                dest.write(data, 0, count);
                            }

                            dest.flush();
                            dest.close();

                            fos.close();
                        }
                    }
                } catch (FileNotFoundException ex) {
                    // Log.e("unzip", "zip decompress write Error");
                    break;
                } catch (IOException ex2) {
                    // Log.e("unzip", "zip entry read Error");
                    break;
                }
            }

            if (zis != null) {
                zis.close();
            }
            return true;
        } catch (Exception cwj) {
            // Log.e("unzip", "open zip Error");
            return false;
        }
    }

    /**
     * 文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isExistFile(String filePath) {
        File srcFile = new File(filePath);
        return srcFile.exists();
    }

    /**
     * 判断给定的路径是否为空
     *
     * @param folderPath
     * @return
     */
    public static boolean isEmptyFolder(String folderPath) {
        if (StringUtils.isEmpty(folderPath)) {
            File file = new File(folderPath);
            return file.list().length == 0;
        }
        return true;
    }

    /**
     * 读取access目录下的文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context, String fileName) {
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) {
                Result += line;
            }
            bufReader.close();
            inputReader.close();
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (null != inputReader) {
                    inputReader.close();
                }

                if (null != bufReader) {
                    bufReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (null != inputReader) {
                    inputReader.close();
                }

                if (null != bufReader) {
                    bufReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return null;
    }
}
