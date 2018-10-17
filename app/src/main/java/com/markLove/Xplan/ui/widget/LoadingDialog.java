package com.markLove.Xplan.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markLove.Xplan.R;


public class LoadingDialog extends AlertDialog {
	private TextView tv;

	public LoadingDialog(Context context) {
		super(context, R.style.AsyncTaskDialog);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);
		tv = (TextView) this.findViewById(R.id.tv);
		tv.setText("加载中...");
		LinearLayout linearLayout = (LinearLayout) this
				.findViewById(R.id.LinearLayout);
		linearLayout.getBackground().setAlpha(210);
	}

}
