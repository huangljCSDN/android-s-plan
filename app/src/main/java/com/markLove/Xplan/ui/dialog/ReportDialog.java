package com.markLove.Xplan.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.markLove.Xplan.R;

/**
 * 作者：created by huanglingjun on 2018/10/12
 * 描述：举报弹窗
 */
public class ReportDialog extends AlertDialog implements View.OnClickListener{

	public ReportDialog(Context context) {
		super(context, R.style.AsyncTaskDialog);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_report);
		findViewById(R.id.tv_one).setOnClickListener(this);
		findViewById(R.id.tv_two).setOnClickListener(this);
		findViewById(R.id.tv_three).setOnClickListener(this);
		findViewById(R.id.tv_four).setOnClickListener(this);
		findViewById(R.id.tv_five).setOnClickListener(this);
		findViewById(R.id.tv_cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.tv_one:
			case R.id.tv_two:
			case R.id.tv_three:
			case R.id.tv_four:
			case R.id.tv_five:
				String content = ((TextView)v).getText().toString();
				if (onDialogCallBack !=null){
					onDialogCallBack.onCallBack(content);
				}
				dismiss();
				break;
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
