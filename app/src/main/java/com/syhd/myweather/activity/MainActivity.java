package com.syhd.myweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.syhd.myweather.R;
import com.syhd.myweather.bean.Address;
import com.syhd.myweather.db.dao.AddressDao;
import com.syhd.myweather.fragment.WeaFragment;
import com.syhd.myweather.utils.SharePreUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends BaseActicity {

    private static final String TAG = "MainActivity";

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.image_add)
    ImageView imageAdd;
    /**
     * 定位信息
     */
    private String city = "朝阳";
    private String province = "北京";
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    if (city.contains("区")) {
                        city = city.replace("区", "");
                    }
                    if (province.contains("市")) {
                        province = province.replace("市", "");
                    }
                    //保存定位的城市
                    saveLocation(city, province);
                    fragments.get(0).pullToRefresh(city,province);
                    break;
            }
            return false;
        }
    });
    private FragAdapter adapter;
    private int id;
    private CircleIndicator indicator;

    private void refreshAllData() {
        pager.removeAllViews();
        pager.removeAllViewsInLayout();
        fragments.clear();
        List<Address> all = addressDao.findAll();
        Log.i(TAG, "refreshAllData: "+all.toString());
        for(int i=0;i<all.size();i++){
            WeaFragment weaFragment = new WeaFragment();
            Bundle args1 = new Bundle();
            if(i==0){
                args1.putBoolean("isLocal", true);
            }else {
                args1.putBoolean("isLocal", false);
            }
            args1.putString("city", all.get(i).getCity());
            args1.putString("province", all.get(i).getProvince());
            weaFragment.setArguments(args1);
            fragments.add(weaFragment);
        }
        adapter = new FragAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        id = SharePreUtil.getIntData("id",0);
        pager.setCurrentItem(id);
        pager.setOffscreenPageLimit(fragments.size());
        if(all.size()==1){
            indicator.setVisibility(View.INVISIBLE);
        }else {
            indicator.setVisibility(View.VISIBLE);
            indicator.setViewPager(pager);
            adapter.registerDataSetObserver(indicator.getDataSetObserver());
        }
    }

    private AddressDao addressDao;
    private ArrayList<WeaFragment> fragments;

    /**
     * 保存定位的城市
     *
     * @param city
     * @param province
     */
    private void saveLocation(String city, String province) {
        addressDao.update(new Address(city, province),1);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void setStatusBardark() {
        setStatusBarDark(true);
    }

    @Override
    protected void initView() {
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CityManageActivity.class);
                startActivityForResult(intent,0);
            }
        });

        indicator = (CircleIndicator)findViewById(R.id.indicator);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                if(data.getIntExtra("data", -1)==1){
                    refreshAllData();
                }else {
                    int position = data.getIntExtra("position", -1);
                    if(position !=-1){
                        pager.setCurrentItem(position);
                    }
                }
            }
        }
    }

    public class FragAdapter extends FragmentStatePagerAdapter {

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            Log.i(TAG, "getItem: "+fragments.get(arg0).getArguments().getString("city"));
            return fragments.get(arg0);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    protected void initData() {
        if(SharePreUtil.getIntData("id", -1)==-1){
            SharePreUtil.saveIntData("id",0);
        }
        this.id = SharePreUtil.getIntData("id",0);
        addressDao = new AddressDao(getBaseContext());
        //定位
        mLocationClient = new LocationClient(getBaseContext());  //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        mLocationClient.start();

        //构造适配器
        fragments = new ArrayList<>();
        List<Address> all = addressDao.findAll();
        for(int i=0;i<all.size();i++){
            if(i==0){
                WeaFragment weaFragment = new WeaFragment();
                Bundle args1 = new Bundle();
                args1.putString("city", all.get(i).getCity());
                args1.putString("province", all.get(i).getProvince());
                args1.putBoolean("isLocal", true);
                weaFragment.setArguments(args1);
                fragments.add(weaFragment);
            }else {
                WeaFragment weaFragment = new WeaFragment();
                Bundle args1 = new Bundle();
                args1.putString("city", all.get(i).getCity());
                args1.putString("province", all.get(i).getProvince());
                args1.putBoolean("isLocal", false);
                weaFragment.setArguments(args1);
                fragments.add(weaFragment);
            }
        }
        adapter = new FragAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(id);
        pager.setOffscreenPageLimit(fragments.size());
        if(all.size()==1){
            indicator.setVisibility(View.INVISIBLE);
        }else {
            indicator.setVisibility(View.VISIBLE);
            indicator.setViewPager(pager);
            adapter.registerDataSetObserver(indicator.getDataSetObserver());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.stop();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1500;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            int locType = location.getLocType();
            switch (locType) {
                case BDLocation.TypeGpsLocation://GPS定位结果
                    city = location.getDistrict();
                    province = location.getCity();
                    mLocationClient.stop();
                    handler.sendEmptyMessage(1);
                    break;
                case BDLocation.TypeNetWorkLocation://网络定位
                    city = location.getDistrict();
                    province = location.getCity();
                    mLocationClient.stop();
                    handler.sendEmptyMessage(1);
                    break;
//                case BDLocation.TypeOffLineLocation://离线定位
//                    city = location.getDistrict();
//                    province = location.getCity();
//                    mLocationClient.stop();
//                    handler.sendEmptyMessage(1);
//                    break;
                default://定位失败
//                    Toast.makeText(getBaseContext(), "定位失败，请检查网络或GPS是否开启", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onReceiveLocation: " + "定位失败");
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.unRegisterLocationListener(myListener);
    }
}
