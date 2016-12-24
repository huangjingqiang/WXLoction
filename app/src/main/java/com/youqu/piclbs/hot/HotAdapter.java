package com.youqu.piclbs.hot;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youqu.piclbs.R;
import com.youqu.piclbs.bean.AddressBean;

import java.util.List;

/**
 * Created by hjq on 16-12-22.
 */

public class HotAdapter extends RecyclerView.Adapter<HotAdapter.MyViewHolder>{
    private Activity activity;
    private List<AddressBean.TopLocationBean> items;

    public HotAdapter(Activity activity,List<AddressBean.TopLocationBean> items){
        this.activity = activity;
        this.items = items;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_text,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.address.setText(items.get(position).location);
        holder.title.setText(items.get(position).name);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView address;


        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_name);
            address = (TextView) itemView.findViewById(R.id.item_address);
        }
    }

}
