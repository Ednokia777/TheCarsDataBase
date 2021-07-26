package com.tomb.thecarsdata.db;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tomb.thecarsdata.model.CarModel;

import java.util.List;

@androidx.room.Dao
public interface CarDao {

    @Insert
    public long addCar(CarModel car);

    @Update
    public void updateCar(CarModel car);

    @Delete
    public void deleteCar(CarModel car);

    @Query("select * from cars")
    public List<CarModel> getAllCars();

    //вытаскиваем автомобиль по условию и по столбцу
    @Query("select * from cars where car_id ==:carId")
    public CarModel getCar(long carId);

}
