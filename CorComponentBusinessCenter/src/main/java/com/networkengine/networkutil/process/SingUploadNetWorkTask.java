package com.networkengine.networkutil.process;

import android.text.TextUtils;
import android.util.Log;

import com.networkengine.engine.LogicEngine;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by liuhao on 2017/6/9.
 */

public abstract class SingUploadNetWorkTask extends SingNetWorkTask {


    public abstract RequestBody setRequestBody();

    /**
     * 默认上传方法，子类可以重写该方法，实现上传的逻辑
     *
     * @param requestBody
     * @return
     */
    public String uploadFile(RequestBody requestBody) {
        if (requestBody == null) {
            return null;
        }
        if (mFileSubPackage == null) {
            return null;
        }
        try {
            return LogicEngine.getInstance().getFileTransController().uploadFiles(mFileSubPackage.getNetPath(), requestBody);
        } catch (IOException e) {
            return null;
        }
    }


    @Override
    public boolean doInBackground(final PublishProgress publishProgress) {
        if (mFileSubPackage == null)
            return false;

        RequestBody requestBody = setRequestBody();

        if (requestBody == null)
            return false;


        RequestBody fileRequestBody = new FileRequestBody(requestBody) {
            @Override
            public void loading(long current, boolean done) {
                mFileSubPackage.setPos(current);
                publishProgress.PublishProgress(current);
            }
        };
        String string = "";
        try {
            string = uploadFile(fileRequestBody);
        } catch (IllegalStateException e) {
            Log.e("error", "uploadFile >>>　" + e.getMessage());
            return false;
        }


        if (TextUtils.isEmpty(string))
            return false;
        mFileSubPackage.setNetResult(string);

        return true;
    }

    abstract class FileRequestBody extends RequestBody {
        private final RequestBody requestBody;
        private BufferedSink bufferedSink;

        public FileRequestBody(RequestBody requestBody) {
            this.requestBody = requestBody;
        }

        @Override
        public MediaType contentType() {
            return requestBody.contentType();
        }

        @Override
        public long contentLength() throws IOException {
            return requestBody.contentLength();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            if (bufferedSink == null) {
                bufferedSink = Okio.buffer(sink(sink));
            }
            requestBody.writeTo(bufferedSink);
            bufferedSink.flush();
        }

        private Sink sink(Sink sink) {
            return new ForwardingSink(sink) {
                private long current;
                private long total;

                @Override
                public void write(Buffer source, long byteCount) throws IOException {
                    if (isStop) {
                        source.close();
                        return;
                    }
                    super.write(source, byteCount);
                    if (total == 0) {
                        total = contentLength();
                    }
                    current += byteCount;
                    if (current <= total) {
                        loading(current, total == current);

                    }
                }
            };
        }

        public abstract void loading(long current, boolean done);
    }


}
