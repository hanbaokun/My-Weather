package com.syhd.myweather.fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.ScrollViewListener;
import com.syhd.myweather.R;
import com.syhd.myweather.customview.LineChartViewDouble;
import com.syhd.myweather.utils.MyUtil;
import com.syhd.myweather.utils.SharePreUtil;
import com.syhd.myweather.web.ApiService;
import com.syhd.myweather.web.ErrorCode;
import com.syhd.myweather.web.entity.GetWeatherInfoResponse;
import com.syhd.myweather.web.entity.WeatherInfo;
import com.syhd.myweather.web.entity.WeatherInfo.FutureBean;

import java.util.Calendar;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author 韩宝坤
 * @date 2016/8/22 15:58
 * @email kunyu1342@163.com
 * @description
 */
public class WeaFragment extends BaseFragment {
    private static final String TAG = WeaFragment.class.getName();
    @BindView(R.id.weather_area)
    TextView weatherArea;
    @BindView(R.id.topactionbar)
    RelativeLayout topactionbar;
    @BindView(R.id.background)
    RelativeLayout background;
    @BindView(R.id.temperature1)
    ImageView temperature1;
    @BindView(R.id.temperature2)
    ImageView temperature2;
    @BindView(R.id.temperature3)
    ImageView temperature3;
    @BindView(R.id.weather_type)
    TextView weatherType;
    @BindView(R.id.aqi)
    TextView aqi;
    @BindView(R.id.humidity)
    TextView humidity;
    @BindView(R.id.wind)
    TextView wind;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.update_time)
    TextView updateTime;
    @BindView(R.id.weather_type_iv_today)
    ImageView weatherTypeIvToday;
    @BindView(R.id.temp_high_today)
    TextView tempHighToday;
    @BindView(R.id.temp_low_today)
    TextView tempLowToday;
    @BindView(R.id.today_wea)
    TextView todayWea;
    @BindView(R.id.weather_type_iv_tomorrow)
    ImageView weatherTypeIvTomorrow;
    @BindView(R.id.temp_high_tomorrow)
    TextView tempHighTomorrow;
    @BindView(R.id.temp_low_tomorrow)
    TextView tempLowTomorrow;
    @BindView(R.id.tomorrow_wea)
    TextView tomorrowWea;
    @BindView(R.id.weather_type_iv_day_after_tomorrow)
    ImageView weatherTypeIvDayAfterTomorrow;
    @BindView(R.id.temp_high_day_after_tomorrow)
    TextView tempHighDayAfterTomorrow;
    @BindView(R.id.temp_low_day_after_tomorrow)
    TextView tempLowDayAfterTomorrow;
    @BindView(R.id.the_day_of_tomorrow_wea)
    TextView theDayOfTomorrowWea;
    @BindView(R.id.weather_layout)
    LinearLayout weatherLayout;
    @BindView(R.id.wea_days_forecast_week1)
    TextView weaDaysForecastWeek1;
    @BindView(R.id.wea_days_forecast_week2)
    TextView weaDaysForecastWeek2;
    @BindView(R.id.wea_days_forecast_week3)
    TextView weaDaysForecastWeek3;
    @BindView(R.id.wea_days_forecast_week4)
    TextView weaDaysForecastWeek4;
    @BindView(R.id.wea_days_forecast_week5)
    TextView weaDaysForecastWeek5;
    @BindView(R.id.wea_days_forecast_week6)
    TextView weaDaysForecastWeek6;
    @BindView(R.id.wea_days_forecast_day1)
    TextView weaDaysForecastDay1;
    @BindView(R.id.wea_days_forecast_day2)
    TextView weaDaysForecastDay2;
    @BindView(R.id.wea_days_forecast_day3)
    TextView weaDaysForecastDay3;
    @BindView(R.id.wea_days_forecast_day4)
    TextView weaDaysForecastDay4;
    @BindView(R.id.wea_days_forecast_day5)
    TextView weaDaysForecastDay5;
    @BindView(R.id.wea_days_forecast_day6)
    TextView weaDaysForecastDay6;
    @BindView(R.id.wea_days_forecast_weather_day_iv1)
    ImageView weaDaysForecastWeatherDayIv1;
    @BindView(R.id.wea_days_forecast_weather_day_iv2)
    ImageView weaDaysForecastWeatherDayIv2;
    @BindView(R.id.wea_days_forecast_weather_day_iv3)
    ImageView weaDaysForecastWeatherDayIv3;
    @BindView(R.id.wea_days_forecast_weather_day_iv4)
    ImageView weaDaysForecastWeatherDayIv4;
    @BindView(R.id.wea_days_forecast_weather_day_iv5)
    ImageView weaDaysForecastWeatherDayIv5;
    @BindView(R.id.wea_days_forecast_weather_day_iv6)
    ImageView weaDaysForecastWeatherDayIv6;
    @BindView(R.id.wea_days_forecast_weather_day_tv1)
    TextView weaDaysForecastWeatherDayTv1;
    @BindView(R.id.wea_days_forecast_weather_day_tv2)
    TextView weaDaysForecastWeatherDayTv2;
    @BindView(R.id.wea_days_forecast_weather_day_tv3)
    TextView weaDaysForecastWeatherDayTv3;
    @BindView(R.id.wea_days_forecast_weather_day_tv4)
    TextView weaDaysForecastWeatherDayTv4;
    @BindView(R.id.wea_days_forecast_weather_day_tv5)
    TextView weaDaysForecastWeatherDayTv5;
    @BindView(R.id.wea_days_forecast_weather_day_tv6)
    TextView weaDaysForecastWeatherDayTv6;
    @BindView(R.id.line_char)
    LineChartViewDouble lineChar;
    @BindView(R.id.wea_days_forecast_weather_night_tv1)
    TextView weaDaysForecastWeatherNightTv1;
    @BindView(R.id.wea_days_forecast_weather_night_tv2)
    TextView weaDaysForecastWeatherNightTv2;
    @BindView(R.id.wea_days_forecast_weather_night_tv3)
    TextView weaDaysForecastWeatherNightTv3;
    @BindView(R.id.wea_days_forecast_weather_night_tv4)
    TextView weaDaysForecastWeatherNightTv4;
    @BindView(R.id.wea_days_forecast_weather_night_tv5)
    TextView weaDaysForecastWeatherNightTv5;
    @BindView(R.id.wea_days_forecast_weather_night_tv6)
    TextView weaDaysForecastWeatherNightTv6;
    @BindView(R.id.wea_days_forecast_weather_night_iv1)
    ImageView weaDaysForecastWeatherNightIv1;
    @BindView(R.id.wea_days_forecast_weather_night_iv2)
    ImageView weaDaysForecastWeatherNightIv2;
    @BindView(R.id.wea_days_forecast_weather_night_iv3)
    ImageView weaDaysForecastWeatherNightIv3;
    @BindView(R.id.wea_days_forecast_weather_night_iv4)
    ImageView weaDaysForecastWeatherNightIv4;
    @BindView(R.id.wea_days_forecast_weather_night_iv5)
    ImageView weaDaysForecastWeatherNightIv5;
    @BindView(R.id.wea_days_forecast_weather_night_iv6)
    ImageView weaDaysForecastWeatherNightIv6;
    @BindView(R.id.wea_days_forecast_wind_direction_tv1)
    TextView weaDaysForecastWindDirectionTv1;
    @BindView(R.id.wea_days_forecast_wind_direction_tv2)
    TextView weaDaysForecastWindDirectionTv2;
    @BindView(R.id.wea_days_forecast_wind_direction_tv3)
    TextView weaDaysForecastWindDirectionTv3;
    @BindView(R.id.wea_days_forecast_wind_direction_tv4)
    TextView weaDaysForecastWindDirectionTv4;
    @BindView(R.id.wea_days_forecast_wind_direction_tv5)
    TextView weaDaysForecastWindDirectionTv5;
    @BindView(R.id.wea_days_forecast_wind_direction_tv6)
    TextView weaDaysForecastWindDirectionTv6;
    @BindView(R.id.wea_life_tv_index_dress)
    TextView weaLifeTvIndexDress;
    @BindView(R.id.wea_life_index_rlyt_dress)
    RelativeLayout weaLifeIndexRlytDress;
    @BindView(R.id.wea_life_index_tv_cold)
    TextView weaLifeIndexTvCold;
    @BindView(R.id.wea_life_index_rlyt_cold)
    RelativeLayout weaLifeIndexRlytCold;
    @BindView(R.id.wea_life_index_tv_morning_exercise)
    TextView weaLifeIndexTvMorningExercise;
    @BindView(R.id.wea_life_index_rlyt_morning_exercise)
    RelativeLayout weaLifeIndexRlytMorningExercise;
    @BindView(R.id.wea_life_index_tv_car_wash)
    TextView weaLifeIndexTvCarWash;
    @BindView(R.id.wea_life_index_rlyt_carwash)
    RelativeLayout weaLifeIndexRlytCarwash;
    private String city;
    private String province;
    private WeatherInfo weatherInfo;
    private PullToRefreshScrollView pullRefreshScrollview;
    /**
     * 模糊处理过的Drawable
     */
    private Drawable mBlurDrawable;
    /**
     * 屏幕密度
     */
    private float mDensity;
    /**
     * 透明
     */
    private int mAlpha = 0;
    /**
     * 延迟刷新Handler
     */
    public Handler mHandler;
    /**
     * 延迟刷新Runnable
     */
    public Runnable mRun;
    /**
     * 延迟刷新线程是否已经启动
     */
    public boolean mIsPostDelayed;
    /**
     * 上次主动更新时间
     */
    private long mLastActiveUpdateTime;

    private String msg = "";
    /**
     * 滑动监听器
     */
    private ScrollViewListener scrollViewListener;

    private int i = 1;


    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    //TODO 填充页面
                    setBackground(weatherInfo);
                    setActionBarBg(weatherInfo);
                    // 设置温度
                    setTemperature(weatherInfo);
                    //设置天气类型
                    setWeatherType(weatherInfo);
                    // 设置aqi
                    setAQI(weatherInfo);
                    // 设置风向、风力
                    setWind(weatherInfo);
                    // 现在小时
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    //20160823211108
                    String time = weatherInfo.getUpdateTime();
                    int hour1 = Integer.parseInt(time.substring(8, 10));
                    int minute1 = Integer.parseInt(time.substring(10, 12));
                    FutureBean weather1 = weatherInfo.getFuture().get(0);
                    FutureBean weather2 = weatherInfo.getFuture().get(1);
                    FutureBean weather3 = weatherInfo.getFuture().get(2);
                    FutureBean weather4 = weatherInfo.getFuture().get(3);
                    FutureBean weather5 = weatherInfo.getFuture().get(4);
                    FutureBean weather6 = weatherInfo.getFuture().get(5);
//                    FutureBean weather7 = weatherInfo.getFuture().get(6);

                    // 设置今天，明天，后天大概天气
                    setThreeDaysWeather(weather1, weather2, weather3, hour);
                    // 设置多天天气预报
                    setDaysForecast(weather1, weather2, weather3, weather4, weather5, weather6,
                            hour1, minute1, calendar);

                    // 生活指数信息
                    setLifeIndex(weatherInfo);
                    stopRefresh();
                    break;
                case 2:
                    stopRefresh();
                    if (i == 1) {
                        if ("查询不到".equals(msg)) {
                            if(SharePreUtil.getIntData("isfirst",0)!=1) {
                                Toast.makeText(getContext(), msg + ",正在尝试查询上级区域", Toast.LENGTH_SHORT).show();
                            }
                            SharePreUtil.saveIntData("isfirst",1);
                            updateWeatherInfo(province, province);
                            i += 1;
                        } else {
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if ("查询不到".equals(msg)) {
                            i = 1;
                            if(SharePreUtil.getIntData("issec",0)!=1) {
                                Toast.makeText(getContext(), msg + ",目前该城市还未支持", Toast.LENGTH_SHORT).show();
                            }
                            SharePreUtil.saveIntData("issec",1);
                        } else {
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
            return false;
        }
    });

    /**
     * 设置生活指数
     *
     * @param weatherInfo
     */

    private void setLifeIndex(WeatherInfo weatherInfo) {
        weaLifeTvIndexDress.setText(weatherInfo.getDressingIndex());
        weaLifeIndexTvCold.setText(weatherInfo.getColdIndex());
        weaLifeIndexTvMorningExercise.setText(weatherInfo.getExerciseIndex());
        weaLifeIndexTvCarWash.setText(weatherInfo.getWashIndex());
    }

    /**
     * 设置多天天气预报
     */
    private void setDaysForecast(FutureBean weather1,
                                 FutureBean weather2, FutureBean weather3,
                                 FutureBean weather4, FutureBean weather5,
                                 FutureBean weather6, int hour1, int minute1,
                                 Calendar calendar) {

        // 设置标题星期
        weaDaysForecastWeek1.setText(weather1.getWeek());
        weaDaysForecastWeek2.setText(weather2.getWeek());
        weaDaysForecastWeek3.setText(weather3.getWeek());
        weaDaysForecastWeek4.setText(weather4.getWeek());
        weaDaysForecastWeek5.setText(weather5.getWeek());
        weaDaysForecastWeek6.setText(weather6.getWeek());

        //2016-08-23
        weaDaysForecastDay1.setText(weather1.getDate().split("-")[1] + "/" + weather1.getDate().split("-")[2]);
        weaDaysForecastDay2.setText(weather2.getDate().split("-")[1] + "/" + weather1.getDate().split("-")[2]);
        weaDaysForecastDay3.setText(weather3.getDate().split("-")[1] + "/" + weather1.getDate().split("-")[2]);
        weaDaysForecastDay4.setText(weather4.getDate().split("-")[1] + "/" + weather1.getDate().split("-")[2]);
        weaDaysForecastDay5.setText(weather5.getDate().split("-")[1] + "/" + weather1.getDate().split("-")[2]);
        weaDaysForecastDay6.setText(weather6.getDate().split("-")[1] + "/" + weather1.getDate().split("-")[2]);

        // 取得白天天气类型图片id
        int weatherDayId1;
        if (weather1.getDayTime() != null) {
            weatherDayId1 = MyUtil.getWeatherTypeImageID(weather1.getDayTime(), true);
        } else {
            weatherDayId1 = MyUtil.getWeatherTypeImageID(weather1.getNight(), true);
        }

        int weatherDayId2 = MyUtil.getWeatherTypeImageID(weather2.getDayTime(), true);
        int weatherDayId3 = MyUtil.getWeatherTypeImageID(weather3.getDayTime(), true);
        int weatherDayId4 = MyUtil.getWeatherTypeImageID(weather4.getDayTime(), true);
        int weatherDayId5 = MyUtil.getWeatherTypeImageID(weather5.getDayTime(), true);
        int weatherDayId6 = MyUtil.getWeatherTypeImageID(weather6.getDayTime(), true);

        //设置白天天气类型图片
        weaDaysForecastWeatherDayIv1.setImageResource(weatherDayId1);
        weaDaysForecastWeatherDayIv2.setImageResource(weatherDayId2);
        weaDaysForecastWeatherDayIv3.setImageResource(weatherDayId3);
        weaDaysForecastWeatherDayIv4.setImageResource(weatherDayId4);
        weaDaysForecastWeatherDayIv5.setImageResource(weatherDayId5);
        weaDaysForecastWeatherDayIv6.setImageResource(weatherDayId6);

        // 设置白天天气类型文字
        if (weather1.getDayTime() != null) {
            weaDaysForecastWeatherDayTv1.setText(weather1.getDayTime());
        } else {
            weaDaysForecastWeatherDayTv1.setText(R.string.dash);
        }

        weaDaysForecastWeatherDayTv2.setText(weather2.getDayTime());
        weaDaysForecastWeatherDayTv3.setText(weather3.getDayTime());
        weaDaysForecastWeatherDayTv4.setText(weather4.getDayTime());
        weaDaysForecastWeatherDayTv5.setText(weather5.getDayTime());
        weaDaysForecastWeatherDayTv6.setText(weather6.getDayTime());

        if (weather1.getTemperature().contains("/")) {
            // 设置白天温度曲线
            lineChar.setTempDay(new int[]{Integer.parseInt(weather1.getTemperature().split("/")[0].trim().replace("°C", "")),
                    Integer.parseInt(weather2.getTemperature().split("/")[0].trim().replace("°C", "")),
                    Integer.parseInt(weather3.getTemperature().split("/")[0].trim().replace("°C", "")),
                    Integer.parseInt(weather4.getTemperature().split("/")[0].trim().replace("°C", "")),
                    Integer.parseInt(weather5.getTemperature().split("/")[0].trim().replace("°C", "")),
                    Integer.parseInt(weather6.getTemperature().split("/")[0].trim().replace("°C", ""))
            });
            // 设置夜间温度曲线
            lineChar.setTempNight(new int[]{Integer.parseInt(weather1.getTemperature().split("/")[1].trim().replace("°C", "")),
                    Integer.parseInt(weather2.getTemperature().split("/")[1].trim().replace("°C", "")),
                    Integer.parseInt(weather3.getTemperature().split("/")[1].trim().replace("°C", "")),
                    Integer.parseInt(weather4.getTemperature().split("/")[1].trim().replace("°C", "")),
                    Integer.parseInt(weather5.getTemperature().split("/")[1].trim().replace("°C", "")),
                    Integer.parseInt(weather6.getTemperature().split("/")[1].trim().replace("°C", ""))});
        } else {
            // 设置白天温度曲线
            lineChar.setTempDay(new int[]{Integer.parseInt(weather1.getTemperature().replace("°C", "")),
                    Integer.parseInt(weather2.getTemperature().split("/")[0].trim().replace("°C", "")),
                    Integer.parseInt(weather3.getTemperature().split("/")[0].trim().replace("°C", "")),
                    Integer.parseInt(weather4.getTemperature().split("/")[0].trim().replace("°C", "")),
                    Integer.parseInt(weather5.getTemperature().split("/")[0].trim().replace("°C", "")),
                    Integer.parseInt(weather6.getTemperature().split("/")[0].trim().replace("°C", ""))
            });
            // 设置夜间温度曲线
            lineChar.setTempNight(new int[]{Integer.parseInt(weather1.getTemperature().replace("°C", "")),
                    Integer.parseInt(weather2.getTemperature().split("/")[1].trim().replace("°C", "")),
                    Integer.parseInt(weather3.getTemperature().split("/")[1].trim().replace("°C", "")),
                    Integer.parseInt(weather4.getTemperature().split("/")[1].trim().replace("°C", "")),
                    Integer.parseInt(weather5.getTemperature().split("/")[1].trim().replace("°C", "")),
                    Integer.parseInt(weather6.getTemperature().split("/")[1].trim().replace("°C", ""))});
        }
        lineChar.invalidate();

        // 设置夜间天气类型文字
        if (weather1.getNight() != null) {
            weaDaysForecastWeatherNightTv1.setText(weather1.getNight());
        } else {
            weaDaysForecastWeatherNightTv1.setText(R.string.dash);
        }
        weaDaysForecastWeatherNightTv2.setText(weather2.getNight());
        weaDaysForecastWeatherNightTv3.setText(weather3.getNight());
        weaDaysForecastWeatherNightTv4.setText(weather4.getNight());
        weaDaysForecastWeatherNightTv5.setText(weather5.getNight());
        weaDaysForecastWeatherNightTv6.setText(weather6.getNight());

        // 取得夜间天气类型图片id
        int weatherNightId1;
        if (weather1.getNight() != null) {
            weatherNightId1 = MyUtil.getWeatherTypeImageID(weather1.getNight(), false);
        } else {
            weatherNightId1 = R.drawable.ic_weather_no;
        }
        int weatherNightId2 = MyUtil.getWeatherTypeImageID(weather2.getNight(), false);
        int weatherNightId3 = MyUtil.getWeatherTypeImageID(weather3.getNight(), false);
        int weatherNightId4 = MyUtil.getWeatherTypeImageID(weather4.getNight(), false);
        int weatherNightId5 = MyUtil.getWeatherTypeImageID(weather5.getNight(), false);
        int weatherNightId6 = MyUtil.getWeatherTypeImageID(weather6.getNight(), false);

        //设置夜间天气类型图片
        weaDaysForecastWeatherNightIv1.setImageResource(weatherNightId1);
        weaDaysForecastWeatherNightIv2.setImageResource(weatherNightId2);
        weaDaysForecastWeatherNightIv3.setImageResource(weatherNightId3);
        weaDaysForecastWeatherNightIv4.setImageResource(weatherNightId4);
        weaDaysForecastWeatherNightIv5.setImageResource(weatherNightId5);
        weaDaysForecastWeatherNightIv6.setImageResource(weatherNightId6);

        // 设置风向
        if (weather1.getWind() != null) {
            weaDaysForecastWindDirectionTv1.setText(weather1.getWind());
        } else {
            weaDaysForecastWindDirectionTv1.setText(R.string.dash);
        }
        weaDaysForecastWindDirectionTv2.setText(weather2.getWind());
        weaDaysForecastWindDirectionTv3.setText(weather3.getWind());
        weaDaysForecastWindDirectionTv4.setText(weather4.getWind());
        weaDaysForecastWindDirectionTv5.setText(weather5.getWind());
        weaDaysForecastWindDirectionTv6.setText(weather6.getWind());
    }

    private void setActionBarBg(WeatherInfo weatherInfo) {
        // 天气类型图片id
        int weatherId = MyUtil.getWeatherTypeActionBarID(weatherInfo.getWeather());
        setTopActionabarBg(weatherId);
    }


    /**
     * 设置背景
     *
     * @param weatherInfo
     */
    private void setBackground(final WeatherInfo weatherInfo) {
        Handler mHandler = new Handler();
        Runnable mRun = new Runnable() {
            @Override
            public void run() {
                try {
                    if (!getActivity().isFinishing()) {
                        // 天气类型图片id
                        int weatherId = MyUtil.getWeatherTypeBackgroundID(weatherInfo.getWeather());
                        background.setBackgroundResource(weatherId);
                    }
                } catch (Exception e) {
                }
            }
        };
        mHandler.postDelayed(mRun, 1000);
    }

    /**
     * 设置今天，明天，后天大概天气
     *
     * @param weather2 今天
     * @param weather3 明天
     * @param weather4 后天
     * @param hour     当前小时
     */
    private void setThreeDaysWeather(FutureBean weather2, FutureBean weather3,
                                     FutureBean weather4, int hour) {
        // 天气类型图片id
        int weatherId;

        // 设置今天天气信息
        // 当前为凌晨
        Log.i(TAG, "setThreeDaysWeather: " + hour + "==============");
        if (hour >= 0 && hour < 6) {
            weatherId = MyUtil.getWeatherTypeImageID(weather2.getDayTime(), false);
            todayWea.setText(weather2.getDayTime());
            // 当前为白天时
        } else if (hour >= 6 && hour < 18) {
            weatherId = MyUtil.getWeatherTypeImageID(weather2.getDayTime(), true);
            todayWea.setText(weather2.getDayTime());
            // 当前为夜间
        } else {
            weatherId = MyUtil.getWeatherTypeImageID(weather2.getNight(), false);
            todayWea.setText(weather2.getNight());
        }
        weatherTypeIvToday.setImageResource(weatherId);
        Log.i(TAG, "setThreeDaysWeather: " + weather2.getTemperature());
        if (weather2.getTemperature().contains("/")) {
            tempHighToday.setText(weather2.getTemperature().split("/")[0].trim());
            tempLowToday.setText(weather2.getTemperature().split("/")[1].trim());
        } else {
            tempHighToday.setText(weather2.getTemperature());
            tempLowToday.setText(weather2.getTemperature());
        }

        // 设置明天天气信息
        weatherId = MyUtil.getWeatherTypeImageID(weather3.getDayTime(), true);
        weatherTypeIvTomorrow.setImageResource(weatherId);
        if (weather3.getTemperature().contains("/")) {
            tempHighTomorrow.setText(weather3.getTemperature().split("/")[0].trim());
            tempLowTomorrow.setText(weather3.getTemperature().split("/")[1].trim());
        } else {
            tempHighTomorrow.setText(weather3.getTemperature());
            tempLowTomorrow.setText(weather3.getTemperature());
        }
        tomorrowWea.setText(MyUtil.getWeatherType
                (getActivity(), weather3.getDayTime(), weather3.getNight()));

        // 设置后天天气信息
        weatherId = MyUtil.getWeatherTypeImageID(weather4.getDayTime(), true);
        weatherTypeIvDayAfterTomorrow.setImageResource(weatherId);
        if (weather4.getTemperature().contains("/")) {
            tempHighDayAfterTomorrow.setText(weather4.getTemperature().split("/")[0].trim());
            tempLowDayAfterTomorrow.setText(weather4.getTemperature().split("/")[1].trim());
        } else {
            tempHighDayAfterTomorrow.setText(weather4.getTemperature());
            tempLowDayAfterTomorrow.setText(weather4.getTemperature());
        }
        theDayOfTomorrowWea.setText(MyUtil.getWeatherType
                (getActivity(), weather4.getDayTime(), weather4.getNight()));
    }

    /**
     * 设置天气类型
     *
     * @param weatherInfo
     */
    private void setWeatherType(WeatherInfo weatherInfo) {
        weatherType.setText(weatherInfo.getWeather());
        humidity.setText(weatherInfo.getHumidity());
        String updateTime = weatherInfo.getUpdateTime();
        date.setText("日期：" + updateTime.substring(0, 4) + "/" + updateTime.substring(4, 6) + "/" + updateTime.substring(6, 8));
        this.updateTime.setText("发布：" + updateTime.substring(8, 10) + ":" + updateTime.substring(10, 12));
    }

    /**
     * 设置风向、风力
     */
    private void setWind(WeatherInfo weatherInfo) {
        if (weatherInfo.getWind() != null) {
            // 设置风向图片
            setImage(wind, getWindImageId(weatherInfo.getWind()));
            // 设置风向、风力
            wind.setText(weatherInfo.getWind());
        } else {
            setImage(wind, R.drawable.ic_wind_3);
            wind.setText(R.string.no);
        }
    }

    /**
     * 取得风向图片id
     *
     * @param windDirection 风向
     * @return 风向图片id
     */
    private int getWindImageId(String windDirection) {
        String substring = windDirection.substring(0, windDirection.length() - 2);
        Log.i(TAG, "getWindImageId: " + substring);
        int imgId;
        switch (substring) {
            case "南风":
                imgId = R.drawable.ic_wind_1;
                break;
            case "西南风":
                imgId = R.drawable.ic_wind_2;
                break;
            case "西风":
                imgId = R.drawable.ic_wind_3;
                break;
            case "西北风":
                imgId = R.drawable.ic_wind_4;
                break;
            case "北风":
                imgId = R.drawable.ic_wind_5;
                break;
            case "东北风":
                imgId = R.drawable.ic_wind_6;
                break;
            case "东风":
                imgId = R.drawable.ic_wind_7;
                break;
            case "东南风":
                imgId = R.drawable.ic_wind_8;
                break;
            default:
                imgId = R.drawable.ic_wind_3;
                break;
        }
        return imgId;
    }

    /**
     * 设置aqi
     */
    private void setAQI(WeatherInfo weatherInfo) {
        if (weatherInfo.getAirCondition() != null) {
            aqi.setVisibility(View.VISIBLE);
            // 设置空气质量图片
            setImage(aqi, getQualityImageId(weatherInfo.getAirCondition()));
            // 设置空气质量
            aqi.setText("空气质量：" + weatherInfo.getAirCondition());
        } else {
            aqi.setVisibility(View.GONE);
        }
    }

    /**
     * 设置左侧图片
     *
     * @param tv      textView
     * @param imageId 图片id
     */
    private void setImage(TextView tv, int imageId) {
        @SuppressWarnings("deprecation") Drawable drawable = getResources().getDrawable(imageId);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            // 设置图片
            tv.setCompoundDrawables(drawable, null, null, null);
        }
    }

    /**
     * 取得aqi图片id
     *
     * @param quality 大气质量
     * @return aqi图片id
     */
    private int getQualityImageId(String quality) {
        int imgId;
        switch (quality) {
            case "优":
                imgId = R.drawable.ic_quality_nice;
                break;
            case "良":
                imgId = R.drawable.ic_quality_good;
                break;
            case "轻度污染":
                imgId = R.drawable.ic_quality_little;
                break;
            case "中度污染":
                imgId = R.drawable.ic_quality_medium;
                break;
            case "重度污染":
                imgId = R.drawable.ic_quality_serious;
                break;
            case "严重污染":
                imgId = R.drawable.ic_quality_terrible;
                break;
            default:
                imgId = R.drawable.ic_quality_nice;
                break;
        }
        return imgId;
    }

    /**
     * 设置温度
     *
     * @param weatherInfo weatherInfo
     */
    private void setTemperature(WeatherInfo weatherInfo) {
        String temp = weatherInfo.getTemperature();
        if (temp.contains("℃")) {
            temp = temp.replace("℃", "");
        }
        Log.i(TAG, "setTemperature: " + temp);
        temperature1.setVisibility(View.VISIBLE);
        temperature2.setVisibility(View.VISIBLE);
        temperature3.setVisibility(View.VISIBLE);
        if (temp != null) {
            // 两位正数
            if (temp.length() == 2 && !temp.contains("-")) {
                int temp1 = Integer.parseInt(temp.substring(0, 1));
                setTemperatureImage(temp1, temperature1);
                int temp2 = Integer.parseInt(temp.substring(1));
                setTemperatureImage(temp2, temperature2);
                temperature3.setVisibility(View.GONE);
                // 一位
            } else if (temp.length() == 1 && !temp.contains("-")) {
                int temp1 = Integer.parseInt(temp);
                setTemperatureImage(temp1, temperature1);
                temperature2.setVisibility(View.GONE);
                temperature3.setVisibility(View.GONE);
                // 两位负数
            } else if (temp.length() == 2 && temp.contains("-")) {
                temperature1.setImageResource(R.drawable.ic_minus);
                int temp2 = Integer.parseInt(temp.substring(1));
                setTemperatureImage(temp2, temperature2);
                temperature3.setVisibility(View.GONE);
                // 三位负数
            } else if (temp.length() == 3 && temp.contains("-")) {
                temperature1.setImageResource(R.drawable.ic_minus);
                int temp2 = Integer.parseInt(temp.substring(1, 2));
                setTemperatureImage(temp2, temperature2);
                int temp3 = Integer.parseInt(temp.substring(2));
                setTemperatureImage(temp3, temperature3);
            } else {
                temperature1.setImageResource(R.drawable.number_0);
                temperature2.setImageResource(R.drawable.number_0);
                temperature3.setImageResource(R.drawable.number_0);
            }
        } else {
            temperature1.setImageResource(R.drawable.number_0);
            temperature2.setImageResource(R.drawable.number_0);
            temperature3.setImageResource(R.drawable.number_0);
        }
    }

    /**
     * 设置温度图片
     *
     * @param temp1     温度
     * @param imageView imageView控件
     */
    private void setTemperatureImage(int temp1, ImageView imageView) {
        switch (temp1) {
            case 0:
                imageView.setImageResource(R.drawable.number_0);
                break;
            case 1:
                imageView.setImageResource(R.drawable.number_1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.number_2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.number_3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.number_4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.number_5);
                break;
            case 6:
                imageView.setImageResource(R.drawable.number_6);
                break;
            case 7:
                imageView.setImageResource(R.drawable.number_7);
                break;
            case 8:
                imageView.setImageResource(R.drawable.number_8);
                break;
            case 9:
                imageView.setImageResource(R.drawable.number_9);
                break;
            default:
                imageView.setImageResource(R.drawable.number_0);
                break;
        }
    }

    /**
     * 下拉刷新
     *
     * @param city
     * @param province
     */
    public void pullToRefresh(final String city, final String province) {
        mHandler = new Handler();
        mRun = new Runnable() {
            @Override
            public void run() {
                try {
                    mIsPostDelayed = false;
                    if (!getActivity().isFinishing()) {
                        if (!hasActiveUpdated()) {
                            pullRefreshScrollview.setRefreshing();
                            updateWeatherInfo(city, province);
                        }
                        // 加载成功
//                        mHasLoadedOnce = true;
                    }
                } catch (Exception e) {
//                    LogUtil.e(LOG_TAG, e.toString());
                }
            }
        };
        mHandler.postDelayed(mRun, 500);
//        mHandler.post(mRun);
        mIsPostDelayed = true;
    }

    /**
     * 停止正在刷新动画
     */
    private void stopRefresh() {
        // 停止正在刷新动画
        pullRefreshScrollview.onRefreshComplete();
        // 取消刷新按钮的动画
//        mRefreshBtn.clearAnimation();
        // 最近一次更细时间
        mLastActiveUpdateTime = SystemClock.elapsedRealtime();
    }

    /**
     * 是否3秒内主动更新过
     *
     * @return 主动更新与否
     */
    private boolean hasActiveUpdated() {
        if (mLastActiveUpdateTime == 0) {
            return false;
        }
        long now = SystemClock.elapsedRealtime();
        long timeD = now - mLastActiveUpdateTime;
        // 间隔3秒内不再自动更新
        return timeD <= 3000;
    }

    /**
     * 设置下拉刷新
     */
    private void setPullToRefresh() {
        pullRefreshScrollview.getLoadingLayoutProxy().setPullLabel(getString(R.string.pull_to_refresh));
        pullRefreshScrollview.getLoadingLayoutProxy().setRefreshingLabel(
                getString(R.string.refreshing));
        pullRefreshScrollview.getLoadingLayoutProxy().setReleaseLabel(getString(R.string.leave_to_refresh));
        pullRefreshScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                updateWeatherInfo(city, province);
            }
        });
        setTopActionabarBg(R.drawable.topactionbar_bg_01);
    }

    /**
     * 设置上滑刷新的tpbar背景渐变
     *
     * @param topactionbarId
     */
    private void setTopActionabarBg(int topactionbarId) {
        Bitmap mbitmap = BitmapFactory.decodeResource(getResources(), topactionbarId);
        mBlurDrawable = new BitmapDrawable(mbitmap);
        //mBlurDrawable = MyUtil.getWallPaperBlurDrawable(getActivity());
        mDensity = getResources().getDisplayMetrics().density;
        if (scrollViewListener == null) {
            scrollViewListener = new ScrollViewListener() {
                @Override
                public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
//              LogUtil.i(LOG_TAG, "x: " + x + "y: " + y + "oldx: " + oldx + "oldy: " + oldy);
                    // scroll最大滚动距离（xxxh：2320）/密度（xxxh：3）/1.5  =  515
                    mAlpha = Math.round(Math.round(y / mDensity / 1.5));
                    if (mAlpha > 255) {
                        mAlpha = 255;
                    } else if (mAlpha < 0) {
                        mAlpha = 0;
                    }
                    // 设置模糊处理后drawable的透明度
                    mBlurDrawable.setAlpha(mAlpha);
                    // 设置背景
                    //noinspection deprecation
                    topactionbar.setBackgroundDrawable(mBlurDrawable);
                }
            };
        }
        pullRefreshScrollview.setScrollViewListener(scrollViewListener);
    }


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weafragment, container, false);
        pullRefreshScrollview = (PullToRefreshScrollView) view.findViewById(R.id.pull_refresh_scrollview);
        // 设置下拉刷新
        setPullToRefresh();
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        city = arguments.getString("city");
        province = arguments.getString("province");
        boolean isLocal = arguments.getBoolean("isLocal");
        if (isLocal) {
            Resources res = getResources();
            Drawable img_off = res.getDrawable(R.drawable.ic_gps1);
            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
            weatherArea.setCompoundDrawables(img_off, null, null, null);
        }
        weatherArea.setText(city);
//        updateWeatherInfo(city, province);
        pullToRefresh(city, province);
    }

    private void updateWeatherInfo(String city, String province) {
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
                    case ErrorCode.appkeyerror:
                        Log.e(TAG, "onResponse: " + "appkey错误");
                        msg = "appkey错误";
                        handler.sendEmptyMessage(2);
                        break;
                    case ErrorCode.weihu:
                        Log.e(TAG, "onResponse: " + "接口维护");
                        msg = "接口维护";
                        handler.sendEmptyMessage(2);
                        break;
                    case ErrorCode.tingyong:
                        Log.e(TAG, "onResponse: " + "接口停用");
                        msg = "接口停用";
                        handler.sendEmptyMessage(2);
                        break;
                    case ErrorCode.success:
//                        Log.i(TAG, "onResponse: " + ipInfoResponse.result.get(0).toString());
                        weatherInfo = ipInfoResponse.result.get(0);
                        handler.sendEmptyMessage(1);
                        break;
                    case ErrorCode.chaxunbudao:
                        Log.e(TAG, "onResponse: " + "查询不到");
                        msg = "查询不到";
                        handler.sendEmptyMessage(2);
                        break;
                    case ErrorCode.chengshikong:
                        Log.e(TAG, "onResponse: " + "城市为空");
                        msg = "城市为空";
                        handler.sendEmptyMessage(2);
                        break;
                }
            }

            @Override
            public void onFailure(Call<GetWeatherInfoResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                msg = "网络错误";
                handler.sendEmptyMessage(2);
            }
        });
    }
}
