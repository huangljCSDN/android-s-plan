package com.markLove.Xplan.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.markLove.Xplan.R;

/**
 * 作者：created by huanglingjun on 2018/10/12
 * 描述：退出编辑弹窗
 */
public class ExitEditDialog extends AlertDialog implements View.OnClickListener{

	public ExitEditDialog(Context context) {
		super(context, R.style.AsyncTaskDialog);
		// TODO Auto-generated constructor stub
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_voice);
        TextView textView = findViewById(R.id.tv_content);
        textView.setText(getContext().getString(R.string.tip_exit_edit));
        findViewById(R.id.tv_sure).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_sure:
                if (onDialogCallBack != null){
                    onDialogCallBack.onCallBack("");
                }
                dismiss();
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

	public interface OnDialogCallBack{
		void onCallBack(String content);
	}

	public OnDialogCallBack onDialogCallBack;

	public void setOnDialogCallBack(OnDialogCallBack onDialogCallBack) {
		this.onDialogCallBack = onDialogCallBack;
	}
}
