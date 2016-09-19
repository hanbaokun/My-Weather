/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.syhd.myweather.bean;

/**
 * 城市管理
 *
 * @author 咖枯
 * @version 1.0 2015/10/22
 */
public class CityManage {
    public CityManage() {
    }

    public CityManage(String cityName, String tempHigh, String tempLow, String weatherType,int isdefault) {
        this.cityName = cityName;
        this.tempHigh = tempHigh;
        this.tempLow = tempLow;
        this.weatherType = weatherType;
        this.isdefault = isdefault;
    }

    /**
     * 城市名
     */
    private String cityName;
    private int isdefault;

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    /**
     * 高温
     */
    private String tempHigh;

    public String getTempLow() {
        return tempLow;
    }

    public void setTempLow(String tempLow) {
        this.tempLow = tempLow;
    }

    /**
     * 低温
     */
    private String tempLow;

    /**
     * 天气类型
     */
    private String weatherType;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTempHigh() {
        return tempHigh;
    }

    public void setTempHigh(String tempHigh) {
        this.tempHigh = tempHigh;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

}
