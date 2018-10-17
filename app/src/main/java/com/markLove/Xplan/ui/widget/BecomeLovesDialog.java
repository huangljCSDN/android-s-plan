package com.markLove.Xplan.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markLove.Xplan.R;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/5/5.
 */

public class BecomeLovesDialog extends Dialog {

    private BecomeLovesDialog(Context context) {
        super(context);
    }

    private BecomeLovesDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BecomeLovesDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public interface DialogViewOnClick {
        void onClick();
    }

    public static class Builder {
        WeakReference<Context> context;
        private View contentView;
        SparseArray<DialogViewOnClick> onClickListenerSparseArray = new SparseArray<>();

        public Builder(Context context) {
            this.context = new WeakReference<Context>(context);
        }

        public Builder setContentView(@LayoutRes int layoutID) {
            this.contentView = LayoutInflater.from(context.get()).inflate(layoutID, null);
            return this;
        }

        public Builder setTextViewStyle(@IdRes int resID, String text, int size, int color, int visible) {
            TextView textView = (TextView) contentView.findViewById(resID);
            if (visible == View.VISIBLE) {
                if (null != text) {
                    textView.setText(text);
                }
                if (size >= 0) {
                    textView.setTextSize(size);
                }
                if (color != -1)
                    textView.setTextColor(color);
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(visible);
            }
            return this;
        }

        public Builder setImageViewStyle(@IdRes int resID, int src, int visible) {
            ImageView imageView = (ImageView) contentView.findViewById(resID);
            if (visible == View.VISIBLE) {
                imageView.setImageResource(src);
                imageView.setVisibility(View.VISIBLE);
            }
            return this;
        }

        public Builder setViewVisible(@IdRes int resID, int visible) {
            contentView.findViewById(resID).setVisibility(visible);
            return this;
        }

        public Builder setOnClickListener(@IdRes int resID, DialogViewOnClick onClick) {
            onClickListenerSparseArray.put(resID, onClick);
            return this;
        }

        public BecomeLovesDialog create() {
            final BecomeLovesDialog dialog = new BecomeLovesDialog(context.get(), R.style.red_dialog);
            dialog.addContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            for (int i = 0; i < onClickListenerSparseArray.size(); i++) {
                int key = onClickListenerSparseArray.keyAt(i); // get the object by the key. Object obj = sparseArray.get(key);
                final DialogViewOnClick value = onClickListenerSparseArray.get(key);
                contentView.findViewById(key).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != value) {
                            value.onClick();
                        }
                        dialog.dismiss();
                    }
                });
            }

//            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//            Display display = windowManager.getDefaultDisplay();
//            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//            lp.width = (int) (display.getWidth());
//            Window dialogwindow = dialog.getWindow();
//            dialogwindow.setAttributes(lp);
//            dialogwindow.setWindowAnimations(R.style.ButtomInOutAnim); // 设置窗口弹出动画
//            dialogwindow.setGravity(Gravity.CENTER);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

    public void isShowLeft(int visible) {

    }

    public void isShowRight(int visible) {

    }

    public interface OnLeftClickListener {
        void onClick(View v);
    }

    public interface OnRightClickListener {
        void onClick(View v);
    }


}
