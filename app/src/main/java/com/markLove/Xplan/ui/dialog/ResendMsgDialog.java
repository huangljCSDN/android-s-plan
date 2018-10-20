package com.markLove.Xplan.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.markLove.Xplan.R;

/**
 * 作者：created by huanglingjun on 2018/10/12
 * 描述：
 */
public class ResendMsgDialog extends AlertDialog implements View.OnClickListener {
    private OnMenuClickListener onMenuClickListener;

    public ResendMsgDialog(Context context) {
        super(context, R.style.DialogBottom);
    }

    public ResendMsgDialog setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.popup_chat_msg_fail);
        getWindow().setGravity(Gravity.BOTTOM);

        findViewById(R.id.popup_again).setOnClickListener(this);
        findViewById(R.id.popup_cancel).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_again:
                onMenuClickListener.onMenuClick();
                break;
        }
        this.dismiss();
    }

    public interface OnMenuClickListener {
        void onMenuClick();
    }

}
