package com.tomb.thecarsdata.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler extends SQLiteOpenHelper {
    public DbHandler(Context context) {
        super(context, CarContract.DB_NAME, null, CarContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + CarContract.Utils.TABLE_NAME + " ("
                + CarContract.Utils.ID_KEY + " INTEGER PRIMARY KEY,"
                + CarContract.Utils.NAME_KEY + " TEXT,"
                + CarContract.Utils.COLOR_KEY + " TEXT,"
                + CarContract.Utils.ENGINE_KEY + " TEXT,"
                + CarContract.Utils.PRICE_KEY + " TEXT"
                + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CarContract.Utils.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
