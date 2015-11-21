package com.fbrd.rsc2015.ui.activity;

import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.domain.service.NdefReaderTask;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask(this).execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask(this).execute(tag);
                    break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, adapter);
    }

    private void setupForegroundDispatch(NFCScanActivity nfcScanActivity, NfcAdapter adapter) {
        final Intent intent = new Intent(nfcScanActivity.getApplicationContext(), nfcScanActivity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(nfcScanActivity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(nfcScanActivity, pendingIntent, filters, techList);
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
