package com.youqu.piclbs.location;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youqu.piclbs.R;
import com.youqu.piclbs.bean.AddressBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjq on 16-12-23.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    public Activity activity;
    public List<AddressBean.CategoryBean> items;
    public onCategoryItemClickLinstener listener;
    public List<Boolean> isClicks;

    public interface onCategoryItemClickLinstener {
        void OnItemClick(int pos);
    }

    public CategoryAdapter(Activity activity, List<AddressBean.CategoryBean> items) {
        this.activity = activity;
        this.items = items;
        isClicks = new ArrayList<>();
        for (int i=0;i<items.size();i++){
            if (i == 0){
                isClicks.add(true);
            }else {
                isClicks.add(false);
            }
        }
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(activity).inflate(R.layout.layout_text, parent, false);
        MyViewHolder holder = new MyViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.MyViewHolder holder, final int position) {
        holder.textView.setText(items.get(position).category);

        if (isClicks.get(position)){
            holder.ll.setSelected(true);
        }else {
            holder.ll.setSelected(false);
        }

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClick(position);
                for (int i=0;i<items.size();i++){
                    isClicks.set(i,false);
                }
                isClicks.set(position,true);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout ll;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.layout_tv);
            ll = (LinearLayout) itemView.findViewById(R.id.layout_text);
        }
    }

    public void setCategoryItemClickListener(onCategoryItemClickLinstener listener) {
        this.listener = listener;
    }
}
