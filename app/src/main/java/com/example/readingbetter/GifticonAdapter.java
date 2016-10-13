package com.example.readingbetter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;

import java.util.ArrayList;
import java.util.List;

public class GifticonAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<Gifticon> gifticonList = null;
    private ArrayList<Gifticon> arrayList;

    private String loginId;

    public GifticonAdapter(Context context, List<Gifticon> gifticonList) {
        mContext = context;
        this.gifticonList = gifticonList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Gifticon>();
        this.arrayList.addAll(gifticonList);
    }

    public class ViewHolder {
        TextView gifticon;
        ImageView barcode;
        ImageView kakaotalk;
    }

    @Override
    public int getCount() {
        return gifticonList.size();
    }

    @Override
    public Gifticon getItem(int position) {
        return gifticonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.gifticonlist_item, null);

            // Locate the TextViews in listview_item.xml
            holder.gifticon = (TextView) view.findViewById(R.id.gifticon);
            holder.barcode = (ImageView) view.findViewById(R.id.barcode);
            holder.kakaotalk = (ImageView) view.findViewById(R.id.kakaotalk);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews
        holder.gifticon.setText(gifticonList.get(position).getTitle());

        holder.barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = (View) View.inflate(mContext, R.layout.gifticon_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(mContext);
                dlg.setView(dialogView);
                WebView webView = (WebView) dialogView.findViewById(R.id.cover);
                webView.loadData(BookListAdapter.creHtmlBody(gifticonList.get(position).getCover()), "text/html", "utf-8");
                dlg.setTitle(gifticonList.get(position).getTitle());
                dlg.setPositiveButton("확인", null);
                dlg.show();
            }
        });

        holder.kakaotalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    KakaoLink kakaoLink = KakaoLink.getKakaoLink(mContext);
                    KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

                    loginId = LoginActivity.setting.getString("id", null);

                    String text = loginId + "님이\nReadingBetter에서\n기프티콘을 보내셨습니다!";

                    final String linkContents = kakaoTalkLinkMessageBuilder
                            .addImage(gifticonList.get(position).getCover(), 250, 350)
                            .addText(text)
                            .addAppButton("앱으로 이동")
                            .build();

                    kakaoLink.sendMessage(linkContents, mContext);

                } catch (KakaoParameterException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}
