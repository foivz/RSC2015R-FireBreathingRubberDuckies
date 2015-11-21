package com.fbrd.rsc2015.domain.model.request;

/**
 * Created by noxqs on 21.11.15..
 */
public class LocationModel {

    private double latitude;
    private double longitude;
    private int mapId;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
}
