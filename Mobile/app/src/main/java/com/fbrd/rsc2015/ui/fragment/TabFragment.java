package com.fbrd.rsc2015.ui.fragment;

import com.dmacan.lightandroid.ui.custom.tabs.Tab;

import android.app.Fragment;

/**
 * Created by david on 21.11.2015..
 */
public abstract class TabFragment extends Fragment implements Tab {

    private String label;
    private boolean isFocused;

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void onFocusChanged(boolean b) {
        isFocused = b;
    }
}
