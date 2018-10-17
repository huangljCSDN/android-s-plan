package com.markLove.Xplan.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.markLove.Xplan.R;

/**
 * 作者：created by huanglingjun on 2018/10/12
 * 描述：
 */
public class MorePopWindow extends PopupWindow implements View.OnClickListener{

    private Context context;
    private LinearLayout ll_report, ll_exit;

    private String msg_one,msg_two;

    public MorePopWindow(Context context,String msg_one,String msg_two) {
        super(context);
        this.context = context;
        this.msg_one = msg_one;
        this.msg_two = msg_two;
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_more, null);
        ll_report = view.findViewById(R.id.ll_report);
        ll_exit = view.findViewById(R.id.ll_exit);

        TextView tvReport = view.findViewById(R.id.tv_report);
        TextView tvExit = view.findViewById(R.id.tv_exit);
        tvReport.setText(msg_one);
        tvExit.setText(msg_two);

        ll_report.setOnClickListener(this);
        ll_exit.setOnClickListener(this);
        setContentView(view);
        initWindow();
    }

    private void initWindow() {
        DisplayMetrics d = context.getResources().getDisplayMetrics();
//        this.setWidth((int) (d.widthPixels * 0.35));
//        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) context, 0.5f);//0.0-1.0
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) context, 1f);
            }
        });
    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void showAtBottom(View view) {
        //弹窗位置设置
        showAsDropDown(view, Math.abs((view.getWidth() - getWidth()) / 2), 10);
        //showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 10, 110);//有偏差
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_report:
                if (onDialogCallBack != null){
                    onDialogCallBack.onReportCallBack();
                }
                dismiss();
                break;
            case R.id.ll_exit:
                if (onDialogCallBack != null){
                    onDialogCallBack.onExitCallBack();
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface OnDialogCallBack{
        void onReportCallBack();
        void onExitCallBack();
    }

    public OnDialogCallBack onDialogCallBack;

    public void setOnDialogCallBack(OnDialogCallBack onDialogCallBack) {
        this.onDialogCallBack = onDialogCallBack;
    }
}
