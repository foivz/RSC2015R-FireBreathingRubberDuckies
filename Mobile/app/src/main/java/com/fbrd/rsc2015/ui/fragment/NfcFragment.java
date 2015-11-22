package com.fbrd.rsc2015.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginmodule.model.bus.ZET;
import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.domain.service.NFCScannedEvent;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.halfbit.tinybus.Subscribe;

/**
 * Created by david on 21.11.2015..
 */
public class NfcFragment extends TabFragment {

    @Bind(R.id.txtNfc)
    TextView txtNfc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nfc, null, false);
        ButterKnife.bind(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
