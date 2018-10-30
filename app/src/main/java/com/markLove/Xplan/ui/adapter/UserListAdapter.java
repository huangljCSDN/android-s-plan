package com.markLove.Xplan.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markLove.Xplan.R;
import com.markLove.Xplan.bean.NearUserBean;
import com.markLove.Xplan.utils.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：created by huanglingjun on 2018/10/18
 * 描述：用户列表
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder>{

    private Context context;
    private List<NearUserBean> list = new ArrayList<>();

    public UserListAdapter(Context context, List<NearUserBean> datas) {
        this.context = context;
        this.list =datas;
    }

    public void setData(List<NearUserBean> data){
        this.list = data;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.contentView.setTag(position);
        if (position == getItemCount() - 1 || position == getItemCount() -2){
            holder.contentView.setVisibility(View.INVISIBLE);
        } else {
            holder.contentView.setVisibility(View.VISIBLE);
            NearUserBean peopleBean = list.get(position);
            holder.nickName.setText(peopleBean.getNickName());
            holder.tvTime.setText(peopleBean.getLastOnline());
            if (peopleBean.getSex() == 1){
                holder.ivSex.setImageResource(R.drawable.ic_man);
            } else {
                holder.ivSex.setImageResource(R.drawable.ic_woman);
            }

            if (peopleBean.getIsCertification() == 1){
                holder.ivCertification.setVisibility(View.VISIBLE);
            } else {
                holder.ivCertification.setVisibility(View.GONE);
            }

            ImageLoaderUtils.displayCircle(context,peopleBean.getHeadImageUrl(),holder.ivHead);

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
        View contentView;
        TextView tvTime,nickName;
        ImageView ivSex,ivHead,ivCertification;

        public MyViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            nickName = (TextView) itemView.findViewById(R.id.tv_nick_name);
            ivSex = (ImageView) itemView.findViewById(R.id.iv_sex);
            ivHead = (ImageView) itemView.findViewById(R.id.iv_head);
            ivCertification = itemView.findViewById(R.id.iv_certification);
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
