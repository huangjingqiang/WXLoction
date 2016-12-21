package com.youqu.piclbs;

import android.content.Intent;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youqu.piclbs.tencent.fence.DemoGeofenceEditorActivty;

import java.io.File;
import java.io.IOException;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv;
    private Button tv;
    private final int REQUEST_IMAGE = 0x111;
    private Button dtu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) this.findViewById(R.id.main_iv);
        tv = (Button) this.findViewById(R.id.main_tv);
        dtu = (Button) this.findViewById(R.id.ditu);

        tv.setOnClickListener(this);
        dtu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ditu:
                Intent intent = new Intent(this, DemoGeofenceEditorActivty.class);
                startActivity(intent);
                break;
            case R.id.main_tv:
                MultiImageSelector.create()
                        .showCamera(false)
                        .single()
                        .start(MainActivity.this, REQUEST_IMAGE);
                break;
        }


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
                    Glide.with(this)
                            .load(tmpAvatarLocalPath)
                            .into(iv);
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
}
