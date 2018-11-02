package com.markLove.Xplan.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
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
		tv.setText("努力加载中...");
		ImageView imageView = this.findViewById(R.id.iv_anim_left);
		ImageView imageViewRight = this.findViewById(R.id.iv_anim_right);
		if (imageView.getDrawable() instanceof AnimationDrawable) {
			AnimationDrawable drawable = (AnimationDrawable) imageView.getDrawable();
			drawable.start();
		}
		if (imageViewRight.getDrawable() instanceof AnimationDrawable) {
			AnimationDrawable drawable = (AnimationDrawable) imageViewRight.getDrawable();
			drawable.start();
		}
		LinearLayout linearLayout = (LinearLayout) this
				.findViewById(R.id.LinearLayout);
		linearLayout.getBackground().setAlpha(210);
	}

}
