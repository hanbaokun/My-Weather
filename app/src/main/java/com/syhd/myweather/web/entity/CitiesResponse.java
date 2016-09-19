package com.syhd.myweather.web.entity;

import java.util.List;

/**
 * @author 韩宝坤
 * @date 2016/8/25 16:21
 * @email kunyu1342@163.com
 * @description
 */
public class CitiesResponse {
    private String province;
    /**
     * city : 合肥
     * district : [{"district":"合肥"},{"district":"长丰"},{"district":"肥东"},{"district":"肥西"},{"district":"巢湖"},{"district":"庐江"}]
     */

    private List<CityBean> city;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<CityBean> getCity() {
        return city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }
}
