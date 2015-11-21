package com.example.loginmodule.util;

import com.example.loginmodule.model.entity.User;
import com.example.loginmodule.model.response.GetUserResponse;
import com.example.loginmodule.service.app.AppServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.example.loginmodule.model.response.Response;

import retrofit.HttpException;
import rx.Observable;

/**
 * Created by david on 15.11.2015..
 */
public class ServiceUtil {

    private static Gson gson = new GsonBuilder().create();

    public static HttpException toHttpException(Throwable throwable) {
        return (HttpException) throwable;
    }

    public static int getResponseCode(Throwable throwable) {
        try {
            return toHttpException(throwable).code();
        } catch (Exception e) {
            return -1;
        }
    }

    public static int getStatusCode(Throwable throwable) {
        try {
            HttpException exception = toHttpException(throwable);
            Response response = gson.fromJson(exception.response().errorBody().string(), Response.class);
            return response.getStatus();
        } catch (Exception e) {
            return -1;
        }
    }

    public static String formatToken(String token) {
        return "Bearer " + token;
    }

    public static Observable<GetUserResponse> updateUser(String token, User user, AppServiceImpl.AppService service) {
        return service.updateUser(token,
                user.getId(),
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getImage());
    }

    public static Observable<GetUserResponse> updateUser(String token, User user, String regId, AppServiceImpl.AppService service) {
        return service.updateUser(token,
                user.getId(),
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                regId,
                user.getImage());
    }
}
