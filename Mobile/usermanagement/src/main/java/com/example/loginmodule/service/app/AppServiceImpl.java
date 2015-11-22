package com.example.loginmodule.service.app;

import com.example.loginmodule.model.response.EmailTestResponse;
import com.example.loginmodule.model.response.GetUserResponse;
import com.example.loginmodule.model.response.LoginResponse;
import com.example.loginmodule.model.response.LoginSocialResponse;
import com.example.loginmodule.model.response.RegistrationResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import ru.noties.flatten.FlattenJsonDeserializer;
import ru.noties.flatten.Flattened;
import rx.Observable;

/**
 * Created by noxqs on 14.11.15..
 */
public class AppServiceImpl {

    private String endpoint;
    private AppService appService;

    public AppServiceImpl(String endpoint) {
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
        appService = adapter.create(AppService.class);
    }

    private Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Flattened.class,
                        new FlattenJsonDeserializer(
                                RegistrationResponse.class
                        ))
                .create();
    }

    public AppService getAppService() {
        return appService;
    }

    public interface AppService {

        @FormUrlEncoded
        @POST("/api/1/login")
        Observable<LoginResponse> login(@Field("username") String username,
                                        @Field("password") String password,
                                        @Field("grant_type") String grantType);

        @FormUrlEncoded
        @POST("/api/1/register")
        Observable<RegistrationResponse> register(@Field("email") String email,
                                                  @Field("username") String username,
                                                  @Field("password") String password,
                                                  @Field("firstName") String firstName,
                                                  @Field("lastName") String lastName,
                                                  @Field("registrationId") String registrationId);

        @FormUrlEncoded
        @POST("/auth/facebook")
        Observable<LoginSocialResponse> loginFacebook(@Field("accessToken") String accessToken, @Field("registrationId") String regId);

        @FormUrlEncoded
        @POST("/api/1/test/email")
        Observable<EmailTestResponse> testEmail(@Field("email") String email);

        @GET("/api/1/users/{id}")
        Observable<GetUserResponse> getUser(@Header("Authorization") String token, @Path("id") String id);

        @FormUrlEncoded
        @POST("/auth/google")
        Observable<LoginSocialResponse> loginGoogle(@Field("code") String code, @Field("registrationId") String regId);

        @FormUrlEncoded
        @PUT("/api/1/users/{id}")
        Observable<GetUserResponse> updateUser(@Header("Authorization") String token,
                                               @Path("id") String idPath,
                                               @Field("id") String id,
                                               @Field("email") String email,
                                               @Field("username") String username,
                                               @Field("firstName") String firstName,
                                               @Field("lastName") String lastName,
                                               @Field("registrationId") String registrationId,
                                               @Field("avatar") String avatar);

        @FormUrlEncoded
        @PUT("/api/1/users/{id}")
        Observable<GetUserResponse> updateUser(@Header("Authorization") String token,
                                               @Path("id") String idPath,
                                               @Field("id") String id,
                                               @Field("email") String email,
                                               @Field("username") String username,
                                               @Field("firstName") String firstName,
                                               @Field("lastName") String lastName,
                                               @Field("avatar") String avatar);
    }
}
