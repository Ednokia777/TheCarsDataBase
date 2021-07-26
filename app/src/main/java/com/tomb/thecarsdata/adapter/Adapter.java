package com.tomb.thecarsdata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tomb.thecarsdata.model.CarModel;
import com.tomb.thecarsdata.MainActivity;
import com.tomb.thecarsdata.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private Context context;
    private ArrayList<CarModel> cars;
    private ArrayList<CarModel> carsAll;
    private MainActivity mainActivity;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView colorTextView;
        public TextView engineTextView;
        public TextView priceTextView;


        public MyViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            colorTextView = view.findViewById(R.id.colorTextView);
            engineTextView = view.findViewById(R.id.engineTextView);
            priceTextView = view.findViewById(R.id.priceTextView);

        }
    }


    public Adapter(Context context, ArrayList<CarModel> cars, MainActivity mainActivity) {
        this.context = context;
        this.cars = cars;
        this.carsAll = new ArrayList<>(cars);
        this.mainActivity = mainActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final CarModel car = cars.get(position);
        holder.nameTextView.setText(car.getName());
        holder.colorTextView.setText(car.getColor());
        holder.engineTextView.setText(car.getEngine());
        holder.priceTextView.setText(car.getPrice() + " $");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addAndEditCars(true, car, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cars.size();
    }


}