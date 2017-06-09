package com.hikeit;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Jon on 4/12/2017.
 */

public class HikeListItem implements Parcelable, Comparable<HikeListItem> {
    /* ADD distance and move difficulty to overlay on image */
    public static enum Difficulty { Easy, Moderate, Hard, Vigorous };
    public ArrayList<String> imgSrc;
    public String title = "Hike Title";
    public Difficulty difficulty = Difficulty.Easy;
    public float rating = 1.0f;
    public float distance = 1.5f;
    public float lat = 30;
    public float lg = -120;
    public long count = 1; //count represents the numOfRatings in the DB

    public Bitmap picture;

    public HikeListItem(ArrayList<String> img, String title, Difficulty diff, float rating, float dist, float lati, float lgi, long cnt)
    {
        imgSrc = img;
        this.title = title;
        difficulty = diff;
        this.rating = rating;
        distance = dist;
        lat = lati;
        lg = lgi;
        count = cnt;

    }

    public HikeListItem(Parcel in){
        in.readList(this.imgSrc, null);
        this.title = in.readString();
        this.difficulty = Difficulty.valueOf(in.readString());
        this.rating = in.readFloat();
        this.distance = in.readFloat();
        this.lat = in.readFloat();
        this.lg = in.readFloat();
        this.count = in.readInt();
    }

    public static Comparator<HikeListItem> COMPARE_BY_CLOSE_TO_ME = new Comparator<HikeListItem>() {
        public int compare(HikeListItem one, HikeListItem other) {
            return distanceFromMe(one).compareTo(distanceFromMe(other));
        }
    };



    public int compareTo(HikeListItem other)
    {
        return distanceFromMe(this).compareTo(distanceFromMe(other));
    }

    public static Double distanceFromMe(HikeListItem p) {
        double theta = p.lg - (-120.6596200);
        double dist = Math.sin(deg2rad(p.lat)) * Math.sin(deg2rad(35.2827500))
                + Math.cos(deg2rad(p.lat)) * Math.cos(deg2rad(35.2827500))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        return dist;
    }

    public static double deg2rad(double deg) { return (deg * Math.PI / 180.0); }
    public static double rad2deg(double rad) { return (rad * 180.0 / Math.PI); }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(imgSrc);
        dest.writeString(title);
        dest.writeString(difficulty.toString());
        dest.writeFloat(rating);
        dest.writeFloat(distance);
        dest.writeFloat(lat);
        dest.writeFloat(lg);
        dest.writeLong(count);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public HikeListItem createFromParcel(Parcel in) {
            return new HikeListItem(in);
        }

        public HikeListItem[] newArray(int size) {
            return new HikeListItem[size];
        }
    };
}
