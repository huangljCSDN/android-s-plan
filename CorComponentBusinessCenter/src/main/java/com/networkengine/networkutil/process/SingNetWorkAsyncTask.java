package com.networkengine.networkutil.process;

import android.os.AsyncTask;


/**
 * Created by liuhao on 2017/7/28.
 */

public class SingNetWorkAsyncTask extends AsyncTask<Void, Long, Boolean> {
    private SingNetWorkTask singNetWorkTask;


    SingNetWorkAsyncTask(SingNetWorkTask singNetWorkTask) {
        this.singNetWorkTask = singNetWorkTask;
        initSingNetWorkTask();
    }

    private void initSingNetWorkTask() {

        singNetWorkTask.mFileSubPackage = singNetWorkTask.setFileSubPackage();
        singNetWorkTask.mSingNetFileTransferListener = singNetWorkTask.setSingNetFileTransferListener();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (singNetWorkTask.doInBackgroundBefore(new SingNetWorkTask.PublishProgress() {
            @Override
            public void PublishProgress(long size) {
                publishProgress(size);
            }
        })) {

            return true;
        }
        return singNetWorkTask.doInBackground(new SingNetWorkTask.PublishProgress() {
            @Override
            public void PublishProgress(long size) {
                publishProgress(size);
            }
        });
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        singNetWorkTask.doInBackgroundLater(aBoolean);
        singNetWorkTask.onPostExecute(aBoolean);

    }

    @Override
    protected void onProgressUpdate(Long... values) {
        singNetWorkTask.onProgressUpdate(values[0]);
    }
}
