package com.fbrd.rsc2015.ui.fragment;

import android.app.Fragment;

import com.dmacan.lightandroid.ui.custom.tabs.Tab;

/**
 * Created by david on 21.11.2015..
 */
public abstract class TabFragment extends Fragment implements Tab {

    private String label;
    private boolean isFocused;
    private boolean active;
    private OnActiveListener onActiveListener;

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

    public void setActive(boolean active) {
        this.active = active;
        if (onActiveListener != null)
            onActiveListener.onActive(active);
    }

    public void setOnActiveListener(OnActiveListener onActiveListener) {
        this.onActiveListener = onActiveListener;
    }

    public interface OnActiveListener {
        void onActive(boolean active);
    }

}
