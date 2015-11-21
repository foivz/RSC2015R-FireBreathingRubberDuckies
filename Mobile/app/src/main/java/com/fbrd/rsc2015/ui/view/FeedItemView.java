package com.fbrd.rsc2015.ui.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.domain.model.FeedItem;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import io.nlopez.smartadapters.views.BindableLayout;

/**
 * Created by david on 21.11.2015..
 */
public class FeedItemView extends BindableLayout<FeedItem> {

    @Bind(R.id.imgFeedItem)
    ImageView imgFeedItem;
    @Bind(R.id.txtFeedName)
    TextView txtFeedName;
    @Bind(R.id.txtFeedDescription)
    TextView txtFeedDescription;

    public FeedItemView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_feed;
    }

    @Override
    public void bind(FeedItem feedItem) {
        Picasso.with(getContext()).load(feedItem.getImage()).into(imgFeedItem);
        txtFeedDescription.setText(feedItem.getDescription());
        txtFeedName.setText(feedItem.getName());
    }
}
