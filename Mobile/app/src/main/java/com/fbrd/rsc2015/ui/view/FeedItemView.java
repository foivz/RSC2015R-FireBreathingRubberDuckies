package com.fbrd.rsc2015.ui.view;

import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.domain.model.response.FeedItem;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
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
    public void onViewInflated() {
        super.onViewInflated();
        ButterKnife.bind(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_feed;
    }

    @Override
    public void bind(FeedItem feedItem) {
        setOnClickListener(v -> notifyItemAction(1000, feedItem, v));
        Picasso.with(getContext()).load(feedItem.getImage()).into(imgFeedItem);
        txtFeedDescription.setText(feedItem.getDescription());
        txtFeedName.setText(feedItem.getName());
    }

}
