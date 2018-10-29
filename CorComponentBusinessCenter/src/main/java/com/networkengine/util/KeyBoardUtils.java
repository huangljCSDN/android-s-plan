package com.networkengine.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 打开或关闭软键盘
 *
 * @author zhy
 */
public class KeyBoardUtils {
    /**
     * 打开软键盘
     *
     * @param view     输入框
     * @param mContext 上下文
     */
    public static void openKeybord(View view, Context mContext) {
        try {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && view != null) {
                view.requestFocus();
                imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        } catch (Exception e) {
            Log.e("hh", "openKeybord >>> " + e.getMessage());
        }
    }

    /**
     * 关闭软键盘
     *
     * @param view    输入框
     * @param context 上下文
     */
    public static void closeKeybord(View view, Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && view != null) {
                if (view.getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            Log.e("hh", "closeKeybord >>> " + e.getMessage());
        }
    }
}
