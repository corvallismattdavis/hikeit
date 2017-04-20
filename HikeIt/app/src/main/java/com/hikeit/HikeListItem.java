package com.hikeit;

/**
 * Created by Jon on 4/12/2017.
 */

public class HikeListItem {
    /* ADD distance and move difficulty to overlay on image */
    public static enum Difficulty { Easy, Moderate, Hard, Vigorous };
    public String imgSrc = "empty_photo.jpg";
    public String title = "Hike Title";
    public Difficulty difficulty = Difficulty.Easy;
    public float rating = 1.0f;
    public float distance = 1.5f;

    public HikeListItem(String img, String title, Difficulty diff, float rating, float dist)
    {
        imgSrc = img;
        this.title = title;
        difficulty = diff;
        this.rating = rating;
        distance = dist;
    }
}
