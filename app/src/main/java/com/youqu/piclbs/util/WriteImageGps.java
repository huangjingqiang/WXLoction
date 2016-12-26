package com.youqu.piclbs.util;

import android.location.Location;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

/**
 * Created by hujiang on 2016/12/26.
 */

public class WriteImageGps {
    //经度，纬度，图片地址
    public static boolean writeImageGps(String longitude,String latitude,String url){
        double lo = Double.valueOf(longitude);
        double la = Double.valueOf(latitude);
        try {
            ExifInterface exifInterface = new ExifInterface(url);
            //经度
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, gpsInfoConvert(lo));
            //经度参考
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lo > 0.0f ? "E" : "W");
            //纬度
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, gpsInfoConvert(la));
            //纬度参考
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, la > 0.0f ? "N" : "S");

            exifInterface.saveAttributes();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static String gpsInfoConvert(double gpsInfo) {
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
}
