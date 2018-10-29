package com.xsimple.im.control.iable;


import com.xsimple.im.control.listener.MediaPlayerListener;

/**
 * Created by liuhao on 2017/4/13.
 */

public interface IPlayMediaControl {

    boolean playMedia(long localId, String path);

    void setMediaPlayerListener(MediaPlayerListener mediaPlayerListener);

    void playNextMedia(int posstion);

    boolean isPlaying();

    void stopMedia();


}
