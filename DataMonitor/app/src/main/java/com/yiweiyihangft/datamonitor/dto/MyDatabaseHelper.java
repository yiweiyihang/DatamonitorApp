package com.yiweiyihangft.datamonitor.dto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 32618 on 2017/2/13.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    /**
     * 创建用户排序表语句
     */
    public static final String CREATE_SORTMAP = "CREATE TABLE sortMap ("
            + "id integer primary key autoincrement, "
            + "index integer)";


    private Context mContext;

    /**
     * 构造函数
     *
     * @param context
     * @param name    数据库名
     * @param factory 返回一个自定义的 Cursor
     * @param version 当前数据库的版本号
     */
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        // 执行表创建语句 创建用户排序表
        db.execSQL(CREATE_SORTMAP);
        // 弹出创建成功提示
        Toast.makeText(mContext, "Create table sortMap", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
