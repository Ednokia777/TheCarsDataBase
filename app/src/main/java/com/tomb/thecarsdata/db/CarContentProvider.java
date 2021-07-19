package com.tomb.thecarsdata.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CarContentProvider extends ContentProvider {
    public static final int ALLCARS = 777;
    public static final int CAR_ID = 999;

    DbHandler dbHandler;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(CarContract.AUTHORITY, CarContract.PATH_CARS, ALLCARS);
        uriMatcher.addURI(CarContract.AUTHORITY, CarContract.PATH_CARS + "/#", CAR_ID);
    }

    @Override
    public boolean onCreate() {
        dbHandler = new DbHandler(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);

        switch (match) {
            case ALLCARS:
                cursor = db.query(CarContract.Utils.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case CAR_ID:
                selection = CarContract.Utils.ID_KEY + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(CarContract.Utils.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                Log.d("incorrect_uri", "неверный uri");
                throw new IllegalArgumentException("Can't query incorrect URI" + uri);
        }


        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        String name = contentValues.getAsString(CarContract.Utils.NAME_KEY);
        if (name == null) {
            throw new IllegalArgumentException("Введите марку");
        }

        String color = contentValues.getAsString(CarContract.Utils.COLOR_KEY);
        if (color== null || !(color == CarContract.Utils.COLOR_WHITE || color == CarContract.Utils.COLOR_WHITE ||
                color == CarContract.Utils.COLOR_BLACK || color == CarContract.Utils.COLOR_GRAY ||
                color == CarContract.Utils.COLOR_RED || color == CarContract.Utils.COLOR_BLUE || color == CarContract.Utils.COLOR_YELLOW)) {
            throw new IllegalArgumentException("укажите цвет");
        }

        String engine = contentValues.getAsString(CarContract.Utils.ENGINE_KEY);
        if (engine == null || !(engine == CarContract.Utils.ENGINE_PETROL || engine == CarContract.Utils.ENGINE_COMBINE ||
                engine == CarContract.Utils.ENGINE_ELECTRO)) {
            throw new IllegalArgumentException("укажите тип двигателя");
        }

        String price = contentValues.getAsString(CarContract.Utils.PRICE_KEY);
        if (price == null) {
            throw new IllegalArgumentException("Укажите цену");
        }
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        int match = uriMatcher.match(uri);

        switch (match) {
            case ALLCARS:
                long id = db.insert(CarContract.Utils.TABLE_NAME, null, contentValues);
                if (id == -1) {
                    Log.d("insert", "Ошибка при вставке данных");
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri, null);

                return ContentUris.withAppendedId(uri, id);

            default:
                Log.d("incorrect_uri", "неверный uri");
                throw new IllegalArgumentException("Can't query incorrect URI" + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case ALLCARS:
                rowsDeleted = db.delete(CarContract.Utils.TABLE_NAME, s, strings);
                break;
            case CAR_ID:
                s = CarContract.Utils.ID_KEY + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(CarContract.Utils.TABLE_NAME, s, strings);
                break;
            default:
                Log.d("incorrect_uri", "неверный uri");
                throw new IllegalArgumentException("Can't delete this URI" + uri);
        }
        if(rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        if(contentValues.containsKey(CarContract.Utils.NAME_KEY)) {
            String name = contentValues.getAsString(CarContract.Utils.NAME_KEY);
            if (name == null) {
                throw new IllegalArgumentException("Введите марку");
            }
        }

        if(contentValues.containsKey(CarContract.Utils.COLOR_KEY)) {
            String color = contentValues.getAsString(CarContract.Utils.COLOR_KEY);
            if (color== null || !(color == CarContract.Utils.COLOR_WHITE || color == CarContract.Utils.COLOR_WHITE ||
                    color == CarContract.Utils.COLOR_BLACK || color == CarContract.Utils.COLOR_GRAY ||
                    color == CarContract.Utils.COLOR_RED || color == CarContract.Utils.COLOR_BLUE || color == CarContract.Utils.COLOR_YELLOW)) {
                throw new IllegalArgumentException("укажите цвет");
            }

        }

        if(contentValues.containsKey(CarContract.Utils.ENGINE_KEY)) {
            String engine = contentValues.getAsString(CarContract.Utils.ENGINE_KEY);
            if (engine == null || !(engine == CarContract.Utils.ENGINE_PETROL || engine == CarContract.Utils.ENGINE_COMBINE ||
                    engine == CarContract.Utils.ENGINE_ELECTRO)) {
                throw new IllegalArgumentException("укажите тип двигателя");
            }
        }

        if(contentValues.containsKey(CarContract.Utils.PRICE_KEY)) {
            String price = contentValues.getAsString(CarContract.Utils.PRICE_KEY);
            if (price == null) {
                throw new IllegalArgumentException("Укажите цену");
            }
        }


        SQLiteDatabase db = dbHandler.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsUpdate;

        switch (match) {
            case ALLCARS:
                rowsUpdate = db.update(CarContract.Utils.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case CAR_ID:
                selection = CarContract.Utils.ID_KEY + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdate = db.update(CarContract.Utils.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                Log.d("incorrect_uri", "неверный uri");
                throw new IllegalArgumentException("Can't update this URI" + uri);
        }

        if (rowsUpdate != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdate;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case ALLCARS:
                return CarContract.Utils.CONTENT_MULTIPLE_ITEMS;
            case CAR_ID:
                return CarContract.Utils.CONTENT_SINGLE_ITEMS;
            default:
                Log.d("incorrect_uri", "неверный uri");
                throw new IllegalArgumentException("unknown URI" + uri);
        }
    }
}
