package com.fbrd.rsc2015.domain.repository;

import com.example.loginmodule.model.response.RegistrationResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.fbrd.rsc2015.domain.model.CommunicationsResponse;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import ru.noties.flatten.FlattenJsonDeserializer;
import ru.noties.flatten.Flattened;
import rx.Observable;

/**
 * Created by david on 21.11.2015..
 */
public class RSCRepository {

    private String endpoint;
    private Api appService;

    public RSCRepository(String endpoint) {
        this.endpoint = endpoint;
        init();
    }

    private void init() {
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(endpoint)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        appService = adapter.create(Api.class);
    }

    private Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Flattened.class,
                        new FlattenJsonDeserializer(
                                RegistrationResponse.class
                        ))
                .create();
    }

    public Api getAppService() {
        return appService;
    }

    public interface Api {

        @GET("/api/1/test/webrtc")
        Observable<CommunicationsResponse> testComms();
    }

}
