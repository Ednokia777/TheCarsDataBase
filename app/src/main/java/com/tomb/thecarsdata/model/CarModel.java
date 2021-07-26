package com.tomb.thecarsdata.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Comparator;

@Entity(tableName = "cars")
public class CarModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "car_id")
    private int id;
    @ColumnInfo(name = "car_name")
    private String name;
    @ColumnInfo(name = "car_color")
    private String color;
    @ColumnInfo(name = "car_engine")
    private String engine;
    @ColumnInfo(name = "car_price")
    private String price;

    @Ignore
    public CarModel(int id, int name, int color, int engine, int price) {
    }

    public CarModel(int id, String name, String color, String engine, String price) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.engine = engine;
        this.price = price;
    }

    @Ignore
    public CarModel(String name, String color, String engine, String price) {
        this.name = name;
        this.color = color;
        this.engine = engine;
        this.price = price;
    }

    public static Comparator<CarModel> CarsAZ = new Comparator<CarModel>() {
        @Override
        public int compare(CarModel car1, CarModel car2) {
            return car1.getName().compareTo(car2.getName());
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "Car{" + "id=" + id + ", name=" + name + ", color=" + color + ", engine=" + engine + ", price="  + price + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
