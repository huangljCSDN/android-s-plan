package com.markLove.Xplan.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.markLove.Xplan.R;


/**
 * Created by Administrator on 2017/5/5.
 */

public class AlertLoveDialog extends Dialog {
    private AlertLoveDialog(Context context) {
        super(context);
    }

    private AlertLoveDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private AlertLoveDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        Context context;
        String title;//标题
        String msg;//消息内容
        String hint;//输入框提示语
        int inputType;//输入框输入类型
        int mInputLength = 6;

        String textLeft;//左边的按钮文字
        OnLeftClickListener onLeftClickListener;//左边按钮的监听

        String textRight;//右边的按钮文字
        OnRightClickListener onRightClickListener;//右边按钮监听


        String textRightEdit;//如果为输入框   按钮文字
        OnEditRightClickListener onEditRightClickListener;//输入框   右边按钮监听

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder setInputType(int inputType) {
            this.inputType = inputType;
            return this;
        }

        public Builder setOnLeftClickListener(String textLeft, OnLeftClickListener onLeftClickListener) {
            this.onLeftClickListener = onLeftClickListener;
            this.textLeft = textLeft;
            return this;
        }

        public Builder setOnRightClickListener(String textRight, OnRightClickListener onRightClickListener) {
            this.onRightClickListener = onRightClickListener;
            this.textRight = textRight;
            return this;
        }

        public Builder setOnEditRightClickListener(String textRightEdit, OnEditRightClickListener onEditRightClickListener) {
            this.onEditRightClickListener = onEditRightClickListener;
            this.textRightEdit = textRightEdit;
            return this;
        }

        public Builder setInputFilter(int length) {
            mInputLength = length;
            return this;
        }

        public AlertLoveDialog create() {
            AlertLoveDialog dialog = new AlertLoveDialog(context, R.style.alertDialog);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_alert_love, null);
            dialog.setContentView(view);

            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = (int) (display.getWidth()) - 100;
            Window dialogwindow = dialog.getWindow();
            dialogwindow.setAttributes(lp);
            dialogwindow.setGravity(Gravity.CENTER);
            dialog.setCanceledOnTouchOutside(false);
            init(view, dialog);
            return dialog;
        }

        private void init(View view, final Dialog dialog) {
            TextView tv_title = (TextView) view.findViewById(R.id.tv_alert_title);
            View v_title_bottom = view.findViewById(R.id.v_line);

            TextView tv_msg = (TextView) view.findViewById(R.id.tv_alert_message);

            TextView left = (TextView) view.findViewById(R.id.tv_alert_left);
            View v_left_right = (View) view.findViewById(R.id.v_alert_line);

            TextView right = (TextView) view.findViewById(R.id.tv_alert_right);

            TextView right_edit = (TextView) view.findViewById(R.id.tv_alert_right);
            final EditText et_msg = (EditText) view.findViewById(R.id.et_message);
            et_msg.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mInputLength)});
            if (!TextUtils.isEmpty(title)) {
                v_title_bottom.setVisibility(View.VISIBLE);
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(title);
            }

            if (!TextUtils.isEmpty(msg)) {
                tv_msg.setVisibility(View.VISIBLE);
                tv_msg.setText(msg);
            }

            if (!TextUtils.isEmpty(hint)) {
                et_msg.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        int index = et_msg.getSelectionStart() - 1;
                        if (index > 0) {
                            char c = s.charAt(index);
                            if (isEmojiCharacter(c)) {
                                Editable edit = et_msg.getText();
                                edit.delete(index, index + 1);
                            }
                        }
                    }
                });
                et_msg.setVisibility(View.VISIBLE);
                et_msg.setHint(hint);
            }
            if (inputType != 0) {
                if (null != et_msg) {
                    et_msg.setInputType(inputType);
                    et_msg.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }

            if (!TextUtils.isEmpty(textLeft)) {
                v_left_right.setVisibility(View.VISIBLE);
                left.setVisibility(View.VISIBLE);
                left.setText(textLeft);
                if (null == onLeftClickListener) {
                    left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onLeftClickListener.onClick(v);
                            dialog.dismiss();
                        }
                    });
                }
            }


            if (!TextUtils.isEmpty(textRight)) {
                right.setVisibility(View.VISIBLE);
                right.setText(textRight);
                if (null == onRightClickListener) {
                    right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onRightClickListener.onClick(v);
                            dialog.dismiss();
                        }
                    });
                }
            }

            if (!TextUtils.isEmpty(textRightEdit)) {
                right.setVisibility(View.VISIBLE);
                right.setText(textRightEdit);
                if (null == onEditRightClickListener) {
                    right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            et_msg.setText("");
                            dialog.dismiss();
                        }
                    });
                } else {
                    right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String trim = et_msg.getText().toString().trim();
                            onEditRightClickListener.onClick(v, trim);
                            et_msg.setText("");
                            dialog.dismiss();
                        }
                    });
                }
            }
        }
    }

    public interface OnLeftClickListener {
        void onClick(View v);
    }

    public interface OnRightClickListener {
        void onClick(View v);
    }

    public interface OnEditRightClickListener {
        void onClick(View v, String s);
    }

    /**
     * 去除字符串中的Emoji表情
     *
     * @param source
     * @return
     */
    private String removeEmoji(CharSequence source) {
        String result = "";
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (isEmojiCharacter(c)) {
                continue;
            }
            result += c;
        }
        return result;
    }


    /**
     * 判断一个字符串中是否包含有Emoji表情
     *
     * @param input
     * @return true 有Emoji
     */
    private boolean isEmojiCharacter(CharSequence input) {
        for (int i = 0; i < input.length(); i++) {
            if (isEmojiCharacter(input.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是Emoji 表情,抄的那哥们的代码
     *
     * @param codePoint
     * @return true 是Emoji表情
     */
    public static boolean isEmojiCharacter(char codePoint) {
        // Emoji 范围
        boolean isScopeOf = (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF) && (codePoint != 0x263a))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));

        return !isScopeOf;
    }

}
