package com.example.loginmodule.model.response;

import com.google.gson.annotations.SerializedName;

import ru.noties.flatten.Flatten;
import ru.noties.flatten.Flattened;

/**
 * Created by noxqs on 14.11.15..
 */
public class EmailTestResponse extends Response {

    public Flattened<String> getEmail() {
        return email;
    }

    public void setEmail(Flattened<String> email) {
        this.email = email;
    }

    @Flatten("email")
    @SerializedName("data")
    Flattened<String> email;
}
