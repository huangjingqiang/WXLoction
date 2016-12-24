package com.youqu.piclbs.location;

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
 * Created by hjq on 16-12-23.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LoactionViewHolder> {
    public Activity activity;
    public List<AddressBean.CategoryBean.LocationBean> items;

    public LocationAdapter(Activity activity, List<AddressBean.CategoryBean.LocationBean> items) {
        this.activity = activity;
        this.items = items;
    }
    public void addItems(List<AddressBean.CategoryBean.LocationBean> items2){
        items = items2;
        notifyDataSetChanged();
    }

    @Override
    public LoactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(activity).inflate(R.layout.item_text,parent,false);
        LoactionViewHolder holder = new LoactionViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(LoactionViewHolder holder, int position) {
        holder.name.setText(items.get(position).name);
        holder.address.setText(items.get(position).location);

        Log.e("------ss>",items+"");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class LoactionViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView address;

        public LoactionViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_name);
            address = (TextView) itemView.findViewById(R.id.item_address);
        }
    }
}

