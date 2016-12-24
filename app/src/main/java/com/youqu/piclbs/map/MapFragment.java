package com.youqu.piclbs.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.map.geolocation.TencentGeofence;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.youqu.piclbs.R;
import com.youqu.piclbs.tencent.fence.DemoGeofenceApp;
import com.youqu.piclbs.tencent.fence.DemoGeofenceService;
import com.youqu.piclbs.tencent.fence.LocationHelper;
import com.youqu.piclbs.tencent.fence.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hjq on 16-12-22.
 */

public class MapFragment extends Fragment implements View.OnTouchListener {

    @BindView(R.id.empty)
    View empty;
    @BindView(R.id.my_loc)
    Button myLoc;
    @BindView(R.id.add)
    Button add;
    @BindView(R.id.del)
    Button del;
    @BindView(R.id.stop)
    Button stop;
    @BindView(R.id.button_container)
    LinearLayout buttonContainer;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.center)
    ImageView center;
    @BindView(R.id.position)
    TextView mPosition;
    @BindView(R.id.top_container)
    RelativeLayout topContainer;
    @BindView(R.id.geofence_list)
    ListView mFenceList;

    private LocationHelper mLocationHelper;
    private TencentMap mTencentMap;
    private ArrayAdapter<TencentGeofence> mFenceListAdapter;
    private List<Marker> mFenceItems;
    private final Location mCenter = new Location("");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, rootView);

        mLocationHelper = new LocationHelper(getActivity());
        initUi();
        return rootView;
    }

    @OnClick({R.id.my_loc,R.id.add,R.id.del,R.id.stop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_loc: // 获取当前位置
                doMyLoc();
                break;

            case R.id.add: // 添加新的围栏
                doPreAdd();
                break;
            case R.id.del: // 删除选中的围栏
                int selected = mFenceList.getCheckedItemPosition();
                doDel(selected);
                break;

            case R.id.stop:
                DemoGeofenceService.stopMe(getActivity()); // 停止测试
                break;

            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    private void initUi() {
        // poi item & poi overlay
        mFenceItems = DemoGeofenceApp.getFenceItems();

        // mapview
        mTencentMap = mMapView.getMap();
        mMapView.setOnTouchListener(this);

        mFenceListAdapter = new ArrayAdapter<TencentGeofence>(getActivity(),
                android.R.layout.simple_list_item_checked,
                DemoGeofenceApp.getFence()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TencentGeofence geofence = getItem(position);
                TextView tv = (TextView) super.getView(position, convertView,
                        parent);
                tv.setText(Utils.toString(geofence));
                return tv;
            }
        };
        mFenceList.setAdapter(mFenceListAdapter);
        mFenceList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        updatePosition();
    }

    private void updatePosition() {
        double lat = mTencentMap.getMapCenter().getLatitude();
        double lng = mTencentMap.getMapCenter().getLongitude();

        mPosition.setText(lat + "," + lng);
        mCenter.setLatitude(lat);
        mCenter.setLongitude(lng);
    }

    private void animateTo(TencentLocation location) {
        if (location == null) {
            return;
        }
        mMapView.getController().animateTo(Utils.of(location));
        // 修改 mapview 中心点
        mMapView.getController().setCenter(Utils.of(location));
        // 注意一定要更新当前位置 mCenter
        updatePosition();
    }

    // 生成 poi item
    private Marker createPoiItem(TencentGeofence geofence) {
        Marker marker = mTencentMap.addMarker(new MarkerOptions().
                position(new LatLng(geofence.getLatitude(), geofence.getLongitude())).
                title(geofence.getTag()).
                snippet(Utils.fmt(geofence.getLatitude()) + ","
                        + Utils.fmt(geofence.getLongitude()) + ","
                        + geofence.getRadius()));
        return marker;
    }

    // ============== util methods

    static void toast(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            updatePosition();
        }
        return false;
    }

    private void doMyLoc() {
        if (mLocationHelper.getLastLocation() != null) {
            animateTo(mLocationHelper.getLastLocation()); // 已有最新位置
        } else if (mLocationHelper.isStarted()) {
            toast(getActivity(), "正在定位"); // 当前正在定位
        } else {
            toast(getActivity(), "开始定位");
            mLocationHelper.start(new Runnable() {
                public void run() {
                    animateTo(mLocationHelper.getLastLocation());
                }
            });
        }
    }

    private void doPreAdd() {
        View root = getActivity().getLayoutInflater().inflate(R.layout.dialog_geofence, null);
        TextView tvLocation = (TextView) root.findViewById(R.id.location);
        tvLocation.setText(Utils.toString(mCenter));

        new AlertDialog.Builder(getActivity()).setTitle("保存围栏").setView(root)
                .setPositiveButton("确定", new AddGeofenceOnClickListener(root))
                .setNegativeButton("取消", null).show();
    }

    private void doAdd(String tag) {
        toast(getActivity(), tag);

        double lat = mCenter.getLatitude();
        double lng = mCenter.getLongitude();
        // 创建地理围栏
        TencentGeofence.Builder builder = new TencentGeofence.Builder();
        TencentGeofence geofence = builder.setTag(tag) // 设置 Tag
                .setCircularRegion(lat, lng, 500) // 设置中心点和半径
                .setExpirationDuration(3 * 3600 * 1000) // 设置有效期
                .build();
        // 更新 adapter view
        mFenceListAdapter.add(geofence);

        // 更新 overlay
        mFenceItems.add(createPoiItem(geofence));

        // 添加地理围栏
        DemoGeofenceService.startMe(getActivity(),
                DemoGeofenceService.ACTION_ADD_GEOFENCE, tag);
    }

    private void doDel(int selected) {
        if (selected == ListView.INVALID_POSITION || selected >= mFenceListAdapter.getCount()) {
            toast(getActivity(), "没有选中");
            return;
        }

        // 更新 adapter view
        TencentGeofence item = mFenceListAdapter.getItem(selected);
        mFenceListAdapter.remove(item);
        // 更新 marker lists
        mFenceItems.remove(selected).remove();

        // 移除地理围栏
        DemoGeofenceService.startMe(getActivity(),
                DemoGeofenceService.ACTION_DEL_GEOFENCE, item.getTag());
    }

    public class AddGeofenceOnClickListener implements DialogInterface.OnClickListener {

        private View mView;

        public AddGeofenceOnClickListener(View view) {
            super();
            this.mView = view;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (getActivity().isFinishing()) {
                return;
            }

            EditText etName = (EditText) mView.findViewById(R.id.name);
            String name = etName.getText().toString();

            if (!TextUtils.isEmpty(name)) {
                doAdd(name);
            } else {
                toast(getActivity(), "围栏名字不能为空");
            }
        }
    }

}
