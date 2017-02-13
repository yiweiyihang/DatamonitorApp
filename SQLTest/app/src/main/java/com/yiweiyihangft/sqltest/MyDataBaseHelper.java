package com.yiweiyihangft.sqltest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 32618 on 2017/2/13.
 */

public class MyDataBaseHelper extends SQLiteOpenHelper {

    /**
     * 创建用户排序表语句
     */
    public static final String CREATE_SORTMAP = "create table sortMap ("
            + "id integer primary key autoincrement, "
            + "sort integer)";

    public static final String CREATE_RESORTMAP = "create table resortMap ("
            + "id integer primary key autoincrement, "
            + "sort integer)";

    private Context mContext;

    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    /**
     * 创建数据库
     *
     * @param db 数据库对象
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 执行表创建语句 创建用户排序表
        db.execSQL(CREATE_SORTMAP);
        db.execSQL(CREATE_RESORTMAP);
        // 弹出创建成功提示
        Toast.makeText(mContext, "Create table sortsortMap", Toast.LENGTH_SHORT).show();

    }

    /**
     * 升级数据库
     *
     * @param sqLiteDatabase 数据库对象
     * @param i    oldVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists sortMap");
        db.execSQL("drop table if exists resortMap");
        onCreate(db);

    }
}
