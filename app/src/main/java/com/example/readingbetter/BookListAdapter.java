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

public class BookListAdapter extends BaseAdapter {
    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<BookInfo> bookInfoList = null;
    private ArrayList<BookInfo> arrayList;

    public BookListAdapter(Context context, List<BookInfo> bookinfolist) {
        mContext = context;
        this.bookInfoList = bookinfolist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<BookInfo>();
        this.arrayList.addAll(bookinfolist);
    }

    public class ViewHolder {
        TextView title;
        TextView recommend;
        WebView cover;
    }

    @Override
    public int getCount() {
        return bookInfoList.size();
    }

    @Override
    public BookInfo getItem(int position) {
        return bookInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.booklist_item, null);

            // Locate the TextViews in listview_item.xml
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.recommend = (TextView) view.findViewById(R.id.recommend);
            holder.cover = (WebView) view.findViewById(R.id.cover);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews
        holder.title.setText(bookInfoList.get(position).getTitle());
        holder.recommend.setText(bookInfoList.get(position).getRecommend());
        holder.cover.loadDataWithBaseURL(null,creHtmlBody(bookInfoList.get(position).getCover()), "text/html", "utf-8", null);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(mContext, BookInfoActivity.class);
                intent.putExtra("no", (bookInfoList.get(position).getNo()));
                intent.putExtra("title", (bookInfoList.get(position).getTitle()));
                intent.putExtra("recommend", (bookInfoList.get(position).getRecommend()));
                intent.putExtra("author", (bookInfoList.get(position).getAuthor()));
                intent.putExtra("publisher", (bookInfoList.get(position).getPublisher()));
                intent.putExtra("cover", (bookInfoList.get(position).getCover()));
                // Start SingleItemView Class
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        bookInfoList.clear();
        if (charText.length() == 0) {
            bookInfoList.addAll(arrayList);
        } else {
            for (BookInfo bi : arrayList) {
                if (bi.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    bookInfoList.add(bi);
                }
            }
        }
        notifyDataSetChanged();
    }

    // 웹뷰 정해진 크기에 딱 맞게 채우기
    public static String creHtmlBody(String imagUrl) {
        StringBuffer sb = new StringBuffer("<HTML>");
        sb.append("<HEAD>");
        sb.append("</HEAD>");
        sb.append("<BODY style='margin:0; padding:0; text-align:center;'>");    //중앙정렬
        sb.append("<img width='100%' height='100%' src=\"" + imagUrl+"\">"); //가득차게 나옴
        sb.append("</BODY>");
        sb.append("</HTML>");
        return sb.toString();
    }
}