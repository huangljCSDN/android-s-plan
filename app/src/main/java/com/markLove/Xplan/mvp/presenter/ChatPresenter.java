package com.markLove.Xplan.mvp.presenter;


import com.markLove.Xplan.mvp.contract.ChatView;

/**
 * Created by luoyunmin on 2017/6/29.
 */

public interface ChatPresenter extends Presenter {

    void getHistory(int meID, int userID);

    void setView(ChatView view);

    void sendPicture(String fileName, String filePath);

    void addChatList(int meID, int toID, String headImgUrl, String nickName);

}
