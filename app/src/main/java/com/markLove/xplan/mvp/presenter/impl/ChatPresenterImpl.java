package com.markLove.xplan.mvp.presenter.impl;

import android.app.Activity;

import com.markLove.xplan.base.App;
import com.markLove.xplan.bean.msg.Message;
import com.markLove.xplan.config.Constants;
import com.markLove.xplan.db.DBDao;
import com.markLove.xplan.eventbus.MessageEvent;
import com.markLove.xplan.eventbus.MessageStatusEvent;
import com.markLove.xplan.mvp.contract.ChatView;
import com.markLove.xplan.mvp.presenter.ChatPresenter;
import com.markLove.xplan.utils.LogUtils;
import com.markLove.xplan.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luoyunmin on 2017/6/29.
 */

public class ChatPresenterImpl implements ChatPresenter {
    ChatView chatView;

    public ChatPresenterImpl() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void getHistory(final int meID, final int userID) {

        Observable.create(new ObservableOnSubscribe<List<Message>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Message>> emitter) throws Exception {
                List<Message> messages = DBDao.getDbDao(App.getInstance()).queryChatMessage(meID, userID);
                emitter.onNext(messages);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<List<Message>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Message> messages) {

                        for (Message message : messages) {
                            LogUtils.d(",fromId=" + message.getFromID() + ",toId=" + message.getToID() + ",chatType=" + message.getChatType() + ",type=" + message.getType());
                        }

                        chatView.showHistoryMessage(messages);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void setView(ChatView view) {
        this.chatView = view;
    }

    @Override
    public void sendPicture(String fileName, String filePath) {

    }

    @Override
    public void addChatList(int meID, int toID, String headImgUrl, String nickName) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestory() {
        EventBus.getDefault().unregister(this);
        chatView = null;
    }

    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        Message msg = event.getMsg();
        if (msg.getFromID() != 0 && (msg.getType() == Message.Type.CHAT || msg.getType() == Message.Type.GROUPCHAT)) {
            if (msg.getToID() == PreferencesUtils.getInt(((Activity) chatView), Constants.ME_USER_ID)) {
                chatView.addOneMessage(msg);
            }
        }
    }

    @Subscribe
    public void onEventMainThread(MessageStatusEvent event) {

        chatView.updataMessage(event.getMsgID(), event.getState());
    }
}
