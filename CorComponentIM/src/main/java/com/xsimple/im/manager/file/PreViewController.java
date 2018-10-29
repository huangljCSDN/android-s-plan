package com.xsimple.im.manager.file;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.networkengine.controller.PreviewConroller;
import com.networkengine.engine.LogicEngine;
import com.xsimple.im.R;
import com.xsimple.im.engine.IMEngine;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by liuhao on 2017/3/14.
 */

public class PreViewController {

    private Context mContext;

    private PreviewConroller mConroller;

    private IMEngine mImEngine;

    public PreViewController(Context context) {
        this.mContext = context;
        mConroller = new PreviewConroller();
        mImEngine = IMEngine.getInstance(context);
    }

    /**
     * 判断能否预览
     *
     * @param type
     * @return
     */
    public boolean canPreViewHtml(String type) {

        if ("doc".equals(type) || "docx".equals(type) || "xls".equals(type)
                || "xlsx".equals(type) || "ppt".equals(type)
                || "pptx".equals(type) || "pdf".equals(type) || "txt".equals(type)
                || "csv".equals(type) || "xml".equals(type)
                ) {
            return true;
        }
        return false;
    }

    /**
     * 预览
     *
     * @param sha
     * @param filetype
     */
    public void preViewHtml(String sha, String filetype) {

//        if (TextUtils.isEmpty(sha) || TextUtils.isEmpty(filetype)) {
//            return;
//        }
//        if (!canPreViewHtml(filetype)) {
//            Toast.makeText(mContext
//                    , mContext.getString(R.string.im_unsupport_previewing)
//                    , Toast.LENGTH_SHORT)
//                    .show();
//            return;
//        }
//
//        final ProgressDialog mProgressDialog = ProgressDialog.createDialog(mContext, null, false);
//        mProgressDialog.setMessage(mContext.getString(R.string.loading));
//        mProgressDialog.show();
//        String userAgent = "Mozilla/5.0 (Linux; U; Android " + android.os.Build.VERSION.RELEASE + "; zh-cn; " + android.os.Build.MODEL + ") AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
//        Map<String, String> map = new HashMap<>();
//
//        map.put("loginName", LogicEngine.getInstance().getUser().getId());
//        map.put("authToken", LogicEngine.getInstance().getUser().getUserToken());
//        map.put("appKey", com.networkengine.PubConstant.APP_KEY);
//        map.put("userAgent", userAgent);
//        map.put("downLoadUrl", mImEngine.getLogicEngine().getFileTransBaseUrl() + "file/download?sha=" + sha);
//        map.put("platFormType", "xsimple");
//        map.put("filePid", sha);
//        map.put("fileType", filetype);
//
//
//        mConroller.getPreViewHtml(map, new PreviewConroller.IPreviewConroller() {
//
//            @Override
//            public void onSuccess(String htmlPath) {
//                mProgressDialog.dismiss();
//                if (TextUtils.isEmpty(htmlPath)) {
//                    Toast.makeText(mContext
//                            , mContext.getString(R.string.business_preview_error)
//                            , Toast.LENGTH_SHORT)
//                            .show();
//                } else {
//                    try {
//                        String packageName = mContext.getPackageName();
//                        Intent intent = new Intent(packageName + ".WebActivity");
//                        intent.putExtra("HTML_URL", htmlPath);
//                        mContext.startActivity(intent);
//                    } catch (ActivityNotFoundException e) {
//                        Toast.makeText(mContext
//                                , mContext.getString(R.string.business_preview_error)
//                                , Toast.LENGTH_SHORT)
//                                .show();
//                    }
//
//
//                    //
//                }
//            }
//
//            @Override
//            public void onFail(String msg) {
//                Toast.makeText(mContext
//                        , mContext.getString(R.string.business_preview_error)
//                        , Toast.LENGTH_SHORT)
//                        .show();
//                mProgressDialog.dismiss();
//            }
//        });

    }
}
