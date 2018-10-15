package com.markLove.xplan.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.markLove.xplan.R;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/5/2.
 */

public class SelectDialog extends Dialog {
    private SelectDialog(Context context) {
        super(context);
    }

    private SelectDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private SelectDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public static class Builder {
        private WeakReference<Context> context;
        private String title;
        private String textOne;
        private OnOneClickListener onOneClickListener;
        private String textTwo;
        private OnTwoClickListener onTwoClickListener;
        private String textThree;
        private OnThrClickListener onThrClickListener;
        public Builder(Context context) {
            this.context = new WeakReference<Context>(context);
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setOnOneClickListener(String textOne,OnOneClickListener onOneClickListener) {
            this.onOneClickListener = onOneClickListener;
            this.textOne=textOne;
            return this;
        }

        public Builder setOnTwoClickListener(String textTwo,OnTwoClickListener onTwoClickListener) {
            this.onTwoClickListener = onTwoClickListener;
            this.textTwo = textTwo;
            return this;
        }

        public Builder setOnThrClickListener(String textThree,OnThrClickListener onThrClickListener) {
            this.onThrClickListener = onThrClickListener;
            this.textThree = textThree;
            return this;
        }

        public SelectDialog creat(){
            final SelectDialog selectDialog = new SelectDialog(context.get()
                    , R.style.DefaultDialog);
            View view= LayoutInflater.from(context.get()).inflate(R.layout.dialog_choose_photo,null);
            selectDialog.setContentView(view);
            TextView cancel_tv = (TextView) view.findViewById(R.id.cancel_tv);
            cancel_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectDialog.dismiss();
                }
            });
            WindowManager windowManager = (WindowManager) context.get().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = selectDialog.getWindow().getAttributes();
            lp.width = (int) (display.getWidth()-50);
            Window dialogwindow = selectDialog.getWindow();
            dialogwindow.setAttributes(lp);
            dialogwindow.setWindowAnimations(R.style.ButtomInOutAnim); // 设置窗口弹出动画
            dialogwindow.setGravity(Gravity.BOTTOM);
            selectDialog.setCanceledOnTouchOutside(true);

            if (!TextUtils.isEmpty(title)){
                TextView tv_title = (TextView) view.findViewById(R.id.takephoto_title);
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(title);
            }
            if (!TextUtils.isEmpty(textOne)){
                TextView one = (TextView) view.findViewById(R.id.one);
                one.setVisibility(View.VISIBLE);
                one.setText(textOne);
                one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null!=onOneClickListener){
                            onOneClickListener.onClick(v);
                        }
                        selectDialog.dismiss();
                    }
                });
            }

            if (!TextUtils.isEmpty(textTwo)){
                TextView two = (TextView) view.findViewById(R.id.two);
                two.setVisibility(View.VISIBLE);
                two.setText(textTwo);
                two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null!=onTwoClickListener){
                            onTwoClickListener.onClick(v);
                        }
                        selectDialog.dismiss();
                    }
                });
            }
            if (!TextUtils.isEmpty(textThree)){
                TextView thr = (TextView) view.findViewById(R.id.thr);
                thr.setVisibility(View.VISIBLE);
                thr.setText(textThree);
                thr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null!=onThrClickListener){
                            onThrClickListener.onClick(v);
                        }
                        selectDialog.dismiss();
                    }
                });
            }
            return selectDialog;
        }
    }

    public interface OnOneClickListener{
        void onClick(View v);
    }
    public interface OnTwoClickListener{
        void onClick(View v);
    }
    public interface OnThrClickListener{
        void onClick(View v);
    }
}
