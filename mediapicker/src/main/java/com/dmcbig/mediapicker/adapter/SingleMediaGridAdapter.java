package com.dmcbig.mediapicker.adapter;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.R;
import com.dmcbig.mediapicker.entity.Media;
import com.dmcbig.mediapicker.utils.FileUtils;
import com.dmcbig.mediapicker.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by dmcBig on 2017/7/5.
 * 单张照片
 */

public class SingleMediaGridAdapter extends RecyclerView.Adapter<SingleMediaGridAdapter.MyViewHolder> {

    ArrayList<Media> medias;
    Context context;
    FileUtils fileUtils = new FileUtils();
    ArrayList<Media> selectMedias = new ArrayList<>();
    long maxSelect, maxSize;

    public SingleMediaGridAdapter(ArrayList<Media> list, Context context, ArrayList<Media> select, int max, long maxSize) {
        if (select != null) {
            this.selectMedias = select;
        }
        this.maxSelect = max;
        this.maxSize = maxSize;
        this.medias = list;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView media_image;

        public MyViewHolder(View view) {
            super(view);
            media_image = (ImageView) view.findViewById(R.id.media_image);
            itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getItemWidth())); //让图片是个正方形
        }
    }

    int getItemWidth() {
        return (ScreenUtils.getScreenWidth(context) / PickerConfig.GridSpanCount) - PickerConfig.GridSpanCount;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_media_view_item, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Media media = medias.get(position);
        Uri mediaUri = Uri.parse("file://" + media.path);

        Glide.with(context)
                .load(mediaUri)
                .into(holder.media_image);
        holder.media_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectMedias(media);
                if (mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(v,media,null);
                }
            }
        });
    }

    public void clearSelect(){
        selectMedias.clear();
    }

    public void setSelectMedias(Media media) {
        int index = isSelect(media);
        if (index == -1) {
            selectMedias.add(media);
        } else {
            selectMedias.remove(index);
        }
    }

    /**
     * @param media
     * @return 大于等于0 就是表示以选择，返回的是在selectMedias中的下标
     */
    public int isSelect(Media media) {
        int is = -1;
        if (selectMedias.size() <= 0) {
            return is;
        }
        for (int i = 0; i < selectMedias.size(); i++) {
            Media m = selectMedias.get(i);
            if (m.path.equals(media.path)) {
                is = i;
                break;
            }
        }
        return is;
    }

    public void updateAdapter(ArrayList<Media> list) {
        this.medias = list;
        notifyDataSetChanged();
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public ArrayList<Media> getSelectMedias() {
        return selectMedias;
    }

    @Override
    public int getItemCount() {
        return medias.size();
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Media data, ArrayList<Media> selectMedias);
    }
}
