package com.markLove.Xplan.ui.widget;

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
public class RemoveMsgDialog extends AlertDialog implements View.OnClickListener {
    private OnMenuClickListener onMenuClickListener;

    public RemoveMsgDialog(Context context) {
        super(context, R.style.DialogBottom);
    }

    public RemoveMsgDialog setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_remove_msg);
        getWindow().setGravity(Gravity.BOTTOM);

        findViewById(R.id.tvRemove).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRemove:
                onMenuClickListener.onMenuClick();
                break;
        }
        this.dismiss();
    }

    public interface OnMenuClickListener {
        void onMenuClick();
    }

}
