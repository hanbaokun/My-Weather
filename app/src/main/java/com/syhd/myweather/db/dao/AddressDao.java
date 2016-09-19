package com.syhd.myweather.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.syhd.myweather.bean.Address;
import com.syhd.myweather.db.AddressOpenHelper;
import com.syhd.myweather.utils.ALog;

import java.util.ArrayList;
import java.util.List;

/**
 * 收货地址数据库，增删改查的API
 * Created by ZhangFuen on 2015/12/11.
 */
public class AddressDao {

    private static final String TAG = AddressDao.class.getSimpleName();
    private AddressOpenHelper addressOpenHelper;

    /**
     * 在构造方法里面初始化helper对象
     *
     * @param context 上下文
     */
    public AddressDao(Context context) {
        addressOpenHelper = new AddressOpenHelper(context);
    }

    /**
     * 添加收货地址
     *
     * @param address 收货地址的对象
     * @return 是否成功添加 Address address
     */
    public boolean add(Address address) {
        SQLiteDatabase addressDB = addressOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        ALog.i(address.toString());
        values.put("city", address.getCity());
        values.put("province", address.getProvince());
        values.put("isdefault", address.getIsdefault());
        long result = addressDB.insert("address", null, values);
        addressDB.close();//关闭数据库释放资源
        if (result != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除收货地址
     *
     * @param city 城市
     * @return true 为更改成功 false 更改失败
     */
    public boolean delete(String city) {
        SQLiteDatabase addressDB = addressOpenHelper.getWritableDatabase();
        int result = addressDB.delete("address", "city=?",
                new String[]{city});
        addressDB.close();
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 修改收货地址
     *
     * @param address
     * @return true 为更改成功 false 更改失败
     */
    public boolean update(Address address) {
        SQLiteDatabase addressDB = addressOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("city", address.getCity());
        values.put("province", address.getProvince());
        values.put("isdefault", address.getIsdefault());
        int result = addressDB.update("address", values, "city=?",
                new String[]{address.getCity()});
        addressDB.close();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 修改收货地址
     *
     * @param address
     * @return true 为更改成功 false 更改失败
     */
    public boolean update(Address address, int id) {
        SQLiteDatabase addressDB = addressOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("city", address.getCity());
        values.put("province", address.getProvince());
//        values.put("isdefault", address.getIsdefault());
        int result = addressDB.update("address", values, "_id=?",
                new String[]{id + ""});
        addressDB.close();
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param
     * @return
     */
    public Address find(int isdefault) {
        SQLiteDatabase addressDB = addressOpenHelper.getWritableDatabase();
        Cursor cursor = addressDB.query("address", new String[]{"city", "province", "isdefault"},
                "isdefault=?", new String[]{isdefault + ""}, null, null, null);
        Address address = null;
        if (cursor.moveToNext()) {
            String city = cursor.getString(0);
            String province = cursor.getString(1);
            int isdefa = cursor.getInt(2);
            address = new Address(city, province, isdefa);
        }
        cursor.close();
        addressDB.close();
        return address;

    }

    /**
     * @param
     * @return
     */
    public String find(String city) {
        SQLiteDatabase addressDB = addressOpenHelper.getWritableDatabase();
        Cursor cursor = addressDB.query("address", new String[]{"isdefault"},
                "city=?", new String[]{city}, null, null, null);
        String isdefault;
        if (cursor.moveToNext()) {
            isdefault = cursor.getString(0);
        } else {
            isdefault = "";
        }
        Log.i(TAG, "find: " + isdefault + "是否是默认城市");
        cursor.close();
        addressDB.close();
        return isdefault;
    }

    /**
     * 获取全部收货地址
     *
     * @return 返回收货地址列表
     */
    public List<Address> findAll() {
        SQLiteDatabase addressDB = addressOpenHelper.getReadableDatabase();
        List<Address> addresses = new ArrayList<>();
        Cursor cursor = addressDB.query("address", new String[]{"_id", "city", "province", "isdefault"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            String city = cursor.getString(1);
            String province = cursor.getString(2);
            int isdefault = cursor.getInt(3);
            Address address = new Address(city, province, isdefault);
            addresses.add(address);
        }
        cursor.close();
        addressDB.close();
        return addresses;
    }

}




























