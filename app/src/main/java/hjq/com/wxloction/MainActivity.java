package hjq.com.wxloction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.sourceforge.jheader.App1Header;
import net.sourceforge.jheader.JpegFormatException;
import net.sourceforge.jheader.JpegHeaders;

import java.io.IOException;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv;
    private TextView tv;
    private final int REQUEST_IMAGE = 0x111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) this.findViewById(R.id.main_iv);
        tv = (TextView) this.findViewById(R.id.main_tv);

        tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MultiImageSelector.create()
                .showCamera(false)
                .single()
                .start(MainActivity.this,REQUEST_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_IMAGE){
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (path != null && path.size() > 0) {
                    String tmpAvatarLocalPath = path.get(0);
                    Glide.with(this)
                            .load(tmpAvatarLocalPath)
                            .into(iv);

                    /*try {
                        ExifInterface exifInterface = new ExifInterface(tmpAvatarLocalPath);
                        //经度
                        exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,"139");
                        //经度参考
                        exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF,"E");
                        //纬度
                        exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE,"35");
                        //纬度参考
                        exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF,"N");
                        exifInterface.setAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF,"海平面");
                        exifInterface.setAttribute(ExifInterface.TAG_GPS_ALTITUDE,"10");
                        exifInterface.setAttribute(ExifInterface.TAG_GPS_IMG_DIRECTION_REF,"真方位");
                        exifInterface.setAttribute(ExifInterface.TAG_GPS_IMG_DIRECTION,"0");
                        exifInterface.saveAttributes();

                        String la = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                        String lo = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                        Log.e("------>",la+"--"+lo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    try {
                        JpegHeaders jpegHeaders = new JpegHeaders(tmpAvatarLocalPath);
                        jpegHeaders.convertToExif();
                        App1Header app1Header = jpegHeaders.getApp1Header();
                        app1Header.setValue(App1Header.Tag.GPSINFO,"100");

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JpegFormatException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
    }
}
