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
package com.syhd.myweather.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syhd.myweather.R;
import com.syhd.myweather.bean.Address;
import com.syhd.myweather.bean.CityManage;
import com.syhd.myweather.db.dao.AddressDao;
import com.syhd.myweather.utils.MyUtil;
import com.syhd.myweather.utils.SharePreUtil;
import com.syhd.myweather.web.ApiService;
import com.syhd.myweather.web.ErrorCode;
import com.syhd.myweather.web.entity.GetWeatherInfoResponse;
import com.syhd.myweather.web.entity.WeatherInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 城市管理适配器类
 *
 * @author 咖枯
 * @version 1.0 2015/10/22
 */
public class CityManageAdapter extends MyBaseAdapter<Address> {

    /**
     * 城市管理适配器构造方法
     *
     * @param context context
     * @param list    城市管理列表
     */
    public CityManageAdapter(Context context, List<Address> list) {
        super(context, list);
    }

    public interface DelListener{
        void deleteCity();
    }

    private DelListener delListener;

    public DelListener getDelListener() {
        return delListener;
    }

    public void setDelListener(DelListener delListener) {
        this.delListener = delListener;
    }

    /**
     * 删除城市按钮状态
     */
    private boolean mIsVisible;

    /**
     * 更新删除城市按钮状态
     *
     * @param isVisible 删除按钮是否可见
     */
    public void setCityDeleteButton(boolean isVisible) {
        mIsVisible = isVisible;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder localViewHolder;
        final Address address = list.get(position);
        final AddressDao addressDao = new AddressDao(context);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.gv_city_manage, null);
            localViewHolder = new ViewHolder(convertView);
            convertView.setTag(localViewHolder);
        } else {
            localViewHolder = (ViewHolder) convertView.getTag();
        }
        // 当为最后一项（添加城市按钮）
        if (position == list.size() - 1) {
            localViewHolder.addCity.setVisibility(View.VISIBLE);
            localViewHolder.weatherTypeIv.setVisibility(View.INVISIBLE);
            localViewHolder.cityWeather.setVisibility(View.INVISIBLE);
            localViewHolder.cityDeleteBtn.setVisibility(View.GONE);
        } else {
            if (position == 0 || "1".equals(addressDao.find(address.getCity()))) {
                localViewHolder.cityDeleteBtn.setVisibility(View.GONE);
                updateWeatherInfo(address.getCity(),
                        address.getProvince(),
                        address.getIsdefault(),
                        localViewHolder,
                        position);
            } else {
                if (mIsVisible) {//删除了城市
                    localViewHolder.cityDeleteBtn.setVisibility(View.VISIBLE);
                    localViewHolder.cityDeleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addressDao.delete(address.getCity());
                            list.remove(address);
                            notifyDataSetChanged();
                            if(delListener!=null){
                                delListener.deleteCity();
                            }
                        }
                    });
                } else {
                    localViewHolder.cityDeleteBtn.setVisibility(View.INVISIBLE);
                }
                updateWeatherInfo(address.getCity(),
                        address.getProvince(),
                        address.getIsdefault(),
                        localViewHolder,
                        position);
            }
            localViewHolder.setDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String city = SharePreUtil.getStringData("city","");
                    int id = SharePreUtil.getIntData("id", 0);
                    if (!address.getCity().equals(city) ){
                        address.setIsdefault(1);
                        SharePreUtil.saveStringData("city",address.getCity());
                        SharePreUtil.saveIntData("id",position);
                        Address address1 = list.get(id);
                        address1.setIsdefault(0);
                        notifyDataSetChanged();
                        addressDao.update(new Address(address.getCity(),address.getProvince(),1));
                        addressDao.update(new Address(address1.getCity(),address1.getProvince(),0));
                    }
                }
            });

            if (address.getIsdefault() == 1) {
                localViewHolder.setDefault.setText("默认");
                localViewHolder.setDefault.setBackgroundDrawable(context.getResources().getDrawable(
                        R.drawable.bg_gv_city_manage_default));
                SharePreUtil.saveIntData("id",position);
                SharePreUtil.saveStringData("city",address.getCity());
            } else {
                localViewHolder.setDefault.setBackgroundDrawable(context.getResources().getDrawable(
                        R.drawable.bg_gv_city_manage_set_default));
                localViewHolder.setDefault.setText("设为默认");
            }
            if (position == 0) {
                setImage(localViewHolder.cityName, R.drawable.ic_gps1);
            }
            localViewHolder.cityName.setText(address.getCity());
            localViewHolder.addCity.setVisibility(View.INVISIBLE);
            localViewHolder.weatherTypeIv.setVisibility(View.VISIBLE);
            localViewHolder.cityWeather.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public void updateWeatherInfo(String city, String province, final int isdefault,
                                  final ViewHolder localViewHolder, final int position) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apicloud.mob.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<GetWeatherInfoResponse> call = apiService.getWeatherInfo(city, province);
        call.enqueue(new Callback<GetWeatherInfoResponse>() {

            @Override
            public void onResponse(Call<GetWeatherInfoResponse> call, Response<GetWeatherInfoResponse> response) {
                GetWeatherInfoResponse ipInfoResponse = response.body();
                switch (ipInfoResponse.retCode) {
                    case ErrorCode.success:
//                        Log.i("CityManageAdapter", "onResponse: " + ipInfoResponse.result.get(0).toString());
                        WeatherInfo weatherInfo = ipInfoResponse.result.get(0);
                        CityManage cityManage = new CityManage();
                        cityManage.setCityName(weatherInfo.getDistrct());
                        if (weatherInfo.getFuture().get(0).getTemperature().contains("/")) {
                            cityManage.setTempHigh(weatherInfo.getFuture().get(0).getTemperature().split("/")[0].trim());
                            cityManage.setTempLow(weatherInfo.getFuture().get(0).getTemperature().split("/")[1].trim());
                        } else {
                            cityManage.setTempHigh(weatherInfo.getTemperature());
                            cityManage.setTempLow(weatherInfo.getFuture().get(0).getTemperature());
                        }
                        cityManage.setWeatherType(weatherInfo.getWeather());
                        cityManage.setIsdefault(isdefault);
                        localViewHolder.tempHigh.setText(cityManage.getTempHigh());
                        localViewHolder.tempLow.setText(cityManage.getTempLow());
                        localViewHolder.weatherTypeTv.setText(cityManage.getWeatherType());

                        int weatherTypeImageID = MyUtil.getWeatherTypeImageID(cityManage.getWeatherType(), true);
                        localViewHolder.weatherTypeIv.setImageResource(weatherTypeImageID);

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<GetWeatherInfoResponse> call, Throwable t) {
//                Log.e(TAG, "onFailure: " + t.toString());
//                msg = "网络错误";
//                handler.sendEmptyMessage(2);
            }
        });
    }

    /**
     * 设置左侧图片
     *
     * @param tv      textView
     * @param imageId 图片id
     */
    private void setImage(TextView tv, int imageId) {
        @SuppressWarnings("deprecation") Drawable drawable = context.getResources().getDrawable(imageId);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            // 设置图片
            tv.setCompoundDrawables(drawable, null, null, null);
        }
    }

    static class ViewHolder {
        @BindView(R.id.city_name)
        TextView cityName;
        @BindView(R.id.weather_type_iv)
        ImageView weatherTypeIv;
        @BindView(R.id.temp_high)
        TextView tempHigh;
        @BindView(R.id.temp_low)
        TextView tempLow;
        @BindView(R.id.weather_type_tv)
        TextView weatherTypeTv;
        @BindView(R.id.set_default)
        TextView setDefault;
        @BindView(R.id.city_weather)
        LinearLayout cityWeather;
        @BindView(R.id.add_city)
        ImageView addCity;
        @BindView(R.id.city_delete_btn)
        ImageView cityDeleteBtn;
        @BindView(R.id.background)
        FrameLayout background;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
