package com.topcoder.innovate.model;

/**
 * Created by Jie Yao on 2017/9/6.
 */

public class Position {
    private String city;
    private String name;
    private String zip;
    private Double longitude;
    private  String state;
    private String address;
    private Double latitude;
    private Double blingid;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getBlingid() {
        return blingid;
    }

    public void setBlingid(Double bling_id) {
        this.blingid = bling_id;
    }
}
