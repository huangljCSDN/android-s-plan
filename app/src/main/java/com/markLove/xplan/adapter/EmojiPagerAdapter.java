package com.markLove.xplan.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.markLove.xplan.emoji.EmojiOnClickListener;
import com.markLove.xplan.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoyunmin on 2017/7/25.
 */

public class EmojiPagerAdapter extends PagerAdapter {

    Context mContext;
    List<Integer> mDatas;
    List<RecyclerView> mViews;

    public EmojiPagerAdapter(Context mContext, List<Integer> mDatas) {
        super();
        this.mContext = mContext;
        this.mDatas = mDatas;
        mViews = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            RecyclerView rv = new RecyclerView(mContext);
            rv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(mContext, 250)));
            mViews.add(rv);
        }
    }

    EmojiOnClickListener emojiClickListener;

    public void setEmojiClickListener(EmojiOnClickListener emojiClickListener) {
        this.emojiClickListener = emojiClickListener;
    }

    @Override
    public int getCount() {
        return (int) Math.ceil((double) mDatas.size() / (double) 21);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecyclerView rv = mViews.get(position);
        int startPosition = position * 20;
        int endPosition = (position * 20 + 20) > mDatas.size() ? mDatas.size() : position * 20 + 20;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 7);
        EmojiItemAdapter emojiItemAdapter = new EmojiItemAdapter(mDatas.subList(startPosition, endPosition));
        if (null != emojiClickListener) {
            emojiItemAdapter.setEmojiClickListener(emojiClickListener);
        }
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(emojiItemAdapter);
        container.addView(rv);
        return rv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

//    static class EmojiPagerViewHolder extends RecyclerView.ViewHolder {
//
//        RecyclerView emojiPager;
//
//        public EmojiPagerViewHolder(View itemView) {
//            super(itemView);
//            emojiPager = (RecyclerView) itemView.findViewById(R.id.chat_emoji_pager);
//        }
//    }
}
