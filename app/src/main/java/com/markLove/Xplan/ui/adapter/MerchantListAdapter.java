package com.markLove.Xplan.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.markLove.Xplan.R;
import com.markLove.Xplan.bean.MerchantBean;
import com.markLove.Xplan.ui.widget.GlideRoundImage;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：created by huanglingjun on 2018/10/18
 * 描述：店铺列表
 */
public class MerchantListAdapter extends RecyclerView.Adapter<MerchantListAdapter.MyViewHolder>{

    private Context context;
    private List<MerchantBean> list = new ArrayList<>();

    public MerchantListAdapter(Context context, List<MerchantBean> datas) {
        this.context = context;
        this.list =datas;
    }

    public void setData(List<MerchantBean> data){
        this.list.addAll(data);
//        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_circle_two,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.contentView.setTag(position);
        if (position == getItemCount() - 1 || position == getItemCount() -2){
            holder.contentView.setVisibility(View.INVISIBLE);
        } else {
            holder.contentView.setVisibility(View.VISIBLE);
            MerchantBean merchantBean = list.get(position);
            holder.tvShopName.setText(merchantBean.getShopName());
            holder.tvShopCount.setText(merchantBean.getGroupCount()+"");

            Glide.with(context).load(merchantBean.getLogoUrl())
                    .apply(RequestOptions.placeholderOf(R.drawable.bg_merchant))
                    .apply(RequestOptions.errorOf(R.drawable.bg_merchant))
                    .apply(RequestOptions.bitmapTransform(new GlideRoundImage(context)))
                    .into(holder.ivBg);

            holder.contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null){
                        onItemClickListener.onItemClick(view,position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvShopName,tvShopCount;
        ImageView ivBg,ivMore;
        private View contentView;

        public MyViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            tvShopName = (TextView) itemView.findViewById(R.id.tv_shop_name);
            tvShopCount = (TextView) itemView.findViewById(R.id.tv_shop_count);
            ivBg = (ImageView) itemView.findViewById(R.id.iv_bg);
            ivMore = (ImageView) itemView.findViewById(R.id.iv_shop_more);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
