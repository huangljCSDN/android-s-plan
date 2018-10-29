package com.markLove.Xplan.ui.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.markLove.Xplan.R;
import com.markLove.Xplan.bean.GiftItem;
import com.markLove.Xplan.bean.msg.Message;
import com.markLove.Xplan.bean.msg.body.FileMessageBody;
import com.markLove.Xplan.bean.msg.body.GiftMessageBody;
import com.markLove.Xplan.bean.msg.body.MessageBody;
import com.markLove.Xplan.bean.msg.body.TxtMessageBody;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.db.DBDao;
import com.markLove.Xplan.module.emoji.EmojiUtils;
import com.markLove.Xplan.ui.activity.ShopChatActivity;
import com.markLove.Xplan.ui.activity.ZoomImageActivity;
import com.markLove.Xplan.ui.dialog.BecomeLovesDialog;
import com.markLove.Xplan.ui.dialog.CoupleGiftPopupWindow;
import com.markLove.Xplan.utils.AudioUtils;
import com.markLove.Xplan.utils.DensityUtils;
import com.markLove.Xplan.utils.FileUtils;
import com.markLove.Xplan.utils.ImageLoaderUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.markLove.Xplan.utils.PreferencesUtils;
import com.markLove.Xplan.utils.ScreenUtils;
import com.xsimple.im.db.datatable.IMMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by luoyunmin on 2017/6/29.
 */

public class TestChatMessageAdapter extends RecyclerView.Adapter<ChatBaseViewHolder> {

    Context context;
    List<Message> mDatas;
    MyGestureDetector mGestureDetector;
    int select = 0;//0默认显示,1显示选中删除,2删除选中
    List<String> selectPositionList;
    boolean black = false;

    public TestChatMessageAdapter(Context context, List<Message> datas) {
        this.context = context;
        this.mDatas = datas;
        selectPositionList = new ArrayList<>();
        mGestureDetector = new MyGestureDetector(context, new GestureListener());
    }

    private String fromHeadImgUrl;
    private String toHeadImgUrl;

    public void setToHeadImgUrl(String url) {
        if (!url.startsWith("http")) {
            url = Constants.BASE_IMG_URL + url;
        }
        this.toHeadImgUrl = url;
    }

    public void setFromHeadImgUrl(String url) {
        if (!url.startsWith("http")) {
            url = Constants.BASE_IMG_URL + url;
        }
        this.fromHeadImgUrl = url;
    }

    @Override
    public ChatBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ChatBaseViewHolder holder = null;
        View view = null;
        switch (ViewType.values()[viewType]) {
            case SEND_TXT:
                view = LayoutInflater.from(context).inflate(R.layout.chat_send_txt_item, viewGroup, false);
                holder = new ChatTxtViewHolder(view);
                break;
            case RECEIVED_TXT:
                view = LayoutInflater.from(context).inflate(R.layout.chat_received_txt_item, viewGroup, false);
                holder = new ChatTxtViewHolder(view);
                break;
            case SEND_IMG:
                view = LayoutInflater.from(context).inflate(R.layout.chat_send_img_item, viewGroup, false);
                holder = new ChatImgViewHolder(view);
                break;
            case RECEIVED_IMG:
                view = LayoutInflater.from(context).inflate(R.layout.chat_received_img_item, viewGroup, false);
                holder = new ChatImgViewHolder(view);
                break;
            case LOVE:
                view = LayoutInflater.from(context).inflate(R.layout.dialog_become_loves, viewGroup, false);
                holder = new ChatLoveViewHolder(view, this);
                break;
            case SEND_VOICE:
                view = LayoutInflater.from(context).inflate(R.layout.chat_send_voice_item, viewGroup, false);
                holder = new ChatVoiceViewHolder(view);
                break;
            case RECEIVED_VOICE:
                view = LayoutInflater.from(context).inflate(R.layout.chat_received_voice_item, viewGroup, false);
                holder = new ChatVoiceViewHolder(view);
                break;
            case SUPERLIKE:
                view = LayoutInflater.from(context).inflate(R.layout.chat_superlike_item, viewGroup, false);
                holder = new ChatSuperLikeViewHolder(view);
                break;
            case SEND_GIFT:
                view = LayoutInflater.from(context).inflate(R.layout.chat_send_gift_item, viewGroup, false);
                holder = new ChatGiftViewHolder(view);
                break;
            case RECEIVED_GIFT:
                view = LayoutInflater.from(context).inflate(R.layout.group_chat_received_gift_item, viewGroup, false);
                holder = new ChatGiftViewHolder(view);
                break;
            default:
                view = LayoutInflater.from(context).inflate(R.layout.chat_default_item, viewGroup, false);
                holder = new ChatDefaultViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final ChatBaseViewHolder viewHolder, final int position) {
        final Message msg = mDatas.get(position);
        viewHolder.setChatContent(msg);
        viewHolder.selectView.setTag(msg.getMsgID());
        viewHolder.selectView.setChecked(selectPositionList.contains(msg.getMsgID()));
        if (select == 1) {
            viewHolder.selectView.setVisibility(View.VISIBLE);
            viewHolder.selectView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!selectPositionList.contains(viewHolder.selectView.getTag())) {
                            selectPositionList.add(msg.getMsgID());
                        }
                    } else {
                        if (selectPositionList.contains(viewHolder.selectView.getTag())) {
                            selectPositionList.remove(msg.getMsgID());
                        }
                    }
                }
            });
        } else {
            viewHolder.selectView.setVisibility(View.GONE);
        }
        setUserHead(msg, viewHolder);
        if (null != viewHolder.contentView) {
            viewHolder.contentView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return mGestureDetector.onTouchEvent(event, position, v);
                }
            });
            viewHolder.contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public void onBindViewHolder(ChatBaseViewHolder viewHolder, int position, List payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(viewHolder, position);
        } else {
            if (payloads.get(0) instanceof Message.ChatStatus) {
                int me_user_id = PreferencesUtils.getInt(context, Constants.ME_USER_ID);
                Message msg = mDatas.get(position);
                if (msg.getFromID() == me_user_id) {
                    Message.ChatStatus status = (Message.ChatStatus) payloads.get(0);
                    DBDao.getDbDao(context).updateDataMessage(me_user_id, msg.getMsgID(), status);
                    mDatas.get(position).setStatus(status);
                    viewHolder.setStatus(status);
                }
            }
        }
    }

    FailMessageResend failMessageResend;

    public interface FailMessageResend {
        void failResend(Message msg);
    }

    public void setFailMessageResend(FailMessageResend failMessageResend) {
        this.failMessageResend = failMessageResend;
    }

    public void failOnClick(View v, final Message msg) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg.getStatus() == Message.ChatStatus.FAIL || msg.getStatus() == Message.ChatStatus.REJECTED) {
                    if (null != failMessageResend) {
                        failMessageResend.failResend(msg);
                    }
                }
            }
        });
    }

    private void setUserHead(final Message msg, ChatBaseViewHolder holder) {
        if (null != holder.userHead) {
            if (PreferencesUtils.getInt(context, Constants.ME_USER_ID) == msg.getFromID()) {
                ImageLoaderUtils.displayCircle(context, fromHeadImgUrl, holder.userHead);
            } else {
                ImageLoaderUtils.displayCircle(context, toHeadImgUrl, holder.userHead);
            }
            holder.userHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        holder.setStatus(msg.getStatus());
        if (null != holder.status && msg.getFromID() == PreferencesUtils.getInt(context, Constants.ME_USER_ID)) {
            failOnClick(holder.status, msg);
            if (null != holder.status) {
                if (msg.getStatus() == Message.ChatStatus.FAIL || msg.getStatus() == Message.ChatStatus.REJECTED) {
                    holder.status.setClickable(true);
                } else {
                    holder.status.setClickable(false);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position).getFromID() == PreferencesUtils.getInt(context, Constants.ME_USER_ID)) {
            switch (mDatas.get(position).getChatType()) {
                case TXT:
                    return ViewType.SEND_TXT.ordinal();
                case IMAGE:
                    return ViewType.SEND_IMG.ordinal();
                case LOVE:
                    return ViewType.LOVE.ordinal();
                case VOICE:
                    return ViewType.SEND_VOICE.ordinal();
                case SUPERLIKE:
                    return ViewType.SUPERLIKE.ordinal();
                case GIFT:
                    return ViewType.SEND_GIFT.ordinal();
                default:
                    return ViewType.DEFAULT.ordinal();
            }
        } else {
            switch (mDatas.get(position).getChatType()) {
                case TXT:
                    return ViewType.RECEIVED_TXT.ordinal();
                case IMAGE:
                    return ViewType.RECEIVED_IMG.ordinal();
                case LOVE:
                    return ViewType.LOVE.ordinal();
                case VOICE:
                    return ViewType.RECEIVED_VOICE.ordinal();
                case SUPERLIKE:
                    return ViewType.SUPERLIKE.ordinal();
                case GIFT:
                    return ViewType.RECEIVED_GIFT.ordinal();
                default:
                    return 0;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ChatTxtViewHolder extends ChatBaseViewHolder {
        TextView txtMsg;

        public ChatTxtViewHolder(View itemView) {
            super(itemView);
            userHead = (ImageView) itemView.findViewById(R.id.chat_head_img);
            txtMsg = (TextView) itemView.findViewById(R.id.chat_txt_msg);
            status = (ImageView) itemView.findViewById(R.id.chat_message_status);
            status.setTag(2);
            status.setVisibility(View.GONE);
            contentView = txtMsg;
        }

        @Override
        public void sending() {
            if (status instanceof ImageView) {
                if (1 != (int) status.getTag()) {
                    status.setTag(1);
                    ((ImageView) status).setImageResource(R.drawable.anim_refresh);
                }
                View v = itemView.findViewById(R.id.reject_status);
                if (null != v) {
                    v.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void success() {
            status.setTag(2);
            status.setVisibility(View.GONE);
            status.setClickable(false);
            View v = itemView.findViewById(R.id.reject_status);
            if (null != v) {
                v.setVisibility(View.GONE);
            }
        }

        @Override
        public void fail() {
            if (status instanceof ImageView) {
                status.setTag(3);
                ((ImageView) status).setImageResource(R.mipmap.chat_message_fail);
                status.setVisibility(View.VISIBLE);
                status.setClickable(true);
                View v = itemView.findViewById(R.id.reject_status);
                if (null != v) {
                    v.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void rejected() {
            if (status instanceof ImageView) {
                status.setTag(3);
                ((ImageView) status).setImageResource(R.mipmap.chat_message_fail);
                status.setVisibility(View.VISIBLE);
                status.setClickable(true);
                View v = itemView.findViewById(R.id.reject_status);
                if (null != v) {
                    v.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void setChatContent(final Message msg) {
            MessageBody body = msg.getBody();
            if (body instanceof TxtMessageBody) {
                final String bodyTxt = ((TxtMessageBody) body).getMsg();
                txtMsg.setText(EmojiUtils.parseEmoji(txtMsg.getContext(), bodyTxt));
            }
        }
    }

    static class ChatImgViewHolder extends ChatBaseViewHolder {
        ImageView imgMsg;

        public ChatImgViewHolder(View itemView) {
            super(itemView);
            userHead = (ImageView) itemView.findViewById(R.id.chat_head_img);
            imgMsg = (ImageView) itemView.findViewById(R.id.chat_img_msg);
            contentView = imgMsg;
            status = (ImageView) itemView.findViewById(R.id.chat_message_status);
            status.setTag(2);
            status.setVisibility(View.GONE);
        }

        @Override
        public void sending() {
            if (status instanceof ImageView) {
                if (1 != (int) status.getTag()) {
                    status.setTag(1);
                    ((ImageView) status).setImageResource(R.drawable.anim_refresh);
                }
                View v = itemView.findViewById(R.id.reject_status);
                if (null != v) {
                    v.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void success() {
            status.setTag(2);
            status.setVisibility(View.GONE);
            status.setClickable(false);
            View v = itemView.findViewById(R.id.reject_status);
            if (null != v) {
                v.setVisibility(View.GONE);
            }
        }

        @Override
        public void fail() {
            if (status instanceof ImageView) {
                status.setTag(3);
                ((ImageView) status).setImageResource(R.mipmap.chat_message_fail);
                status.setVisibility(View.VISIBLE);
                status.setClickable(true);
                View v = itemView.findViewById(R.id.reject_status);
                if (null != v) {
                    v.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void rejected() {
            if (status instanceof ImageView) {
                status.setTag(3);
                ((ImageView) status).setImageResource(R.mipmap.chat_message_fail);
                status.setVisibility(View.VISIBLE);
                status.setClickable(true);
                View v = itemView.findViewById(R.id.reject_status);
                if (null != v) {
                    v.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void setChatContent(Message msg) {
            MessageBody body = msg.getBody();
            LogUtils.i("huang","boody ="+ body);
            if (body instanceof FileMessageBody) {
                final Context context = rootView.getContext();
                final FileMessageBody imgMessageBody = (FileMessageBody) body;
                final String imgPath = Constants.LOCAL_IMG_PATH + imgMessageBody.getFileName();
                final String imgPath1 = context.getExternalFilesDir("imge").getAbsolutePath() + File.separator + imgMessageBody.getFileName();
                final String imgPath3 = imgMessageBody.getFilePath();
                if (new File(imgPath1).exists()) {
                    setImageResource(imgPath1);
                } else if (new File(imgPath).exists()) { //压缩图
                    setImageResource(imgPath);
                } else if (new File(imgPath3).exists()){ //原图
                    setImageResource(imgPath3);
                } else {
                    int me_user_id = PreferencesUtils.getInt(context, Constants.ME_USER_ID);
                    int damageImg;
                    if (msg.getFromID() == me_user_id) {
                        damageImg = R.mipmap.send_file_damage;
                    } else {
                        damageImg = R.mipmap.receiver_file_damage;
                    }
//                    ImageLoaderUtils.display(context,damageImg,imgMsg,200,200);
                    ImageLoaderUtils.displayRoundImage(context,damageImg,imgMsg,200,200);
                }
            }
        }

        private void setImageResource(String imgPath) {
            Context context = rootView.getContext();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imgPath, options);
            int width = options.outWidth;
            int height = options.outHeight;
            if (width <= 0 || height <= 0) {
                width = 100;
                height = 100;
            } else if (width > 400 || height > 400) {
                float inSampleSize = (float) 400 / (float) Math.max(width, height);
                width = (int) (inSampleSize * width);
                height = (int) (inSampleSize * height);
            }

//            ImageLoaderUtils.display(context,new File(imgPath),imgMsg,width,height);
            ImageLoaderUtils.displayRoundImage(context,new File(imgPath),imgMsg,width,height);
        }
    }

    static class ChatDefaultViewHolder extends ChatBaseViewHolder {

        TextView defaultView;

        public ChatDefaultViewHolder(View itemView) {
            super(itemView);
            defaultView = (TextView) itemView.findViewById(R.id.default_msg);
        }

        @Override
        public void sending() {

        }

        @Override
        public void success() {

        }

        @Override
        public void fail() {

        }


        @Override
        public void setChatContent(Message msg) {

        }
    }

    static class ChatLoveViewHolder extends ChatBaseViewHolder {

        TextView tvBecomeLoveMsg;
        LinearLayout llBecomeLove;
        LinearLayout llBecomeLoveContent;
        View horizontalLine;
        TextView tvBecomeLoveleft;
        TextView tvBecomeLoveRight;
        private TestChatMessageAdapter mAdapter;

        public ChatLoveViewHolder(View itemView) {
            super(itemView);
            llBecomeLoveContent = (LinearLayout) itemView.findViewById(R.id.chat_become_Content_ll);
            tvBecomeLoveMsg = (TextView) itemView.findViewById(R.id.tv_become_loves_msg);
            llBecomeLove = (LinearLayout) itemView.findViewById(R.id.become_love_ll);
            horizontalLine = itemView.findViewById(R.id.horizontal_line);
            tvBecomeLoveleft = (TextView) itemView.findViewById(R.id.tv_dialog_left);
            tvBecomeLoveRight = (TextView) itemView.findViewById(R.id.tv_dialog_right);
            contentView = llBecomeLoveContent;
        }

        public ChatLoveViewHolder(View itemView, TestChatMessageAdapter adapter) {
            super(itemView);
            llBecomeLoveContent = (LinearLayout) itemView.findViewById(R.id.chat_become_Content_ll);
            tvBecomeLoveMsg = (TextView) itemView.findViewById(R.id.tv_become_loves_msg);
            llBecomeLove = (LinearLayout) itemView.findViewById(R.id.become_love_ll);
            horizontalLine = itemView.findViewById(R.id.horizontal_line);
            tvBecomeLoveleft = (TextView) itemView.findViewById(R.id.tv_dialog_left);
            tvBecomeLoveRight = (TextView) itemView.findViewById(R.id.tv_dialog_right);
            contentView = llBecomeLoveContent;
            mAdapter = adapter;
        }

        @Override
        public void sending() {

        }

        @Override
        public void success() {

        }

        @Override
        public void fail() {

        }

        @Override
        public void setChatContent(final Message msg) {
            MessageBody body = msg.getBody();
        }
    }

    static class ChatGiftViewHolder extends ChatBaseViewHolder {
        TextView giftTxt;
        ImageView giftImg;
        LinearLayout giftContentll;

        public ChatGiftViewHolder(View itemView) {
            super(itemView);
            giftContentll = (LinearLayout) itemView.findViewById(R.id.chat_gift_content_ll);
            userHead = (ImageView) itemView.findViewById(R.id.chat_head_img);
            giftTxt = (TextView) itemView.findViewById(R.id.chat_gift_msg_txt);
            giftImg = (ImageView) itemView.findViewById(R.id.chat_gift_msg_img);
            status = (ImageView) itemView.findViewById(R.id.chat_message_status);
            status.setTag(2);
            status.setVisibility(View.GONE);
            contentView = giftContentll;
        }

        @Override
        public void sending() {
            if (status instanceof ImageView) {
                if (1 != (int) status.getTag()) {
                    status.setTag(1);
                    ((ImageView) status).setImageResource(R.drawable.anim_refresh);
                }
            }
        }

        @Override
        public void success() {
            status.setTag(2);
            status.setVisibility(View.GONE);
            status.setClickable(false);
        }

        @Override
        public void fail() {
            if (status instanceof ImageView) {
                status.setTag(3);
                ((ImageView) status).setImageResource(R.mipmap.chat_message_fail);
                status.setVisibility(View.VISIBLE);
                status.setClickable(true);
            }
        }

        @Override
        public void setChatContent(Message msg) {
            MessageBody body = msg.getBody();
            if (body instanceof GiftMessageBody) {
                GiftMessageBody giftMessageBody = (GiftMessageBody) msg.getBody();
                final Context context = rootView.getContext();
                int me_user_id = PreferencesUtils.getInt(context, Constants.ME_USER_ID);
                final GiftItem.DataBean giftItem = DBDao.getDbDao(context).queryGift(me_user_id, giftMessageBody.getGiftId());
                //根据ID查找到礼物的图片地址，然后加载
                if (null != giftItem) {
                    if (msg.getFromID() == PreferencesUtils.getInt(context, Constants.ME_USER_ID)) {
                        giftTxt.setText("你送给对方一个" + giftItem.getGiftName());
                    } else {
                        giftTxt.setText("你收到一个" + giftItem.getGiftName());
                    }
                    String giftImgUrl = giftItem.getGiftPicture();
                    if (null != giftImgUrl && !giftImgUrl.startsWith("http")) {
                        giftImgUrl = Constants.BASE_IMG_URL + giftImgUrl;
                    }
                    Glide.with(context).load(giftImgUrl).into(giftImg);

                }
            }
        }
    }

    static class ChatVoiceViewHolder extends ChatBaseViewHolder {
        TextView voiceTime;
        ImageView voicePlayer;
        RelativeLayout voiceContent;
        ImageView voiceDamage;

        public ChatVoiceViewHolder(View itemView) {
            super(itemView);
            userHead = (ImageView) itemView.findViewById(R.id.chat_head_img);
            voiceTime = (TextView) itemView.findViewById(R.id.chat_voice_time);
            voicePlayer = (ImageView) itemView.findViewById(R.id.chat_voice_player);
            voiceDamage = (ImageView) itemView.findViewById(R.id.chat_voice_damage);
            voiceContent = (RelativeLayout) itemView.findViewById(R.id.chat_voice_content);
            if (voicePlayer.getDrawable() instanceof AnimationDrawable) {
                AnimationDrawable drawable = (AnimationDrawable) voicePlayer.getDrawable();
                drawable.stop();
            }
            status = (ImageView) itemView.findViewById(R.id.chat_message_status);
            status.setTag(2);
            status.setVisibility(View.GONE);
            contentView = voiceContent;
        }

        @Override
        public void sending() {
            if (status instanceof ImageView) {
                if (1 != (int) status.getTag()) {
                    status.setTag(1);
                    ((ImageView) status).setImageResource(R.drawable.anim_refresh);
                }
                View v = itemView.findViewById(R.id.reject_status);
                if (null != v) {
                    v.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void success() {
            status.setTag(2);
            status.setVisibility(View.GONE);
            status.setClickable(false);
            View v = itemView.findViewById(R.id.reject_status);
            if (null != v) {
                v.setVisibility(View.GONE);
            }
        }

        @Override
        public void fail() {
            if (status instanceof ImageView) {
                status.setTag(3);
                ((ImageView) status).setImageResource(R.mipmap.chat_message_fail);
                status.setVisibility(View.VISIBLE);
                status.setClickable(true);
                View v = itemView.findViewById(R.id.reject_status);
                if (null != v) {
                    v.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void rejected() {
            if (status instanceof ImageView) {
                status.setTag(3);
                ((ImageView) status).setImageResource(R.mipmap.chat_message_fail);
                status.setVisibility(View.VISIBLE);
                status.setClickable(true);
                View v = itemView.findViewById(R.id.reject_status);
                if (null != v) {
                    v.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void setChatContent(Message msg) {
            MessageBody body = msg.getBody();
            if (body instanceof FileMessageBody) {
                final FileMessageBody voiceMessageBody = (FileMessageBody) msg.getBody();
                final Context context = rootView.getContext();
                final String voicePath = Constants.LOCAL_VOICE_PATH + voiceMessageBody.getFileName();
                final String voicePath1 = context.getExternalFilesDir("voice").getAbsolutePath() + File.separator + voiceMessageBody.getFileName();
                if (new File(voicePath1).exists()) {
                    setVoiceResource(msg, voicePath1);
                } else if (new File(voicePath).exists()) {
                    setVoiceResource(msg, voicePath);
                } else {
                    ViewGroup.LayoutParams layoutParams = voiceContent.getLayoutParams();
                    layoutParams.width = DensityUtils.dip2px(context, 142);
                    voiceContent.setLayoutParams(layoutParams);
                    voiceTime.setText(0 + "\"");
                    voiceDamage.setVisibility(View.VISIBLE);
                    voicePlayer.setTag(false);
                    if (msg.getFromID() == PreferencesUtils.getInt(context, Constants.ME_USER_ID)) {
                        voicePlayer.setImageResource(R.drawable.send_voice_05);
                    } else {
                        voicePlayer.setImageResource(R.drawable.received_voice_05);
                    }
                }
            }
        }

        private void setVoiceResource(Message msg, String voicePath) {
            final Context context = rootView.getContext();
            int duration = FileUtils.getAmrDuration(voicePath);
            ViewGroup.LayoutParams layoutParams = voiceContent.getLayoutParams();
            if (duration >= 0 && duration <= 10) {
                layoutParams.width = DensityUtils.dip2px(context, 64 + duration * 4);
            } else {
                layoutParams.width = DensityUtils.dip2px(context, (100 + 10 * (((duration - 10) / 5) + 1)));
            }
            voiceContent.setLayoutParams(layoutParams);
            voiceTime.setText((duration > 60 ? 60 : duration) + "\"");
            voiceDamage.setVisibility(View.GONE);
            voicePlayer.setTag(false);
            if (msg.getFromID() == PreferencesUtils.getInt(context, Constants.ME_USER_ID)) {
                voicePlayer.setImageResource(R.drawable.send_voice_05);
            } else {
                voicePlayer.setImageResource(R.drawable.received_voice_05);
            }
        }
    }

    static class ChatSuperLikeViewHolder extends ChatBaseViewHolder {

        TextView superLikeMsg;
        RelativeLayout superLikeContent;

        public ChatSuperLikeViewHolder(View itemView) {
            super(itemView);
            superLikeMsg = (TextView) itemView.findViewById(R.id.chat_superlike_msg);
            userHead = (ImageView) itemView.findViewById(R.id.chat_superlike_head);
            status = (TextView) itemView.findViewById(R.id.chat_superlike_fail);
            superLikeContent = (RelativeLayout) itemView.findViewById(R.id.chat_superlike_content);
            contentView = superLikeContent;
        }

        @Override
        public void sending() {

        }

        @Override
        public void success() {

        }

        @Override
        public void fail() {
            status.setTag(3);
            status.setVisibility(View.VISIBLE);
            status.setClickable(true);
        }


        @Override
        public void setChatContent(Message msg) {
            MessageBody body = msg.getBody();
            if (body instanceof TxtMessageBody) {
                superLikeMsg.setText(EmojiUtils.parseEmoji(rootView.getContext(), ((TxtMessageBody) body).getMsg()));
            }
        }
    }

    public void addOne(Message message) {

        if (message.getChatType() == Message.ChatType.IMAGE || message.getChatType() == Message.ChatType.VOICE
                || message.getChatType() == Message.ChatType.TXT || message.getChatType() == Message.ChatType.LOVE
                || message.getChatType() == Message.ChatType.GIFT || message.getChatType() == Message.ChatType.SUPERLIKE
                || message.getChatType() == Message.ChatType.VIDEO) {
            boolean hasMessage = false;
            for (int i = mDatas.size() - 1; i >= 0; i--) {
                Message tempMessage = mDatas.get(i);
                if (tempMessage.getMsgID().equals(message.getMsgID())) {
                    hasMessage = true;
                }
            }
            if (!hasMessage) {
                mDatas.add(message);
                notifyDataSetChanged();
            }

        }
    }

    public Message getItemMsg(int position) {
        return mDatas.get(position);
    }

    public void addAll(List<Message> datas) {
        for (Message msg : datas) {

            if (msg.getChatType() == Message.ChatType.IMAGE || msg.getChatType() == Message.ChatType.VOICE
                    || msg.getChatType() == Message.ChatType.TXT
                    || msg.getChatType() == Message.ChatType.VIDEO) {
                boolean hasMessage = false;
                for (int i = mDatas.size() - 1; i >= 0; i--) {
                    Message tempMessage = mDatas.get(i);
                    if (tempMessage.getMsgID().equals(msg.getMsgID())) {
                        hasMessage = true;
                    }
                }
                if (msg.getStatus() == Message.ChatStatus.SENDING) {
                    msg.setStatus(Message.ChatStatus.FAIL);
                }
                if (!hasMessage) {
                    mDatas.add(msg);
                }

            }
            notifyDataSetChanged();
        }
    }

    public int getItemPositionByMsgID(String msgID) {
        for (int i = mDatas.size() - 1; i >= 0; i--) {
            Message message = mDatas.get(i);
            if (msgID.equals(message.getMsgID())) {
                //获取ViewHolder,然后设置Status
                return i;
            }
        }
        return -1;
    }

    public enum ViewType {
        DEFAULT(0),
        SEND_TXT(1), RECEIVED_TXT(2),
        SEND_IMG(3), RECEIVED_IMG(4),
        SEND_VOICE(5), RECEIVED_VOICE(6),
        LOVE(7),
        SUPERLIKE(8),
        SEND_GIFT(9), RECEIVED_GIFT(10);
        int t;

        ViewType(int t) {
            this.t = t;
        }

    }


    public static void insertMessage(Context context, Message message) {
        int me_user_id = PreferencesUtils.getInt(context, Constants.ME_USER_ID);
        DBDao.getDbDao(context).insertMessage(me_user_id, message);
    }


    private static void showGiftAnim(Context context, Drawable drawable, String name) {
//        View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_gift_anim, null);
//        final Dialog dialog = new Dialog(context, R.style.gift_anim_dialog_style);
////        dialog.getWindow().setWindowAnimations(R.style.gift_dialog_anim);
//        ImageView img = (ImageView) view.findViewById(R.id.gift_img);
//
//        TextView text = (TextView) view.findViewById(R.id.gift_name);
//        img.setImageDrawable(drawable);
//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.gift_in);
//        //开始动画
//        img.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dialog.dismiss();
//            }
//        }, 5000);
//        text.setText(name);
//        dialog.setContentView(view);
//        dialog.show();
//        img.startAnimation(animation);
    }

    public int getUserSendMessageCount(int user_id) {
        int count = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            Message message = mDatas.get(i);
            if ((message.getFromID() == user_id) && (message.getStatus() == Message.ChatStatus.SUCCESS)) {
                count++;
                if (count >= 3) {
                    return 3;
                }
            }
        }
        return count;
    }

    public Message getMessageByID(String messageID) {
        for (int i = mDatas.size() - 1; i >= 0; i--) {
            Message tempMessage = mDatas.get(i);
            if (tempMessage.getMsgID().equals(messageID)) {
                return tempMessage;
            }
        }
        return null;
    }

    public void removeItemMsg(final int position) {
        BecomeLovesDialog.Builder builder = new BecomeLovesDialog.Builder(context);
        builder.setContentView(R.layout.custom_dialog)
                .setTextViewStyle(R.id.custom_dialog_content, "是否删除该条信息", -1, -1, View.VISIBLE)
                .setTextViewStyle(R.id.custom_dialog_cancel, "取消", -1, Color.parseColor("#40A6EA"), View.VISIBLE)
                .setTextViewStyle(R.id.custom_dialog_define, "确定", -1, Color.parseColor("#40A6EA"), View.VISIBLE)
                .setOnClickListener(R.id.custom_dialog_cancel, null)
                .setOnClickListener(R.id.custom_dialog_define, new BecomeLovesDialog.DialogViewOnClick() {
                    @Override
                    public void onClick() {
                        Message msg = mDatas.get(position);
                        int me_user_id = PreferencesUtils.getInt(context, Constants.ME_USER_ID);
                        DBDao.getDbDao(context).delectMessage(me_user_id, msg.getMsgID());
                        mDatas.remove(position);
                        notifyItemRemoved(position);
                    }
                });
        builder.create().show();
    }

    public void selectRemove(int select) {
        this.select = select;
        if (this.select == 0) {
            selectPositionList.clear();
        } else if (this.select == 1) {
            ((ShopChatActivity) context).showRemove();
        } else if (this.select == 2) {
            removeSelecPosition();
        }
        selectPositionList.clear();
        notifyDataSetChanged();
        ((ShopChatActivity) context).selectPosition(mGestureDetector.getPosition());
    }

    public void removeSelecPosition() {
        if (!selectPositionList.isEmpty()) {
            int me_user_id = PreferencesUtils.getInt(context, Constants.ME_USER_ID);
            for (String msgID : selectPositionList) {
                Iterator<Message> iterator = mDatas.iterator();
                while (iterator.hasNext()) {
                    Message msg = iterator.next();
                    if (msg.getMsgID().equals(msgID)) {
                        iterator.remove();
                        DBDao.getDbDao(context).delectMessage(me_user_id, msg.getMsgID());
                        break;
                    }
                }
            }
        }
    }

    public boolean hasSelectRemovePostion() {
        return selectPositionList.isEmpty();
    }

    CoupleGiftPopupWindow popupWindow;

    class GestureListener extends GestureDetector.SimpleOnGestureListener {


        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Message msg = mDatas.get(mGestureDetector.getPosition());
            switch (msg.getChatType()) {
                case IMAGE:
                    FileMessageBody imgMessageBody = (FileMessageBody) msg.getBody();
                    String imgPath = Constants.LOCAL_IMG_PATH + imgMessageBody.getFileName();
                    String imgPath1 = context.getExternalFilesDir("img").getAbsolutePath() + File.separator + imgMessageBody.getFileName();
                    if (new File(imgPath1).exists()) {
                        toZoomImage(imgPath1);
                    } else if (new File(imgPath).exists()) {
                        toZoomImage(imgPath);
                    }
                    break;
                case VOICE:
                    FileMessageBody voiceMessageBody = (FileMessageBody) msg.getBody();
                    String voicePath;
                    String voicePath1 = Constants.LOCAL_VOICE_PATH + voiceMessageBody.getFileName();
                    String voicePath2 = context.getExternalFilesDir("voice").getAbsolutePath() + File.separator + voiceMessageBody.getFileName();
                    if (new File(voicePath1).exists()) {
                        voicePath = voicePath1;
                    } else if (new File(voicePath2).exists()) {
                        voicePath = voicePath2;
                    } else {
                        return false;
                    }
                    final ImageView voicePlayer = (ImageView) mGestureDetector.getTouchView().findViewById(R.id.chat_voice_player);
                    if (msg.getFromID() == PreferencesUtils.getInt(context, Constants.ME_USER_ID)) {
                        voicePlayer.setImageResource(R.drawable.send_voice_05);
                        final AnimationDrawable drawable = (AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.send_voice_anim);
                        if (!(boolean) voicePlayer.getTag()) {
                            AudioUtils.getInstance().play(voicePath, new AudioUtils.PlayStatusListener() {
                                @Override
                                public void playEnd() {
                                    drawable.stop();
                                    drawable.selectDrawable(1);
                                    voicePlayer.setImageResource(R.drawable.send_voice_05);
                                    voicePlayer.setTag(false);
                                }

                                @Override
                                public void playStart() {
                                    voicePlayer.setImageDrawable(drawable);
                                    drawable.start();
                                    voicePlayer.setTag(true);
                                }
                            });
                        } else {
                            AudioUtils.getInstance().stop();
                            drawable.stop();
                            drawable.selectDrawable(1);
                            voicePlayer.setImageResource(R.drawable.send_voice_05);
                            voicePlayer.setTag(false);
                        }
                    } else {
                        voicePlayer.setImageResource(R.drawable.received_voice_05);
                        final AnimationDrawable drawable = (AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.received_voice_anim);
                        if (!(boolean) voicePlayer.getTag()) {

                            AudioUtils.getInstance().play(voicePath, new AudioUtils.PlayStatusListener() {
                                @Override
                                public void playEnd() {
                                    voicePlayer.setTag(false);
                                    drawable.stop();
                                    drawable.selectDrawable(1);
                                    voicePlayer.setImageResource(R.drawable.received_voice_05);
                                }

                                @Override
                                public void playStart() {
                                    voicePlayer.setImageDrawable(drawable);
                                    drawable.start();
                                    voicePlayer.setTag(true);
                                }
                            });
                        } else {
                            AudioUtils.getInstance().stop();
                            drawable.stop();
                            drawable.selectDrawable(1);
                            voicePlayer.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.received_voice_05));
                            voicePlayer.setTag(false);
                        }
                    }
                    break;
                case GIFT:
                    int me_user_id = PreferencesUtils.getInt(context, Constants.ME_USER_ID);
                    GiftMessageBody giftMessageBody = (GiftMessageBody) msg.getBody();
                    final GiftItem.DataBean giftItem = DBDao.getDbDao(context).queryGift(me_user_id, giftMessageBody.getGiftId());
                    ImageView imageView = (ImageView) mGestureDetector.getTouchView().findViewById(R.id.chat_gift_msg_img);
                    //放大动画
                    Drawable drawable = imageView.getDrawable();
                    showGiftAnim(context, drawable, giftItem.getGiftName());
                    break;
            }
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (select == 0 || select == 2) {
                if (null == popupWindow) {
                    popupWindow = new CoupleGiftPopupWindow(context);
                    View bubbleView = LayoutInflater.from(context).inflate(R.layout.chat_item_dialog, null);
                    popupWindow.setBubbleView(bubbleView);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setParam(DensityUtils.dip2px(context, 232), ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setBubbleViewShadowColor("#3A3A3A");
                }
                final Message msg = mDatas.get(mGestureDetector.getPosition());
                if (msg.getChatType() != Message.ChatType.TXT) {
                    popupWindow.getBubbleView().findViewById(R.id.chat_item_dialog_copy).setVisibility(View.GONE);
                    popupWindow.getBubbleView().findViewById(R.id.chat_item_dialog_line1).setVisibility(View.GONE);
                } else {
                    popupWindow.getBubbleView().findViewById(R.id.chat_item_dialog_copy).setVisibility(View.VISIBLE);
                    popupWindow.getBubbleView().findViewById(R.id.chat_item_dialog_line1).setVisibility(View.VISIBLE);
                    popupWindow.getBubbleView().findViewById(R.id.chat_item_dialog_copy).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText("text", ((TxtMessageBody) msg.getBody()).getMsg());
                            cm.setPrimaryClip(clipData);

                        }
                    });
                }
                popupWindow.getBubbleView().findViewById(R.id.chat_item_dialog_delect).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        removeItemMsg(mGestureDetector.getPosition());
                    }
                });
                popupWindow.getBubbleView().findViewById(R.id.chat_item_dialog_more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        selectRemove(1);
                    }
                });
                View touchView = mGestureDetector.getTouchView();
                int[] touchLocation = new int[2];
                touchView.getLocationOnScreen(touchLocation);
                int screenWidth = ScreenUtils.getScreenWidth(context);
                //计算箭头偏移量
                int offset = DensityUtils.dip2px(context, 232) / 2;
                //计算箭头显示在上面还是下面
                //如果点击位置大于屏幕2/3
                int bubbleViewGravity = Gravity.BOTTOM;
                //点击实际的点，往上或下移动一点点
                int showAtLocationX = (int) (e.getRawX() - offset);
                if (e.getRawX() < offset) {
                    showAtLocationX = 0;
                    offset = (int) e.getRawX();
                } else if ((screenWidth - e.getRawX()) < offset) {
                    showAtLocationX = screenWidth - offset * 2;
                    offset = (int) (e.getRawX() - showAtLocationX);
                }
                int showAtLocationY = touchLocation[1] + touchView.getHeight();
                if (mGestureDetector.getPosition() == mDatas.size() - 1) {
                    bubbleViewGravity = Gravity.TOP;
                    showAtLocationY = touchLocation[1] - (DensityUtils.dip2px(context, 32) + 60);
                }
                popupWindow.setOffset(bubbleViewGravity, offset);
                if (!popupWindow.isShowing()) {
                    popupWindow.showAtLocation(mGestureDetector.getTouchView(), Gravity.NO_GRAVITY, showAtLocationX, showAtLocationY);
                } else {
                    popupWindow.update();
                }
                mGestureDetector.removeTouchView();
            }
        }
    }

    class MyGestureDetector extends GestureDetector {
        int position = -1;
        View view;

        public MyGestureDetector(Context context, OnGestureListener listener) {
            super(context, listener);
        }

        public boolean onTouchEvent(MotionEvent ev, int position, View v) {
            this.position = position;
            this.view = v;
//            boolean touchEvent = onTouchEvent(ev);
//            Log.e("lym", "onTouchEvent: " + touchEvent);
            return onTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            return super.onTouchEvent(ev);
        }

        public int getPosition() {
            return position;
        }

        public void removeTouchView() {
            view = null;
        }

        public View getTouchView() {
            return view;
        }

    }

    public void dismissItemPopup() {
        if (null != popupWindow && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public void setBlack(boolean black) {
        this.black = black;
    }

    private void toZoomImage(String path) {
        Intent intent = new Intent(context, ZoomImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("imgPath", path);
        intent.putExtra("data", bundle);
        context.startActivity(intent);
    }

    public void destory() {
        int me_user_id = PreferencesUtils.getInt(context, Constants.ME_USER_ID, 0);
        if (me_user_id != 0) {
            for (Message msg : mDatas) {
                if (msg.getFromID() == me_user_id && msg.getStatus() == Message.ChatStatus.SENDING) {
                    msg.setStatus(Message.ChatStatus.FAIL);
                    DBDao.getDbDao(context).insertMessage(me_user_id, msg);
                }
            }
        }
    }

    public interface OnRejectCoupleListener {
        void onRejectCouple(String rejectContactsId);
    }

    private OnRejectCoupleListener mListener;


    public void setOnRejectCoupleListener(OnRejectCoupleListener listener) {
        mListener = listener;
    }

    public List<Message> changeListIMMessageToListMessage(List<IMMessage> imMessageList){
        List<Message> list = new ArrayList<>();
        for (IMMessage imMessage : imMessageList){
             Message message = changeIMMessageToMessage(imMessage);
             list.add(message);
        }
        return list;
    }

    public Message changeIMMessageToMessage(IMMessage imMessage){
        Message message = null;
        if (imMessage.getContentType() == IMMessage.CONTENT_TYPE_SHORT_VOICE
                || imMessage.getContentType() == IMMessage.CONTENT_TYPE_VOICE_CHAT){
            message = Message.createVoiceMessage(Message.Type.CHAT, Integer.parseInt(imMessage.getSenderId()), Integer.parseInt(imMessage.getTagertId()), "", imMessage.getContent());
        } else if (imMessage.getContentType() == IMMessage.CONTENT_TYPE_IMG){
            message = Message.createImageMessage(Message.Type.CHAT, Integer.parseInt(imMessage.getSenderId()), Integer.parseInt(imMessage.getTagertId()), "", imMessage.getContent());
        } else {
           message = Message.createTxtMessage(Message.Type.CHAT,Integer.parseInt(imMessage.getSenderId()), Integer.parseInt(imMessage.getTagertId()),imMessage.getContent());
        }
        return message;
    }
}
