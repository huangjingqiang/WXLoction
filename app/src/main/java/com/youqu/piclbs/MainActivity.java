package com.youqu.piclbs;

import android.content.Intent;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.youqu.piclbs.hot.HotFragment;
import com.youqu.piclbs.location.LocationFragment;
import com.youqu.piclbs.map.MapFragment;
import com.youqu.piclbs.util.ImageFormatUtil;
import com.youqu.piclbs.util.MainFragmentAdapter;
import com.youqu.piclbs.util.SlidingTabLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
    public void onClick(View view) {
        MultiImageSelector.create()
                .showCamera(false)
                .single()
                .start(MainActivity.this, REQUEST_IMAGE);
    }

    private String gpsInfoConvert(double gpsInfo) {
        gpsInfo = Math.abs(gpsInfo);
        String dms = Location.convert(gpsInfo, Location.FORMAT_SECONDS);
        String[] splits = dms.split(":");
        String[] secnds = (splits[2]).split(".");
        String seconds;
        if (secnds.length == 0) {
            seconds = splits[2];
        } else {
            seconds = secnds[0];
        }
        return splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";
    }

    public void writeGpsImage(String uri) {

        try {
            ExifInterface exifInterface = new ExifInterface(uri);
            double longitude = 139.0;
            double latitude = 35.0;
            //经度
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, this.gpsInfoConvert(longitude));
            //经度参考
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, longitude > 0.0f ? "E" : "W");
            //纬度
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, this.gpsInfoConvert(latitude));
            //纬度参考
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latitude > 0.0f ? "N" : "S");

            exifInterface.saveAttributes();

            ExifInterface newExifInterface = null;
            newExifInterface = new ExifInterface(uri);
            String la = newExifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String lo = newExifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            Log.e("------>", la + "--" + lo + "---" + newExifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("--------->", "修改失败");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (path != null && path.size() > 0) {
                    final String tmpAvatarLocalPath = path.get(0);


                    try {
                        ExifInterface exifInterface = new ExifInterface(tmpAvatarLocalPath);
                        Log.e("-------1>", exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE) + "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String url = ImageFormatUtil.getImageUri(this, tmpAvatarLocalPath);
                    File file = new File(url);
                    if (!file.exists()) {
                        Toast.makeText(this, "图片不存在", Toast.LENGTH_LONG).show();
                    } else {
                        writeGpsImage(url);
                    }

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
