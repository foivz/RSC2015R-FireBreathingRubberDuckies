package com.fbrd.rsc2015.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.loginmodule.model.bus.ZET;
import com.example.loginmodule.util.ServiceUtil;
import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.app.di.component.DaggerVoiceChatComponent;
import com.fbrd.rsc2015.app.di.module.ApiModule;
import com.fbrd.rsc2015.domain.model.event.CommunicationsFailureEvent;
import com.fbrd.rsc2015.domain.model.event.CommunicationsSuccessEvent;
import com.fbrd.rsc2015.domain.repository.RSCRepository;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.halfbit.tinybus.Subscribe;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VoiceChatActivity extends AppCompatActivity {

    public static final String FAILED_FETCH = "FAILED_FETCH";

    @Bind(R.id.communications_web_view)
    WebView communicationsWebView;

    @Inject
    public RSCRepository.Api appService;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_chat);
        ButterKnife.bind(this);

        DaggerVoiceChatComponent.builder()
                .apiModule(new ApiModule())
                .build()
                .inject(this);

        fetchUrl();
    }

    @Subscribe
    public void onUrlLoaded(CommunicationsSuccessEvent event){
        if(url != null){
            Log.e("URLFETCHED", url);
            communicationsWebView.loadUrl(url);
        }
        else{
            Log.e(FAILED_FETCH, "Url is null"); //hardcoded string I know, fuck it.
        }
        WebSettings settings = communicationsWebView.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZET.register(this);
    }

    private void fetchUrl() {
        appService.testComms()
                .map(comms -> {
                    url = comms.getUrl().get();
                    return url;
                })
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        commsUrl -> ZET.post(new CommunicationsSuccessEvent(url)),
                        commsError -> ZET.post(new CommunicationsFailureEvent(ServiceUtil.getStatusCode(commsError)))
                );
    }


    @Override
    protected void onPause() {
        super.onPause();
        ZET.unregister(this);
    }
}
