package com.example.assignment.DatabaseSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "QLThuChi", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE THUCHI(" +
                "maKhoan integer PRIMARY KEY AUTOINCREMENT," +
                "tenKhoan text," +
                "loaiKhoan integer)";
        db.execSQL(sql);
        sql = "CREATE TABLE GIAODICH(" +
                "maGd integer PRIMARY KEY AUTOINCREMENT," +
                "moTaGd text," +
                "ngayGd date," +
                "soTien integer, " +
                "ghiChu text," +
                "maKhoan integer REFERENCES thuchi(maKhoan))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS thuchi");
        db.execSQL("DROP TABLE IF EXISTS giaodich");
        onCreate(db);
    }
}
