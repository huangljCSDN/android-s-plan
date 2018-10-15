package com.markLove.xplan.bean.msg.body;

import android.util.Log;

import com.markLove.xplan.base.App;
import com.markLove.xplan.bean.msg.Message;
import com.markLove.xplan.utils.DataUtils;
import com.markLove.xplan.utils.ImageUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by luoyunmin on 2017/7/5.
 */

public class FileMessageBody extends MessageBody {
    //图片长度
    long fileLength;
    //图片名字
    String fileName;
    //图片路径
    String filePath;

    private String file;

    public FileMessageBody(Message.Type type, Message.ChatType chatType, String fileName, String path) {
        this.fileName = fileName;
        this.filePath = path;
        setChatType(chatType);
        setType(type);
        setDateTime(DataUtils.getDatetime());
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public byte[] toBytes() {
        try {
            File file = new File(filePath);
            fileLength = file.length();
            this.file = ImageUtils.encodeImage(filePath);
            if (chatType == Message.ChatType.IMAGE) {
                filePath = App.getInstance().getExternalFilesDir("img").getAbsolutePath() + File.separator + fileName;
            } else if (chatType == Message.ChatType.VOICE) {
                filePath = App.getInstance().getExternalFilesDir("voice").getAbsolutePath() + File.separator + fileName;
            }
        } catch (IOException e) {
            Log.e("lym", "exception");
            e.printStackTrace();
        }
        return super.toBytes();
    }
}
