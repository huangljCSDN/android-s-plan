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
import com.markLove.Xplan.bean.ShopItemBean;
import com.markLove.Xplan.ui.widget.GlideRoundImage;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：created by huanglingjun on 2018/10/18
 * 描述：店铺聊天室列表
 */
public class ShopChatAdapter extends RecyclerView.Adapter<ShopChatAdapter.MyViewHolder>{

    private Context context;
    private List<ShopItemBean> list = new ArrayList<>();

    public ShopChatAdapter(Context context, List<ShopItemBean> datas) {
        this.context = context;
        this.list =datas;
    }

    public void setData(List<ShopItemBean> data){
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.contentView.setTag(position);
        ShopItemBean shopItemBean = list.get(position);
        holder.tvShopName.setText(shopItemBean.shopName);
        holder.tvShopCount.setText(shopItemBean.shopCount);

        Glide.with(context).load(R.drawable.icon)
                .apply(RequestOptions.placeholderOf(R.drawable.icon_loading_default))
                .apply(RequestOptions.errorOf(R.drawable.icon_loading_default))
                .apply(RequestOptions.bitmapTransform(new GlideRoundImage(context)))
                .into(holder.ivBg);

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

}
