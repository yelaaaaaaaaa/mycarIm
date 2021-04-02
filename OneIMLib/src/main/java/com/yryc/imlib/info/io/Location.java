package com.yryc.imlib.info.io;

/**
 * @author : sklyand
 * @email :
 * @time : 2020/4/22 11:45
 * @describe ：
 */
public class Location {
    /**
     * latitude : 100
     * longitude : 120
     */

    private double latitude;
    private double longitude;

    public Location() {
    }

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

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
}
