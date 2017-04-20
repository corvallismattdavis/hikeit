package com.hikeit;

import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Jon on 4/12/2017.
 */
class HikeListAdapter extends BaseAdapter {

    Context context;
    HikeListItem[] data;
    private static LayoutInflater inflater = null;

    public HikeListAdapter(Context context, HikeListItem[] data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView) vi.findViewById(R.id.list_row_title);
        title.setText(data[position].title);

        TextView difficulty = (TextView) vi.findViewById(R.id.list_row_difficulty);
        difficulty.setText(data[position].difficulty.toString());

        TextView distance = (TextView) vi.findViewById(R.id.list_row_length);
        distance.setText((data[position].distance + " miles"));

        ImageView icon = (ImageView) vi.findViewById(R.id.list_row_img);
        icon.setImageResource(R.drawable.bishops);

        RatingBar rating = (RatingBar) vi.findViewById(R.id.list_row_rating);
        rating.setRating(data[position].rating);
        return vi;
    }
}
