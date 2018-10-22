package com.markLove.Xplan.ui.adapter;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dmcbig.mediapicker.entity.Media;
import com.dmcbig.mediapicker.utils.FileUtils;
import com.dmcbig.mediapicker.utils.ScreenUtils;
import com.markLove.Xplan.R;
import com.markLove.Xplan.utils.ImageLoaderUtils;

import java.util.ArrayList;

/**
 * Created by huanglingjun on 2017/7/5.
 */

public class PublishMediaGridAdapter extends RecyclerView.Adapter<PublishMediaGridAdapter.MyViewHolder> {

    ArrayList<Media> medias;
    Context context;
    FileUtils fileUtils = new FileUtils();
    ArrayList<Media> selectMedias = new ArrayList<>();
    long maxSelect, maxSize;

    public PublishMediaGridAdapter(ArrayList<Media> list, Context context, ArrayList<Media> select, int max, long maxSize) {
        if (select != null) {
            this.selectMedias = select;
        }
        this.maxSelect = max;
        this.maxSize = maxSize;
        this.medias = list;
        this.context = context;
    }

    public void setData(ArrayList<Media> list){
        this.medias = list;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView media_image;
        public TextView textView_size;
        public RelativeLayout video_info;

        public MyViewHolder(View view) {
            super(view);
            media_image = (ImageView) view.findViewById(R.id.media_image);
            video_info = (RelativeLayout) view.findViewById(R.id.video_info);
            textView_size = (TextView) view.findViewById(R.id.textView_size);
            itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getItemWidth())); //让图片是个正方形
        }
    }

    int getItemWidth() {
        return (ScreenUtils.getScreenWidth(context) / 4) - 4;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_publish_media_view, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Media media = medias.get(position);
        Uri mediaUri = Uri.parse("file://" + media.path);

//        Glide.with(context)
//                .load(mediaUri)
//                .into(holder.media_image);
        ImageLoaderUtils.displayLocationRoundImage(context,media.path,holder.media_image);
//        ImageLoaderUtils.displayRoundImage(context,new File(media.path),holder.media_image,160,160);

        if (media.mediaType == 3) {
            holder.video_info.setVisibility(View.VISIBLE);
            holder.textView_size.setText(fileUtils.getSizeByUnit(media.size));
        } else {
            holder.video_info.setVisibility(View.INVISIBLE);
        }

        int isSelect = isSelect(media);


        holder.media_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

////                int isSelect = isSelect(media);
//                if (selectMedias.size() >= maxSelect && isSelect < 0) {
////                    Toast.makeText(context, context.getString(R.string.msg_amount_limit), Toast.LENGTH_SHORT).show();
//                } else {
////                    if (media.size > maxSize) {
//////                        Toast.makeText(context, context.getString(R.string.msg_size_limit) + (FileUtils.fileSize(maxSize)), Toast.LENGTH_LONG).show();
////                    } else {
////                        setSelectMedias(media);
//                        mOnItemClickListener.onItemClick(v, media, selectMedias);
////                    }
//                }

                if (mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(v, media, medias);
                }
            }
        });
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

    public void updateSelectAdapter(ArrayList<Media> select) {
        if (select != null) {
            this.selectMedias = select;
        }
        notifyDataSetChanged();
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
