package com.networkengine.file;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by pengpeng on 17/4/14.
 */

public class FileManage {

    public static String dirSplit = "\\";//linux windows

//    private Context mContext;

    private static FileManage instance;

    public static FileManage getInstance(Context context) {

        if (instance == null) {
            instance = new FileManage();
        }
        return instance;
    }

//    public static FileManage getInstance(Context context){
//
//        if(instance == null){
//            instance = new FileManage(context);
//        }
//        return instance;
//    }


//    public FileManage(Context mContext) {
//        this.mContext = mContext;
//    }

    /**
     * 保存文件
     *
     * @param physicalPath 物理地址
     * @param istream      内容
     * @return
     */
    public boolean saveFileByPhysicalDir(String physicalPath, InputStream istream) {

        boolean flag = false;
        try {
            OutputStream os = new FileOutputStream(physicalPath);
            int readBytes = 0;
            byte buffer[] = new byte[8192];
            while ((readBytes = istream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, readBytes);
            }
            os.close();
            flag = true;
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 创建文件
     *
     * @param path 全路径
     * @return
     */
    public boolean createFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        File f = new File(path);
        if (!f.exists()) {
            try {
                return f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 创建文件夹
     *
     * @param dir
     * @return
     */
    public boolean createDirectory(String dir) {
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }
        return true;
    }


    /**
     * 保存文件
     *
     * @param physicalPath 绝对路径
     * @param content      内容
     */
    public void saveAsFileOutputStream(String physicalPath, String content) {
        File file = new File(physicalPath);
        boolean b = file.getParentFile().isDirectory();
        if (!b) {
            File tem = new File(file.getParent());
            // tem.getParentFile().setWritable(true);
            tem.mkdirs();// 创建目录
        }
        //Log.info(file.getParent()+";"+file.getParentFile().isDirectory());
        FileOutputStream foutput = null;
        try {
            foutput = new FileOutputStream(physicalPath);

            foutput.write(content.getBytes("UTF-8"));
            //foutput.write(content.getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                foutput.flush();
                foutput.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
        //Log.info("文件保存成功:"+ physicalPath);
    }


    /**
     * COPY文件
     *
     * @param srcFile String 原文件路径
     * @param desFile String 目标文件路径
     * @return boolean
     */
    public boolean copyToFile(String srcFile, String desFile) {
        File scrfile = new File(srcFile);
        if (scrfile.isFile() == true) {
            int length;
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(scrfile);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            File desfile = new File(desFile);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(desfile, false);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            desfile = null;
            length = (int) scrfile.length();
            byte[] b = new byte[length];
            try {
                fis.read(b);
                fis.close();
                fos.write(b);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            scrfile = null;
            return false;
        }
        scrfile = null;
        return true;
    }

    /**
     * COPY文件夹 复制文件夹
     *
     * @param sourceDir String 原始文件目录
     * @param destDir   String 目标目录
     * @return boolean
     */
    public boolean copyDir(String sourceDir, String destDir) {
        File sourceFile = new File(sourceDir);
        String tempSource;
        String tempDest;
        String fileName;
        File[] files = sourceFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            fileName = files[i].getName();
            tempSource = sourceDir + "/" + fileName;
            tempDest = destDir + "/" + fileName;
            if (files[i].isFile()) {
                copyToFile(tempSource, tempDest);
            } else {
                copyDir(tempSource, tempDest);
            }
        }
        sourceFile = null;
        return true;
    }

    /**
     * 删除指定目录及其中的所有内容。
     *
     * @param dir 要删除的目录
     * @return 删除成功时返回true，否则返回false。
     */
    public boolean deleteDirectory(File dir) {
        File[] entries = dir.listFiles();
        if (entries != null) {
            int sz = entries.length;
            for (int i = 0; i < sz; i++) {
                if (entries[i].isDirectory()) {
                    if (!deleteDirectory(entries[i])) {
                        return false;
                    }
                } else {
                    if (!entries[i].delete()) {
                        return false;
                    }
                }
            }
            //缓存跟目录不能删除
//            if (!dir.delete()) {
//                return false;
//            }
            return true;
        } else {
            return false;
        }
    }


    /**
     * 检查文件是否存在
     *
     * @param sFileName 文件名
     * @return boolean true - findItemIndex false - not findItemIndex
     */
    public boolean checkExist(String sFileName) {

        boolean result = false;

        try {
            File f = new File(sFileName);

            //if (f.exists() && f.isFile() && f.canRead()) {
            if (f.exists() && f.isFile()) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            result = false;
        }

        /* return */
        return result;
    }

    /**
     * 获得文件大小
     *
     * @param sFileName 文件名
     * @return long 文件不存在返回－1
     */
    public long getSize(String sFileName) {

        long lSize = 0;

        try {
            File f = new File(sFileName);

            //findItemIndex
            if (f.exists()) {
                if (f.isFile() && f.canRead()) {
                    lSize = f.length();
                } else {
                    lSize = -1;
                }
                //not findItemIndex
            } else {
                lSize = 0;
            }
        } catch (Exception e) {
            lSize = -1;
        }
    /* return */
        return lSize;
    }

    /**
     * File 删除文件
     *
     * @param sFileName File 文件名
     * @return boolean true - 删除成功 false - 删除失败
     */
    public boolean deleteFromName(String sFileName) {

        boolean bReturn = true;

        try {
            File oFile = new File(sFileName);

            //findItemIndex
            if (oFile.exists()) {
                //Delete File
                boolean bResult = oFile.delete();
                //Delete Fail
                if (bResult == false) {
                    bReturn = false;
                }
            }

        } catch (Exception e) {
            bReturn = false;
        }

        //return
        return bReturn;
    }

    /**
     * 文件解压
     *
     * @param sToPath  解压到的路径
     * @param sZipFile 文件名绝对路径
     */
    @SuppressWarnings("rawtypes")
    public void releaseZip(String sToPath, String sZipFile) throws Exception {

        if (null == sToPath || ("").equals(sToPath.trim())) {
            File objZipFile = new File(sZipFile);
            sToPath = objZipFile.getParent();
        }
        ZipFile zfile = new ZipFile(sZipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                continue;
            }

            OutputStream os =
                    new BufferedOutputStream(
                            new FileOutputStream(getRealFileName(sToPath, ze.getName())));
            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen = 0;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zfile.close();
    }

    /**
     * getRealFileName
     *
     * @param baseDir     Root Directory
     * @param absFileName absolute Directory File Name
     * @return java.io.File     Return file
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private File getRealFileName(String baseDir, String absFileName) throws Exception {

        File ret = null;
        List dirs = new ArrayList();
        StringTokenizer st = new StringTokenizer(absFileName, System.getProperty("file.separator"));
        while (st.hasMoreTokens()) {
            dirs.add(st.nextToken());
        }
        ret = new File(baseDir);
        if (dirs.size() > 1) {
            for (int i = 0; i < dirs.size() - 1; i++) {
                ret = new File(ret, (String) dirs.get(i));
            }
        }
        if (!ret.exists()) {
            ret.mkdirs();
        }
        ret = new File(ret, (String) dirs.get(dirs.size() - 1));
        return ret;
    }

    /**
     * copyFile
     *
     * @param srcFile    Source File
     * @param targetFile Target file
     */
    @SuppressWarnings("resource")
    public void copyFile(String srcFile, String targetFile) throws IOException {

        FileInputStream reader = new FileInputStream(srcFile);
        FileOutputStream writer = new FileOutputStream(targetFile);
        byte[] buffer = new byte[4096];
        int len;
        try {
            reader = new FileInputStream(srcFile);
            writer = new FileOutputStream(targetFile);

            while ((len = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (writer != null) writer.close();
            if (reader != null) reader.close();
        }
    }

    /**
     * renameFile
     *
     * @param srcFile    Source File
     * @param targetFile Target file
     */
    public void renameFile(String srcFile, String targetFile) throws IOException {
        try {
            copyFile(srcFile, targetFile);
            deleteFromName(srcFile);
        } catch (IOException e) {
            throw e;
        }
    }

    public void write(String tivoliMsg, String logFileName) {
        try {
            byte[] bMsg = tivoliMsg.getBytes();
            FileOutputStream fOut = new FileOutputStream(logFileName, true);
            fOut.write(bMsg);
            fOut.close();
        } catch (IOException e) {
            //throw the exception
        }
    }

    public void writeLog(String logFile, String batchId, String exceptionInfo) {

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.JAPANESE);

        Object args[] = {df.format(new Date()), batchId, exceptionInfo};

        String fmtMsg = MessageFormat.format("{0} : {1} : {2}", args);

        try {

            File logfile = new File(logFile);
            if (!logfile.exists()) {
                logfile.createNewFile();
            }

            FileWriter fw = new FileWriter(logFile, true);
            fw.write(fmtMsg);
            fw.write(System.getProperty("line.separator"));
            fw.flush();
            fw.close();
        } catch (Exception e) {
        }
    }

    public String readTextFile(String realPath) throws Exception {
        File file = new File(realPath);
        if (!file.exists()) {
            System.out.println("File not findItemIndex!");
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(realPath), "UTF-8"));
        String temp = "";
        String txt = "";
        while ((temp = br.readLine()) != null) {
            txt += temp;
        }
        br.close();
        return txt;
    }

    //删除目录
    public void deleteFile(File file) {
        if (file != null && file.exists()){
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }
}
