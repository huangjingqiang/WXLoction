package com.youqu.piclbs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjq on 16-12-22.
 */

public class AddressBean implements Parcelable {
    public String title;   //地名
    public String address; //地址
    public String longitude; //进度
    public String latitude;  //纬度


    @Override
    public String toString() {
        return "AddressBean{" +
                "title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.address);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
    }

    public AddressBean() {
    }

    protected AddressBean(Parcel in) {
        this.title = in.readString();
        this.address = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
    }

    public static final Parcelable.Creator<AddressBean> CREATOR = new Parcelable.Creator<AddressBean>() {
        @Override
        public AddressBean createFromParcel(Parcel source) {
            return new AddressBean(source);
        }

        @Override
        public AddressBean[] newArray(int size) {
            return new AddressBean[size];
        }
    };
}
