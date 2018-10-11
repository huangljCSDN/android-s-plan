package com.markLove.xplan.widget;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.view.Gravity;
import android.view.View;

import com.markLove.xplan.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 选择相片的dialog
 * huanglingjun 2017/11/9.
 */
public class ChoosePicDialog extends DialogFragment implements View.OnClickListener {
    private OnMenuClickListener onMenuClickListener;

    public ChoosePicDialog setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.DialogBottom);
        dialog.setContentView(R.layout.dialog_choose_pic_from_album);
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        dialog.findViewById(R.id.tvGallery).setOnClickListener(this);
        dialog.findViewById(R.id.tvPhoto).setOnClickListener(this);
        dialog.findViewById(R.id.btnCancel).setOnClickListener(this);

        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvGallery:
                onMenuClickListener.onMenuClick(GALLERY);
                break;
            case R.id.tvPhoto:
                onMenuClickListener.onMenuClick(TAKE_PHOTO);
                break;
        }
        this.dismiss();
    }

    public interface OnMenuClickListener {
        void onMenuClick(@MenuType int menuType);
    }


    /**
     * 相册
     */
    public final static int GALLERY = 1100;
    /**
     * 拍照
     */
    public final static int TAKE_PHOTO = 1200;

    @IntDef({GALLERY, TAKE_PHOTO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MenuType {
    }
}
