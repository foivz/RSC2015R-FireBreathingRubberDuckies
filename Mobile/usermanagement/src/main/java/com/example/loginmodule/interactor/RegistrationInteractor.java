package com.example.loginmodule.interactor;

import java.io.File;

/**
 * Created by noxqs on 14.11.15..
 */
public interface RegistrationInteractor {

    void register(String email, String username, String password, String firstName, String lastName, String registrationId);

    void register(String email, String username, String password, String firstName, String lastName, String registrationId, File avatar);

}
