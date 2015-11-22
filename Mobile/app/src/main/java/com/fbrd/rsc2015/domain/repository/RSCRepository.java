package com.fbrd.rsc2015.domain.repository;

import com.fbrd.rsc2015.domain.model.response.PairingResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.example.loginmodule.model.response.RegistrationResponse;
import com.fbrd.rsc2015.domain.model.response.CommunicationsResponse;
import com.fbrd.rsc2015.domain.model.response.FeedResponse;
import com.fbrd.rsc2015.domain.model.response.GamesResponse;
import com.fbrd.rsc2015.domain.model.response.LocationResponse;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
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

        @GET("/api/1/notifications")
        Observable<FeedResponse> fetchNotifications(@Header("Authorization") String token);

        @FormUrlEncoded
        @PUT("/api/1/games")
        Observable<LocationResponse> pushLocation(@Header("Authorization") String token, @Field("lat") double lat, @Field("long") double lon, @Field("mapId") long mapId);

        @GET("/api/1/games/{id}")
        Observable<GamesResponse> getGame(@Header("Authorization") String token, @Path("id") long gameId);

        @FormUrlEncoded
        @PUT("/api/1/games/nfc")
        Observable<PairingResponse> pair(@Header("Authorization") String token, @Field("nfc") String nfc, @Field("gameId") long gameId);

    }

}
