package com.youqu.piclbs.hot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.youqu.piclbs.R;
import com.youqu.piclbs.bean.AddressBean;
import com.youqu.piclbs.util.DensityUtil;
import com.youqu.piclbs.util.SaveDialogFragment;
import com.youqu.piclbs.util.SharedPreferencesUtil;
import com.youqu.piclbs.util.StreamReaderUtil;
import com.youqu.piclbs.util.WriteImageGps;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hjq on 16-12-22.
 */

public class HotFragment extends Fragment {

    @BindView(R.id.hot_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.layot_image)
    LinearLayout iv;
    public HotAdapter adapter;
    @BindView(R.id.transitionsContainer)
    FrameLayout transitionsContainer;
    @BindView(R.id.image_save)
    TextView save;
    private String lo;
    private String la;
    private AddressBean items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hot, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        initClisk();
        return rootView;
    }

    private void initClisk() {
        adapter.setHotItemClickLinstener(new HotAdapter.onHotItemClickLinstener() {
            @Override
            public void ItemClisk(int pos, boolean isex) {
                adapter.notifyDataSetChanged();
                if ((iv.getHeight() > DensityUtil.dp2px(getActivity(), 90)) && !isex) {
                    ViewGroup.LayoutParams params = iv.getLayoutParams();
                    params.height = DensityUtil.dp2px(getActivity(), 90);
                    iv.setLayoutParams(params);
                } else {
                    ViewGroup.LayoutParams params = iv.getLayoutParams();
                    params.height = DensityUtil.dp2px(getActivity(), 260);
                    iv.setLayoutParams(params);
                }
                lo = items.topLocation.get(pos).lng;
                la = items.topLocation.get(pos).lat;
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        String str = StreamReaderUtil.getString(getActivity(), "json");
        Gson gson = new Gson();
        items = gson.fromJson(str, AddressBean.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        if (adapter == null) {
            adapter = new HotAdapter(getActivity(), items.topLocation);
        }
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.image_save)
    public void onClick() {
        String url = SharedPreferencesUtil.getString(getActivity(),"url","");
        if (WriteImageGps.writeImageGps(lo,la,url)){
            SaveDialogFragment fragment = new SaveDialogFragment();
            fragment.show(getFragmentManager(),"SaveDialogFragment");
        }else {
            Toast.makeText(getActivity(),"修改失败",Toast.LENGTH_LONG).show();
        }
    }
}
