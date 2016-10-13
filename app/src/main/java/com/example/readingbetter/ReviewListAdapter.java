package com.example.readingbetter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReviewListAdapter extends BaseAdapter {
    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<ReviewInfo> reviewinfolist = null;
    private ArrayList<ReviewInfo> arraylist;

    public ReviewListAdapter(Context context, List<ReviewInfo> reviewinfolist) {
        mContext = context;
        this.reviewinfolist = reviewinfolist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<ReviewInfo>();
        this.arraylist.addAll(reviewinfolist);
    }

    public class ViewHolder {
        TextView writerID;
        TextView review;
        TextView writeDate;
    }

    @Override
    public int getCount() {
        return reviewinfolist.size();
    }

    @Override
    public ReviewInfo getItem(int position) {
        return reviewinfolist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.reviewlist_item, null);

            // Locate the TextViews in listview_item.xml
            holder.writerID = (TextView) view.findViewById(R.id.reviewWriter);
            holder.review = (TextView) view.findViewById(R.id.review);
            holder.writeDate = (TextView) view.findViewById(R.id.date);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews
        holder.writerID.setText(reviewinfolist.get(position).getWriterID());
        holder.review.setText(reviewinfolist.get(position).getReview());
        holder.writeDate.setText(reviewinfolist.get(position).getWriteDate());

        return view;
    }
}