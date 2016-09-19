package com.syhd.myweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.syhd.myweather.R;
import com.syhd.myweather.adapter.AddCityAdapter;
import com.syhd.myweather.bean.Address;
import com.syhd.myweather.db.dao.AddressDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AddCityActivity extends BaseActicity implements View.OnClickListener {

    private static final String TAG = AddCityActivity.class.getName();
    @BindView(R.id.action_return)
    ImageView actionReturn;
    @BindView(R.id.action_title)
    TextView actionTitle;
    @BindView(R.id.search_city_edit_tv)
    EditText searchCityEditTv;
    @BindView(R.id.clear_btn)
    ImageView clearBtn;
    @BindView(R.id.gv_add_city_title)
    TextView gvAddCityTitle;
    @BindView(R.id.gv_add_city)
    GridView gvAddCity;
    @BindView(R.id.city_contents)
    LinearLayout cityContents;
    @BindView(R.id.no_matched_city_tv)
    TextView noMatchedCityTv;
    @BindView(R.id.lv_search_city)
    ListView lvSearchCity;
    @BindView(R.id.city_manage_background)
    LinearLayout cityManageBackground;
    private String[] hotcities;
    private ArrayList<String> hotcitylists;
    private AddressDao addressDao;

    private String[] cities;
    private List<String> citieslist;

    private List<String> resultList;
    /**
     * 检索城市匹配的列表适配器
     */
    private ArrayAdapter<String> mSearchCityAdapter;

    @Override
    protected int initContentView() {
        return R.layout.activity_add_city;
    }

    @Override
    protected void setStatusBardark() {

    }

    @Override
    protected void initView() {
        actionReturn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        addressDao = new AddressDao(getBaseContext());
        hotcities = getResources().getStringArray(R.array.city_hot);
        hotcitylists = new ArrayList<>();
        for (String s : hotcities) {
            hotcitylists.add(s);
        }
        gvAddCity.setAdapter(new AddCityAdapter(getBaseContext(), hotcitylists));
        gvAddCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = addressDao.find(hotcitylists.get(i));
                Log.i("", "onItemClick: " + s + "======查询结果");
                if ("".equals(s)) {
                    addressDao.add(new Address(hotcitylists.get(i), hotcitylists.get(i), 0));
                    myFinish();
                } else {
                    Toast.makeText(AddCityActivity.this, "已经添加过该城市", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cities = getResources().getStringArray(R.array.city_china);
        citieslist = new ArrayList<>();
        resultList = new ArrayList<>();
        for (String s : cities) {
            citieslist.add(s);
        }
        Log.i(TAG, "initData: " + citieslist.size());
        mSearchCityAdapter = new ArrayAdapter<>(
                this, R.layout.lv_search_city, resultList);
        lvSearchCity.setAdapter(mSearchCityAdapter);
        lvSearchCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String temp = resultList.get(i);
                String city = temp.split("-")[0].trim();
                String pronvice = temp.split("-")[1].trim();
                addressDao.add(new Address(city, pronvice, 0));
                myFinish();
            }
        });
        searchCityEditTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String strs = charSequence.toString().trim();
                if (!"".equals(strs)) {
                    resultList.clear();
                    for (String s : citieslist) {
                        if (s.contains(strs)) {
                            resultList.add(s);
                        }
                    }
                    // 隐藏热门城市视图
                    cityContents.setVisibility(View.GONE);
                    // 显示清除按钮
                    clearBtn.setVisibility(View.VISIBLE);
                    // 匹配的城市不为空
                    if (resultList.size() != 0) {
                        mSearchCityAdapter.notifyDataSetChanged();
                        // 显示查找城市列表
                        lvSearchCity.setSelection(0);
                        lvSearchCity.setVisibility(View.VISIBLE);
                        // 隐藏无匹配城市的提示
                        noMatchedCityTv.setVisibility(View.GONE);
                        // 无匹配的城市
                    } else {
                        // 隐藏查找城市列表
                        lvSearchCity.setVisibility(View.GONE);
                        // 显示无匹配城市的提示
                        noMatchedCityTv.setVisibility(View.VISIBLE);
                    }
                    // 输入内容为空
                } else {
                    // 显示城市视图
                    cityContents.setVisibility(View.VISIBLE);
                    // 隐藏清除按钮
                    clearBtn.setVisibility(View.GONE);
                    // 隐藏查找城市列表
                    lvSearchCity.setVisibility(View.GONE);
                    // 隐藏无匹配城市的提示
                    noMatchedCityTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 结束activity，返回结果到添加城市activity
     */
    private void myFinish() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_return:
                finish();
                break;
            // 清除按钮
            case R.id.clear_btn:
                searchCityEditTv.setText("");
                break;
        }
    }
}
