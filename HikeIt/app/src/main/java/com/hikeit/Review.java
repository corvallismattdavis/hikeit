package com.hikeit;

import java.util.Date;

/**
 * Created by JonPC on 6/8/2017.
 */

public class Review {
    String date;
    long rating;
    String user;
    String review;

    public Review(String d, long rate, String user, String review)
    {
        this.date = d;
        this.rating = rate;
        this.user = user;
        this.review = review;
    }

    public String toString()
    {
        return date + " " + Long.toString(rating) + " " + user + " " + review;
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof Review) {
            Review rev = (Review) obj;
            return (rev.date.equals(this.date) &&
                    rev.rating == this.rating &&
                    rev.user.equals(this.user) &&
                    rev.equals(this.review));
        } else {
            return false;
        }
    }
}
