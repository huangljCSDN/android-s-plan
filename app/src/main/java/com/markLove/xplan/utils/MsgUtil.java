package com.markLove.xplan.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.markLove.xplan.ui.widget.LoadingDialog;

public class MsgUtil {
    public static LoadingDialog baDialog = null;
    public static final boolean isTest =true;
    private static Toast toast;

    /**
     * 显示提示信息
     * <p>
     * <br/> Author:chenyou
     *
     * @param text 提示内容
     */
    public static void showToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 测试阶段全局log
     *
     * @param TAG
     * @param msg
     */
    public static void showLog(String TAG, String msg) {
        if (isTest) {
            Log.d(TAG, "-------SctekRepay-------" + msg);
        }
    }

    /**
     * 显示提示信息(时间较长)
     * <p>
     * <br/> Author:chenyou
     *
     * @param text 提示内容
     */
    public static void showLongToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            toast.setText(text);
        }
//		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, DisplayUtil.dip2px(Application.context, 150));
        toast.show();
    }

    public static void showDialog(Context context) {
        if (baDialog == null) {
            baDialog = new LoadingDialog(context);
            baDialog.setCanceledOnTouchOutside(false);
            // baDialog.setOnKeyListener(keylistener);
        }
        try {
            baDialog.show();
        } catch (Exception e) {
            MsgUtil.showLog("aaaaaa", "----wait.show()失败-----" + e.toString());
        }
    }

    public static void closeDialog() {
        if (baDialog != null) {
            baDialog.dismiss();
            baDialog = null;
        }
    }
}
