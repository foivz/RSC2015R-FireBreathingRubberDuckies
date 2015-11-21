package com.fbrd.rsc2015.ui.activity;

import com.fbrd.rsc2015.R;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NFCScanActivity extends AppCompatActivity {

    @Bind(R.id.scan_nfc)
    TextView scanNfc;

    @Bind(R.id.nfc_disabled_enabled_text)
    TextView nfcDisabledEnabledText;

    private NfcAdapter adapter;

    private PendingIntent mPendingIntent;

    private NdefMessage mNdefPushMessage;

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcscan);
        ButterKnife.bind(this);

        adapter = NfcAdapter.getDefaultAdapter(this);

        if (adapter == null) {
            Toast.makeText(NFCScanActivity.this, "Device doesn't support NFC", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (adapter.isEnabled()){
            nfcDisabledEnabledText.setText(R.string.nfc_enabled_text);
        }
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, adapter);
    }

    private void setupForegroundDispatch(NFCScanActivity nfcScanActivity, NfcAdapter adapter) {
        final Intent intent = new Intent(nfcScanActivity.getApplicationContext(), nfcScanActivity.getClass());
    }

    @Override
    protected void onStop() {
        stopForegroundDispatch(this, adapter);
        super.onStop();

    }

    private void stopForegroundDispatch(NFCScanActivity nfcScanActivity, NfcAdapter adapter) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }
}