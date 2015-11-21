package com.fbrd.rsc2015.domain.model.request;

/**
 * Created by noxqs on 21.11.15..
 */
public class LocationModel {

    private String latitude;
    private String longitude;
    private int mapId;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
}
