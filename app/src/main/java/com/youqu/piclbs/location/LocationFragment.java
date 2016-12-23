package com.youqu.piclbs.location;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.youqu.piclbs.R;
import com.youqu.piclbs.bean.AddressBean;
import com.youqu.piclbs.util.StreamReaderUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hjq on 16-12-22.
 */

public class LocationFragment extends Fragment {

    @BindView(R.id.location_category)
    RecyclerView category_RecyclerView;
    @BindView(R.id.location_content)
    RecyclerView content_RecyclerView;

    private CategoryAdapter categoryAdapter;
    private LocationAdapter locationAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, rootView);

        initRecyclerView();
        return rootView;
    }

    private void initRecyclerView() {
        String str = StreamReaderUtil.getString(getActivity(), "json");
        Gson gson = new Gson();
        final AddressBean items = gson.fromJson(str,AddressBean.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        category_RecyclerView.setLayoutManager(linearLayoutManager);
        category_RecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        content_RecyclerView.setLayoutManager(linearLayoutManager2);
        content_RecyclerView.setHasFixedSize(true);

        if (categoryAdapter == null){
            categoryAdapter = new CategoryAdapter(getActivity(),items.category);
        }
        if (locationAdapter == null){
            locationAdapter = new LocationAdapter(getActivity(),items.category.get(0).location);
        }

        categoryAdapter.setCategoryItemClickListener(new CategoryAdapter.onCategoryItemClickLinstener() {
            @Override
            public void OnItemClick(int pos) {
                locationAdapter.addItems(items.category.get(pos).location);
                Log.e("----->",items.category.get(pos).location+"");
                locationAdapter.notifyDataSetChanged();
            }
        });
        category_RecyclerView.setAdapter(categoryAdapter);
        content_RecyclerView.setAdapter(locationAdapter);
    }
}
