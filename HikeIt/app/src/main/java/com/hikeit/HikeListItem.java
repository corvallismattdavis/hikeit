package com.hikeit;

/**
 * Created by Jon on 4/12/2017.
 */

public class HikeListItem {
    public static enum Difficulty { Easy, Moderate, Hard, Extreme };
    public String imgSrc = "empty_photo.jpg";
    public String title = "Hike Title";
    public Difficulty difficulty = Difficulty.Easy;
    public float rating = 1.0f;

    public HikeListItem(String img, String title, Difficulty diff, float rating)
    {
        imgSrc = img;
        this.title = title;
        difficulty = diff;
        this.rating = rating;
    }
}
