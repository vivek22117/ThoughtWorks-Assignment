package com.ddsolutions.iot.equipment.data.domain;

public class Address {

    private String country;
    private String state;
    private String streatName;
    private String streatNumber;
    private Integer pincode;

    public Address(String country, String state, String streatName, String streatNumber, Integer pincode) {
        this.country = country;
        this.state = state;
        this.streatName = streatName;
        this.streatNumber = streatNumber;
        this.pincode = pincode;
    }

    public Address() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreatName() {
        return streatName;
    }

    public void setStreatName(String streatName) {
        this.streatName = streatName;
    }

    public String getStreatNumber() {
        return streatNumber;
    }

    public void setStreatNumber(String streatNumber) {
        this.streatNumber = streatNumber;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", streatName='" + streatName + '\'' +
                ", streatNumber='" + streatNumber + '\'' +
                ", pincode=" + pincode +
                '}';
    }
}
