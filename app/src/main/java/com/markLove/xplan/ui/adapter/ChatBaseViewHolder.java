package com.markLove.xplan.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.markLove.xplan.R;
import com.markLove.xplan.bean.msg.Message;


/**
 * Created by luoyunmin on 2017/10/23.
 */

public abstract class ChatBaseViewHolder extends RecyclerView.ViewHolder {

    public View rootView;
    public View contentView;
    public CheckBox selectView;
    public ImageView userHead;
    public View status;

    public ChatBaseViewHolder(View itemView) {
        super(itemView);
        rootView = itemView;
        selectView = (CheckBox) itemView.findViewById(R.id.chat_select_view);
    }

    public void setStatus(Message.ChatStatus status) {
        switch (status) {
            case SENDING:
                sending();
                break;
            case SUCCESS:
                success();
                break;
            case FAIL:
                fail();
                break;
            case READ:
                read();
                break;
            case UNREAD:
                unRead();
                break;
            case REJECTED:
                rejected();
                break;
            default:
                break;
        }
    }

    public void read() {

    }

    public void unRead() {

    }

    public void rejected() {

    }

    public abstract void sending();

    public abstract void success();

    public abstract void fail();

    public abstract void setChatContent(Message msg);
}
