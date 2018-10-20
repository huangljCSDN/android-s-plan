package com.markLove.Xplan.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.markLove.Xplan.R;

/**
 * 作者：created by huanglingjun on 2018/10/12
 * 描述：关注ta
 */
public class CareOtherDialog extends AlertDialog implements View.OnClickListener{

	public CareOtherDialog(Context context) {
		super(context, R.style.AsyncTaskDialog);
		// TODO Auto-generated constructor stub
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_care_other);
        findViewById(R.id.tv_sure).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_sure:
                if (onDialogCallBack != null){
                    onDialogCallBack.onCallBack(1);
                }
                dismiss();
            case R.id.tv_cancel:
                if (onDialogCallBack != null){
                    onDialogCallBack.onCallBack(2);
                }
                dismiss();
                break;
        }
    }

	public interface OnDialogCallBack{
		void onCallBack(int type);
	}

	public OnDialogCallBack onDialogCallBack;

	public void setOnDialogCallBack(OnDialogCallBack onDialogCallBack) {
		this.onDialogCallBack = onDialogCallBack;
	}
}
