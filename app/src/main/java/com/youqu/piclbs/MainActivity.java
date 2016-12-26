package com.youqu.piclbs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.youqu.piclbs.hot.HotFragment;
import com.youqu.piclbs.location.LocationFragment;
import com.youqu.piclbs.map.MapFragment;
import com.youqu.piclbs.util.MainFragmentAdapter;
import com.youqu.piclbs.util.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class MainActivity extends AppCompatActivity{
    @BindView(R.id.main_viewpager)
    ViewPager viewPager;
    private final int REQUEST_IMAGE = 0x111;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.main_search)
    RelativeLayout search;
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private MainFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        titles.add("热门地点");
        titles.add("位置标签");
        titles.add("位置选择");

        fragments.add(new HotFragment());
        fragments.add(new LocationFragment());
        fragments.add(new MapFragment());

        adapter = new MainFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (path != null && path.size() > 0) {

                }
            }
        }
    }

    @OnClick(R.id.main_search)
    public void onClick() {
        Intent intent = new Intent(MainActivity.this,SearchResultActivity.class);
        startActivity(intent);
    }
}
