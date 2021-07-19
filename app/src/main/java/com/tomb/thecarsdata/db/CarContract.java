package com.tomb.thecarsdata.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class CarContract {

    private CarContract() {

    }

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "cars_db";
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.tomb.thecarsdata";
    public static final String PATH_CARS = "cars";

    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    public static final class Utils implements BaseColumns {

        public static final String TABLE_NAME = "cars";

        public static final String ID_KEY = BaseColumns._ID;
        public static final String NAME_KEY = "name";
        public static final String COLOR_KEY = "color";
        public static final String ENGINE_KEY = "engine";
        public static final String PRICE_KEY = "price";

        public static final String COLOR_WHITE = "белый";
        public static final String COLOR_BLACK = "ерный";
        public static final String COLOR_GRAY = "серый";
        public static final String COLOR_RED = "красный";
        public static final String COLOR_BLUE = "синий";
        public static final String COLOR_YELLOW = "желтый";
        public static final String COLOR_DEFAULT = "не выбран";

        public static final String ENGINE_PETROL = "бензин";
        public static final String ENGINE_COMBINE = "гибрид";
        public static final String ENGINE_ELECTRO = "электро";
        public static final String ENGINE_DEFAULT = "не выбран";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CARS);
        public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_CARS;
        public static final String CONTENT_SINGLE_ITEMS = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +AUTHORITY + "/" + PATH_CARS;
    }
}