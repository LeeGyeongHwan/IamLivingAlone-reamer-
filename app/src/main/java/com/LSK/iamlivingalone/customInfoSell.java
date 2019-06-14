package com.LSK.iamlivingalone;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

public class customInfoSell implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public customInfoSell(Context ctx){
        context = ctx;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Object tagObject = marker.getTag();

        if (tagObject instanceof  Memo) {
            Memo memo = (Memo) tagObject;
            View view = ((Activity)context).getLayoutInflater()
                    .inflate(R.layout.custommarker_memo, null);
            TextView title = view.findViewById(R.id.memotitle);
            TextView content = view.findViewById(R.id.memocontent);
            title.setText(marker.getTitle());
            content.setText(marker.getSnippet());

            return view;

        } else if (tagObject instanceof  Conversation) {
            Conversation conversation = (Conversation) tagObject;
            View view = ((Activity)context).getLayoutInflater()
                    .inflate(R.layout.custommarker_conver, null);
            TextView title = view.findViewById(R.id.convertitle);
            TextView content = view.findViewById(R.id.convercontent);
            title.setText(marker.getTitle());
            content.setText(marker.getSnippet());

            return view;

        } else if (tagObject instanceof  Sell){
            Sell sell = (Sell)tagObject;
            View view = ((Activity)context).getLayoutInflater()
                    .inflate(R.layout.custommarker_sell, null);
            ImageView sellimage= view.findViewById(R.id.sellPic);
            TextView title= view.findViewById(R.id.customtxt);
            TextView content= view.findViewById(R.id.customcontent);
            title.setText(marker.getTitle());
            content.setText(marker.getSnippet());
            Picasso.get().load(sell.uri).into(sellimage);

            return view;
        }






        return null;
    }
}
