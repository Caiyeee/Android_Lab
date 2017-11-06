package com.example.caiye.lab3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
/**
 * Created by caiye on 2017/10/24.
 */

public class myRecyclerAdapter extends RecyclerView.Adapter<myRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Good> good;
    private OnItemClickListener mOnItemClickListener=null;

    public myRecyclerAdapter(Context context,List<Good> good) {
        this.good = good;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tx1;
        TextView tx2;
        public ViewHolder(View view) {
            super(view);
            tx1 = (TextView) view.findViewById(R.id.cir_rec);
            tx2 = (TextView) view.findViewById(R.id.itemName_rec);
        }
    }
    //返回一个自定义的ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoplist,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tx1.setText(good.get(position).getName().substring(0,1).toUpperCase());
        holder.tx2.setText(good.get(position).getName());

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
        if(good != null)
           return good.size();
        return 0;
    }
}
