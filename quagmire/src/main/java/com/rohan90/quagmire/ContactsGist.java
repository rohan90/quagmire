package com.rohan90.quagmire;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rohan on 10/11/17.
 */

public class ContactsGist implements Parcelable {
    @SerializedName("name")
    private String name;
    @SerializedName("phoneNumber")
    private String phoneNumber;

    public ContactsGist(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phoneNumber);
    }

    protected ContactsGist(Parcel in) {
        this.name = in.readString();
        this.phoneNumber = in.readString();
    }

    public static final Creator<ContactsGist> CREATOR = new Creator<ContactsGist>() {
        @Override
        public ContactsGist createFromParcel(Parcel source) {
            return new ContactsGist(source);
        }

        @Override
        public ContactsGist[] newArray(int size) {
            return new ContactsGist[size];
        }
    };
}
