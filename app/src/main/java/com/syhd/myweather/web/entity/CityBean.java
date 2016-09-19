package com.syhd.myweather.web.entity;

import java.util.List;

/**
 * @author 韩宝坤
 * @date 2016/8/26 16:17
 * @email kunyu1342@163.com
 * @description
 */
public class CityBean {
    private String city;
    private List<DistrictBean> district;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<DistrictBean> getDistrict() {
        return district;
    }

    public void setDistrict(List<DistrictBean> district) {
        this.district = district;
    }
}
