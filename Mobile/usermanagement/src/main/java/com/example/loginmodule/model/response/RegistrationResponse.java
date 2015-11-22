package com.example.loginmodule.model.response;

import ru.noties.flatten.Flatten;
import ru.noties.flatten.Flattened;

/**
 * Created by noxqs on 14.11.15..
 */
public class RegistrationResponse extends Response {

    @Flatten("data:email")
    Flattened<String> email;

    @Flatten("data:username")
    Flattened<String> username;

    @Flatten("data:registrationId")
    Flattened<Integer> registrationId;

    public Flattened<String> getEmail() {
        return email;
    }

    public void setEmail(Flattened<String> email) {
        this.email = email;
    }

    public Flattened<String> getUsername() {
        return username;
    }

    public void setUsername(Flattened<String> username) {
        this.username = username;
    }

    public Flattened<Integer> getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Flattened<Integer> registrationId) {
        this.registrationId = registrationId;
    }
}
