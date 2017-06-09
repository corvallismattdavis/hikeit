package com.hikeit;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Matt on 6/7/2017.
 */

public class ReviewListItem implements Parcelable{

    public float rating = 5.0f;

    public ReviewListItem(float rate) {
        rating = rate;
    }

    public ReviewListItem(Parcel in){
        this.rating = in.readFloat();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(rating);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ReviewListItem createFromParcel(Parcel in) {
            return new ReviewListItem(in);
        }

        public ReviewListItem[] newArray(int size) {
            return new ReviewListItem[size];
        }
    };

}
