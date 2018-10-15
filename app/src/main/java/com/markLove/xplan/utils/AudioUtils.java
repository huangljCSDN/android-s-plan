package com.markLove.xplan.utils;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import com.markLove.xplan.base.App;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by luoyunmin on 2017/7/31.
 */

public class AudioUtils {
    public final static int MAX_VOICE_TIME = 60;
    public final static int COUNTDOWN_VOICE_TIME = 55;
    private MediaPlayer mediaPlayer;
    private static final String TAG = "RecorderUtil";

    private String mFileName = null;
    private MediaRecorder mRecorder = null;
    private long startTime;
    private long timeInterval;
    private boolean isRecording;
    private static AudioUtils audioUtils;

    private AudioUtils() {

    }

    public static AudioUtils getInstance() {
        if (null == audioUtils) {
            synchronized (AudioUtils.class) {
                if (null == audioUtils) {
                    audioUtils = new AudioUtils();
                }
            }
        }
        return audioUtils;
    }

    public void stop() {
        if (null != mediaPlayer) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    PlayStatusListener playStatusListener;

    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    public void play(String path, PlayStatusListener playStatusListener) {
        if (null != this.playStatusListener) {
            //this.playStatusListener.playEnd();
            this.playStatusListener = null;
            if (null != mediaPlayer) {
                stop();
            }
        }
        try {
            this.playStatusListener = playStatusListener;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (null != AudioUtils.this.playStatusListener) {
                        AudioUtils.this.playStatusListener.playEnd();
                        AudioUtils.this.playStatusListener = null;
                        stop();
                    }
                }
            });
            mediaPlayer.prepare();
            mediaPlayer.start();
            this.playStatusListener.playStart();
        } catch (IOException e) {
            if (null != this.playStatusListener) {
                this.playStatusListener.playEnd();
                this.playStatusListener = null;
            }
            e.printStackTrace();
        }
    }

    public interface PlayStatusListener {
        void playEnd();

        void playStart();
    }

    public interface AudioRecoderListener {
        void recoderFail();

        void recoderStart();

        void recoderEnd();

        void recoderCancel();
    }

    AudioRecoderListener audioRecoderListener;

    public boolean isRecording() {
        return isRecording;
    }


    /**
     * 开始录音
     */
    public void startRecording(AudioRecoderListener audioRecoderListener) {
        this.audioRecoderListener = audioRecoderListener;
        mFileName = App.getInstance().getExternalFilesDir("voice") + File.separator + System.currentTimeMillis() + ".amr";
        startTime = System.currentTimeMillis();
        File file = new File(mFileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (isRecording) {
            mRecorder.release();
            mRecorder = null;
        }
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
        }
        try {
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        try {
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setAudioChannels(1);
            mRecorder.setAudioSamplingRate(8000);
            mRecorder.setOutputFile(mFileName);
            mRecorder.prepare();
            mRecorder.start();
            isRecording = true;
            if (null != audioRecoderListener) {
                audioRecoderListener.recoderStart();
            }
        } catch (Exception e) {
            if (null != audioRecoderListener) {
                audioRecoderListener.recoderFail();
            }
            e.printStackTrace();
        }
    }


    /**
     * 停止录音
     */
    public void stopRecording() {
        if (mFileName == null) return;
        timeInterval = System.currentTimeMillis() - startTime;
        try {
            mRecorder.setOnErrorListener(null);
            mRecorder.setOnInfoListener(null);
            mRecorder.setPreviewDisplay(null);
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            isRecording = false;
            if (timeInterval < 1000) {
                File voiceFile = new File(getFilePath());
                if (voiceFile.exists()) {
                    voiceFile.delete();
                }
            }
            if (null != audioRecoderListener) {
                audioRecoderListener.recoderEnd();
                audioRecoderListener = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 取消语音
     */
    public synchronized void cancelRecording() {
        if (mRecorder != null) {
            try {
                if (null != audioRecoderListener) {
                    audioRecoderListener.recoderCancel();
                }
                mRecorder.release();
                mRecorder = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            File file = new File(mFileName);
            file.deleteOnExit();
        }

        isRecording = false;
    }

    /**
     * 获取录音文件
     */
    public byte[] getDate() {
        if (mFileName == null) return null;
        try {
            return readFile(new File(mFileName));
        } catch (IOException e) {
            Log.e(TAG, "read file error" + e);
            return null;
        }
    }

    /**
     * 获取录音文件地址
     */
    public String getFilePath() {
        return mFileName;
    }


    /**
     * 获取录音时长,单位秒
     */
    public long getTimeInterval() {
        return timeInterval;
    }

    public long getCurrentTimeInterval() {
        return (System.currentTimeMillis() - startTime)/1000;
    }


    /**
     * 将文件转化为byte[]
     *
     * @param file 输入文件
     */
    private static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }

    public void destory() {
        if (null != playStatusListener) {
            playStatusListener = null;
        }
        if (null != mediaPlayer) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (null != mRecorder) {
            mRecorder.release();
            mRecorder = null;
        }

    }
}
