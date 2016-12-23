package com.youqu.piclbs.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hjq on 16-12-22.
 */

public class AddressBean implements Parcelable {
    public List<TopLocationBean> topLocation;
    public List<CategoryBean> category;

    @Override
    public String toString() {
        return "AddressBean{" +
                "topLocation=" + topLocation +
                ", category=" + category +
                '}';
    }

    public static class TopLocationBean implements Parcelable {
        /**
         * name : 四川峨眉山
         * location : 中国四川省乐山市峨眉山市
         * lat : 29.30
         * lng : 103.20
         * category : 名胜古迹
         */
        public String name;
        public String location;
        public String lat;
        public String lng;
        public String category;

        @Override
        public String toString() {
            return "TopLocationBean{" +
                    "name='" + name + '\'' +
                    ", location='" + location + '\'' +
                    ", lat='" + lat + '\'' +
                    ", lng='" + lng + '\'' +
                    ", category='" + category + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.location);
            dest.writeString(this.lat);
            dest.writeString(this.lng);
            dest.writeString(this.category);
        }

        public TopLocationBean() {
        }

        protected TopLocationBean(Parcel in) {
            this.name = in.readString();
            this.location = in.readString();
            this.lat = in.readString();
            this.lng = in.readString();
            this.category = in.readString();
        }

        public static final Parcelable.Creator<TopLocationBean> CREATOR = new Parcelable.Creator<TopLocationBean>() {
            @Override
            public TopLocationBean createFromParcel(Parcel source) {
                return new TopLocationBean(source);
            }

            @Override
            public TopLocationBean[] newArray(int size) {
                return new TopLocationBean[size];
            }
        };
    }

    public static class CategoryBean implements Parcelable {
        /**
         * category : 名胜古迹
         * location : [{"name":"四川峨眉山","location":"中国四川省乐山市峨眉山市","lat":"29.30","lng":"103.20","category":"名胜古迹"},{"name":"四川峨眉山","location":"中国四川省乐山市峨眉山市","lat":"29.30","lng":"103.20","category":"名胜古迹"}]
         */
        public String category;
        public List<LocationBean> location;

        @Override
        public String toString() {
            return "CategoryBean{" +
                    "category='" + category + '\'' +
                    ", location=" + location +
                    '}';
        }

        public static class LocationBean implements Parcelable {
            /**
             * name : 四川峨眉山
             * location : 中国四川省乐山市峨眉山市
             * lat : 29.30
             * lng : 103.20
             * category : 名胜古迹
             */
            public String name;
            public String location;
            public String lat;
            public String lng;
            public String category;

            @Override
            public String toString() {
                return "LocationBean{" +
                        "name='" + name + '\'' +
                        ", location='" + location + '\'' +
                        ", lat='" + lat + '\'' +
                        ", lng='" + lng + '\'' +
                        ", category='" + category + '\'' +
                        '}';
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.name);
                dest.writeString(this.location);
                dest.writeString(this.lat);
                dest.writeString(this.lng);
                dest.writeString(this.category);
            }

            public LocationBean() {
            }

            protected LocationBean(Parcel in) {
                this.name = in.readString();
                this.location = in.readString();
                this.lat = in.readString();
                this.lng = in.readString();
                this.category = in.readString();
            }

            public static final Parcelable.Creator<LocationBean> CREATOR = new Parcelable.Creator<LocationBean>() {
                @Override
                public LocationBean createFromParcel(Parcel source) {
                    return new LocationBean(source);
                }

                @Override
                public LocationBean[] newArray(int size) {
                    return new LocationBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.category);
            dest.writeTypedList(this.location);
        }

        public CategoryBean() {
        }

        protected CategoryBean(Parcel in) {
            this.category = in.readString();
            this.location = in.createTypedArrayList(LocationBean.CREATOR);
        }

        public static final Parcelable.Creator<CategoryBean> CREATOR = new Parcelable.Creator<CategoryBean>() {
            @Override
            public CategoryBean createFromParcel(Parcel source) {
                return new CategoryBean(source);
            }

            @Override
            public CategoryBean[] newArray(int size) {
                return new CategoryBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.topLocation);
        dest.writeTypedList(this.category);
    }

    public AddressBean() {
    }

    protected AddressBean(Parcel in) {
        this.topLocation = in.createTypedArrayList(TopLocationBean.CREATOR);
        this.category = in.createTypedArrayList(CategoryBean.CREATOR);
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
