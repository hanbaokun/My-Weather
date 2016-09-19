package com.syhd.myweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AddressOpenHelper extends SQLiteOpenHelper {


    public AddressOpenHelper(Context context) {
        //版本号从1开始
        super(context, "address.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table address(_id integer primary key autoincrement,city varchar(100),province varchar(100),isdefault integer)");
        db.execSQL("insert into address values('1','朝阳','北京','1')");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
