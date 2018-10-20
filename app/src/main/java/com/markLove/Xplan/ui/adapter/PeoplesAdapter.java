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
import com.markLove.Xplan.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：created by huanglingjun on 2018/10/18
 * 描述：
 */
public class PeoplesAdapter extends RecyclerView.Adapter<PeoplesAdapter.MyViewHolder>{

    private Context context;
    private List<UserBean> list = new ArrayList<>();

    public PeoplesAdapter(Context context, List<UserBean> datas) {
        this.context = context;
        this.list =datas;
    }

    public void setData(List<UserBean> data){
        this.list.addAll(data);
//        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_circle_one,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.contentView.setTag(position);
        UserBean peopleBean = list.get(position);
        holder.nickName.setText(peopleBean.getNickName());
        holder.tvTime.setText(peopleBean.getNickName());
        if (peopleBean.getSex() == 1){
            holder.ivSex.setImageResource(R.drawable.ic_man);
        } else {
            holder.ivSex.setImageResource(R.drawable.ic_woman);
        }

        Glide.with(context).load(R.drawable.icon)
                .apply(RequestOptions.placeholderOf(R.drawable.bg_circle))
                .apply(RequestOptions.errorOf(R.drawable.bg_circle))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.ivHead);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        View contentView;
        TextView tvTime,nickName;
        ImageView ivSex,ivHead;

        public MyViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            nickName = (TextView) itemView.findViewById(R.id.tv_nick_name);
            ivSex = (ImageView) itemView.findViewById(R.id.iv_sex);
            ivHead = (ImageView) itemView.findViewById(R.id.iv_head);
        }
    }

}
