package com.futuretongfu.model.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Android on 2017/3/23.
 */
public class SQLiteDB {
    private Context ctx;
    private static final String DB_NAME = "historyRecord.db";
    private static final int DB_VERSION = 1;
    private static final String userTable = "historyRecord";
    private static final String record = "record";
    private OpenDBHelper dataBase;
    protected SQLiteDatabase db;

    public SQLiteDB(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * 创建数据库
     */
    public void openDB() {
        dataBase = new OpenDBHelper(ctx, DB_NAME, null, DB_VERSION);
        if (db != null) {
            return;
        }
        try {
            db = dataBase.getWritableDatabase();
        } catch (Exception e) {
            db = dataBase.getReadableDatabase();
        }
    }

    /**
     * 插入语句
     */
    public long insert(HistoryRecordObject object) {
        ContentValues values = new ContentValues();
        values.put(record, object.getRecord());
        return db.insert(userTable, null, values);
    }

    /**
     * 查询数据库
     */
    public HistoryRecordObject[] Query() {
        Cursor cursor = db.query(userTable, new String[]{record}, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return ConvertToRecord(cursor);
    }

    private HistoryRecordObject[] ConvertToRecord(Cursor cursor) {
        int count = cursor.getCount();
        if (count == 0) {
            if (cursor.isClosed() == false) {
                cursor.close();
            }
            return null;
        }
        HistoryRecordObject[] records = new HistoryRecordObject[count];
        cursor.moveToFirst();
        for (int i = 0; i < count; i++) {
            records[i] = new HistoryRecordObject();
            records[i].setRecord(cursor.getString(cursor.getColumnIndex(record)));
            cursor.moveToNext();
        }
        if (cursor.isClosed() == false) {
            cursor.close();
        }
        return records;
    }

    /**
     * 删除数据库
     */
    public void delete() {
        db.delete(userTable, null, null);
    }

    /**
     * 释放数据库
     */
    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    protected class OpenDBHelper extends SQLiteOpenHelper {

        public OpenDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //创建数据库
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            //创建一张数据库表
            String sql = "create table IF NOT EXISTS " + userTable + " ("
                    + record + " varchar(50)  );";
            sqLiteDatabase.execSQL(sql);
        }

        //更新数据库
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
