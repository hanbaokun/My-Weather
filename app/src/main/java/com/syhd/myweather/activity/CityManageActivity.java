package com.syhd.myweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.syhd.myweather.R;
import com.syhd.myweather.adapter.CityManageAdapter;
import com.syhd.myweather.bean.Address;
import com.syhd.myweather.db.dao.AddressDao;
import com.syhd.myweather.utils.MyUtil;

import java.util.List;

import butterknife.BindView;

public class CityManageActivity extends BaseActicity implements View.OnClickListener {

    private static final String TAG = CityManageActivity.class.getName();
    @BindView(R.id.action_return)
    ImageView actionReturn;
    @BindView(R.id.action_title)
    TextView actionTitle;
    @BindView(R.id.action_refresh)
    ImageView actionRefresh;
    @BindView(R.id.action_refresh_flyt)
    FrameLayout actionRefreshFlyt;
    @BindView(R.id.action_edit)
    ImageView actionEdit;
    @BindView(R.id.action_accept)
    ImageView actionAccept;
    @BindView(R.id.gv_city_manage)
    GridView gvCityManage;
    @BindView(R.id.city_manage_background)
    RelativeLayout cityManageBackground;
    private AddressDao addressDao;
    private List<Address> all;
    private CityManageAdapter adapter;
    private OnItemClickListenerImpl onItemClickListener;
    private int sqlchange=0;

    @Override
    protected int initContentView() {
        return R.layout.activity_city_manage;
    }

    @Override
    protected void setStatusBardark() {
        setStatusBarDark(true);
    }

    @Override
    protected void initView() {
        actionReturn.setOnClickListener(this);
        actionEdit.setOnClickListener(this);
        actionAccept.setOnClickListener(this);
        actionRefresh.setOnClickListener(this);

        onItemClickListener = new OnItemClickListenerImpl();
        gvCityManage.setOnItemClickListener(onItemClickListener);
    }

    @Override
    protected void initData() {
        addressDao = new AddressDao(getBaseContext());
        refreshCity();
    }

    private void refreshCity() {
        all = addressDao.findAll();
        all.add(new Address("A", "A", 0));
        adapter = new CityManageAdapter(getBaseContext(), all);
        adapter.setDelListener(new CityManageAdapter.DelListener() {
            @Override
            public void deleteCity() {
                sqlchange = 1;
            }
        });
        gvCityManage.setAdapter(adapter);
    }

    class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 当为列表最后一项（添加城市）
            if (position == (all.size() - 1)) {
                // 不响应重复点击
                if (MyUtil.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(CityManageActivity.this, AddCityActivity.class);
                startActivityForResult(intent, 0);
            } else {
                Intent intent = new Intent();
                intent.putExtra("position",position);
                intent.putExtra("data",sqlchange);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            myFinish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 结束activity，返回结果到添加城市activity
     */
    private void myFinish() {
        Intent intent = new Intent();
        intent.putExtra("data",sqlchange);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                refreshCity();
                this.sqlchange = 1;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 返回按钮
            case R.id.action_return:
                myFinish();
                break;
            // 编辑按钮
            case R.id.action_edit:
// 显示删除，完成按钮，隐藏修改按钮
                displayDeleteAccept();
                break;
            // 完成按钮
            case R.id.action_accept:
// 隐藏删除，完成按钮,显示修改按钮
                hideDeleteAccept();
                break;
            // 刷新按钮
            case R.id.action_refresh:
                all.clear();
                refreshCity();
                break;
        }
    }

    /**
     * 显示删除，完成按钮，隐藏修改按钮
     */
    private void displayDeleteAccept() {
        // 禁止gridView点击事件
        gvCityManage.setOnItemClickListener(null);
        adapter.setCityDeleteButton(true);
        adapter.notifyDataSetChanged();
        actionEdit.setVisibility(View.GONE);
        actionAccept.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏删除，完成按钮,显示修改按钮
     */
    private void hideDeleteAccept() {
        gvCityManage.setOnItemClickListener(onItemClickListener);
        adapter.setCityDeleteButton(false);
        adapter.notifyDataSetChanged();
        actionAccept.setVisibility(View.GONE);
        actionEdit.setVisibility(View.VISIBLE);
    }
}
