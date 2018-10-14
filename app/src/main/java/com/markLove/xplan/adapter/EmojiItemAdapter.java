package com.markLove.xplan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.markLove.xplan.emoji.EmojiOnClickListener;
import com.markLove.xplan.utils.ScreenUtils;

import java.util.List;
import com.markLove.xplan.R;
/**
 * Created by luoyunmin on 2017/7/25.
 */

public class EmojiItemAdapter extends RecyclerView.Adapter<EmojiItemAdapter.EmojiItemViewHolder> {

    List<Integer> emojis;
    EmojiOnClickListener emojiClickListener;

    public EmojiItemAdapter(List<Integer> emojis) {
        this.emojis = emojis;
    }

    @Override
    public EmojiItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int width = ScreenUtils.getScreenWidth(viewGroup.getContext()) / 7;
        int height = viewGroup.getHeight() / 3;
        ImageView emojiItem = new ImageView(viewGroup.getContext());
//        emojiItem.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        int topMargin = (height - 64) / 2;
        int leftMargin = (width - 64) / 2;
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(64, 64);
        params.setMargins(leftMargin, topMargin, leftMargin, topMargin);
        emojiItem.setLayoutParams(params);
        return new EmojiItemViewHolder(emojiItem);
    }

    @Override
    public void onBindViewHolder(EmojiItemViewHolder emojiItemViewHolder, final int position) {
        if (position < emojis.size()) {
            final int emojiIcon = emojis.get(position);
            emojiItemViewHolder.emojiItem.setImageResource(emojiIcon);
            emojiItemViewHolder.emojiItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emojiClickListener.onEmojiItemClick(emojiIcon);
                }
            });
        } else if (position == getItemCount() - 1) {
            emojiItemViewHolder.emojiItem.setImageResource(R.mipmap.emoji_delect);
            emojiItemViewHolder.emojiItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emojiClickListener.onEmojiItemClick(R.mipmap.emoji_delect);
                }
            });
        }
    }

    public void setEmojiClickListener(EmojiOnClickListener emojiClickListener) {
        this.emojiClickListener = emojiClickListener;
    }

    @Override
    public int getItemCount() {
        return 21;
    }

    public static class EmojiItemViewHolder extends RecyclerView.ViewHolder {
        ImageView emojiItem;

        public EmojiItemViewHolder(View itemView) {
            super(itemView);
            emojiItem = (ImageView) itemView;
        }
    }
}
