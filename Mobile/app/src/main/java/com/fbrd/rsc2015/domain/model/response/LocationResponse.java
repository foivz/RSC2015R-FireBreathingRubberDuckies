package com.fbrd.rsc2015.domain.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.example.loginmodule.model.response.Response;

/**
 * Created by noxqs on 21.11.15..
 */
public class LocationResponse extends Response {


    class Data {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("longitude")
        @Expose
        private int longitude;
        @SerializedName("latitude")
        @Expose
        private int latitude;
        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("avatar")
        @Expose
        private Object avatar;
        @SerializedName("killed")
        @Expose
        private boolean killed;
        @SerializedName("banned")
        @Expose
        private boolean banned;
        @SerializedName("enabled")
        @Expose
        private boolean enabled;
        @SerializedName("nfc")
        @Expose
        private String nfc;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public int getLongitude() {
            return longitude;
        }

        public void setLongitude(int longitude) {
            this.longitude = longitude;
        }

        public int getLatitude() {
            return latitude;
        }

        public void setLatitude(int latitude) {
            this.latitude = latitude;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getAvatar() {
            return avatar;
        }

        public void setAvatar(Object avatar) {
            this.avatar = avatar;
        }

        public boolean isKilled() {
            return killed;
        }

        public void setKilled(boolean killed) {
            this.killed = killed;
        }

        public boolean isBanned() {
            return banned;
        }

        public void setBanned(boolean banned) {
            this.banned = banned;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getNfc() {
            return nfc;
        }

        public void setNfc(String nfc) {
            this.nfc = nfc;
        }
    }

}
