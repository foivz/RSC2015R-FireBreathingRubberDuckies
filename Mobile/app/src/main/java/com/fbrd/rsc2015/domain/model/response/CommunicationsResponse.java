package com.fbrd.rsc2015.domain.model.response;

import com.example.loginmodule.model.response.Response;

import ru.noties.flatten.Flatten;
import ru.noties.flatten.Flattened;

/**
 * Created by noxqs on 21.11.15..
 */
public class CommunicationsResponse extends Response {

    @Flatten("data::url")
    Flattened<String> url;

    @Flatten("data::id")
    Flattened<Integer> id;

    @Flatten("data::created")
    Flattened<String> created;

    @Flatten("data::updated")
    Flattened<String> updated;

    public Flattened<String> getCreated() {
        return created;
    }

    public void setCreated(Flattened<String> created) {
        this.created = created;
    }

    public Flattened<String> getUrl() {
        return url;
    }

    public void setUrl(Flattened<String> url) {
        this.url = url;
    }

    public Flattened<Integer> getId() {
        return id;
    }

    public void setId(Flattened<Integer> id) {
        this.id = id;
    }


    public Flattened<String> getUpdated() {
        return updated;
    }

    public void setUpdated(Flattened<String> updated) {
        this.updated = updated;
    }
}
