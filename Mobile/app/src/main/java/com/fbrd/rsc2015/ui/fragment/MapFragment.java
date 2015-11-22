package com.fbrd.rsc2015.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmacan.lightandroid.ui.custom.view.SexyWebView;
import com.fbrd.rsc2015.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by david on 21.11.2015..
 */
public class MapFragment extends TabFragment {

    @Bind(R.id.wvMap)
    SexyWebView map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, null, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActive(true);
//        map.load(url);
    }

    public void showMap(String url) {
        map.load(url);
    }

}
