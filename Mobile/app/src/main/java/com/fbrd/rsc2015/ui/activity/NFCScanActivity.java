package com.fbrd.rsc2015.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginmodule.model.bus.ZET;
import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.domain.manager.NFCManager;
import com.fbrd.rsc2015.domain.service.NFCScannedEvent;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.halfbit.tinybus.Subscribe;

public class NFCScanActivity extends AppCompatActivity {

    @Bind(R.id.scan_nfc)
    TextView scanNfc;

    @Bind(R.id.nfc_disabled_enabled_text)
    TextView nfcDisabledEnabledText;

    NFCManager nfcManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcscan);
        ButterKnife.bind(this);
        nfcManager = new NFCManager(this);
        nfcManager.connect(success -> {
            if (success) {
                nfcManager.handleIntent(getIntent());
            }
        });
    }

    @Subscribe
    public void onNfcMessageRead(NFCScannedEvent event) {
        Toast.makeText(NFCScanActivity.this, event.getResult(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZET.register(this);
        nfcManager.setupForegroundDispatch(this, NFCScanActivity.class);
    }

    @Override
    protected void onStop() {
        ZET.unregister(this);
        nfcManager.stopForegroundDispatch();
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        nfcManager.handleIntent(intent);
    }

}
