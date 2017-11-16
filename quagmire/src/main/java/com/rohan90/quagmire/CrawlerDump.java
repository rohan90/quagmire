package com.rohan90.quagmire;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohan on 10/11/17.
 */

public class CrawlerDump<T> implements Parcelable {
    @SerializedName("dataType")
    private String type;
    @SerializedName("data")
    private List<T> data;

    private String className;

    public CrawlerDump(String type, List<T> data, String className) {
        this.type = type;
        this.data = data;
        this.className= className;
    }

    public CrawlerDump(String type, List<T> data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public List<T> getData() {
        return data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.className);
        dest.writeList(this.data);
    }

    protected CrawlerDump(Parcel in) {
        this.type = in.readString();
        this.className = in.readString();
        this.data = new ArrayList<T>();
        try {
            in.readList(this.data, Class.forName(this.className).getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<CrawlerDump> CREATOR = new Creator<CrawlerDump>() {
        @Override
        public CrawlerDump createFromParcel(Parcel source) {
            return new CrawlerDump(source);
        }

        @Override
        public CrawlerDump[] newArray(int size) {
            return new CrawlerDump[size];
        }
    };

}
