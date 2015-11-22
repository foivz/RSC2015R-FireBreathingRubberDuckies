package com.fbrd.rsc2015.domain.model.response;

import com.example.loginmodule.model.response.Response;

import java.util.List;

/**
 * Created by noxqs on 21.11.15..
 */
public class LocationResponse extends Response {

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {

        private String id;
        private String firstName;
        private String lastName;
        private double longitude;
        private double latitude;
        private String userName;
        private String email;
        private Object avatar;
        private boolean killed;
        private boolean banned;
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

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(int longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
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

        public String getNfc() {
            return nfc;
        }

        public void setNfc(String nfc) {
            this.nfc = nfc;
        }
    }

}
