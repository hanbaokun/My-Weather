package com.syhd.myweather.bean;

import java.io.Serializable;

/**
 * 收货地址的bean
 * Created by ZhangFuen on 2015/12/11.
 */
public class Address implements Serializable {

    //收货人
    private String city;
    //手机
    private String province;

    private int isdefault;

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    public Address(String city, String province) {
        this.city = city;
        this.province = province;
    }

    public Address(String city, String province, int isdefault) {
        this.city = city;
        this.province = province;
        this.isdefault = isdefault;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
