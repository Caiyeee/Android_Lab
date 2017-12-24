package com.example.caiye.lab9.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.caiye.lab9.R;
import com.example.caiye.lab9.model.Github;

import java.util.List;

/**
 * Created by caiye on 2017/12/23.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private Context context;
    private List<Github> user;
    private OnItemClickListener mOnItemClickListener = null;
    public CardAdapter(Context context,List<Github> user) {
        this.user = user;
        this.context = context;
    }
    // 添加接口和点击函数
    public interface OnItemClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
    //返回一个自定义的viewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    // 填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.login.setText(user.get(position).getLogin());
        holder.id.setText("id: "+user.get(position).getId());
        holder.blog.setText("blog: "+user.get(position).getBlog());

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(user != null)
            return user.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView login;
        TextView id;
        TextView blog;
        public ViewHolder(View view) {
            super(view);
            login = (TextView) view.findViewById(R.id.login);
            id = (TextView) view.findViewById(R.id.id);
            blog = (TextView) view.findViewById(R.id.blog);
        }
    }

}
