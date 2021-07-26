package com.tomb.thecarsdata.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tomb.thecarsdata.model.CarModel;

@Database(entities = {CarModel.class}, version = 1)
public abstract class CarsDB extends RoomDatabase {
    public abstract CarDao getCarDao();
}
