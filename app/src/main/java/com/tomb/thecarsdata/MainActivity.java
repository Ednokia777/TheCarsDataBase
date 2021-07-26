package com.tomb.thecarsdata;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tomb.thecarsdata.adapter.Adapter;
import com.tomb.thecarsdata.db.CarsDB;
import com.tomb.thecarsdata.model.CarModel;
import com.tomb.thecarsdata.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private Adapter carsAdapter;
    private ArrayList<CarModel> carArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CarsDB carsAppDatabase;
    private ArrayAdapter spinnerAdapterNameCar;
    private ArrayAdapter spinnerAdapterColor;
    private ArrayAdapter spinnerAdapterEngine;
    TextView newCarTitle;
    private Spinner nameSpinner;
    private Spinner colorSpinner;
    private Spinner engineSpinner;
    private EditText priceEditText;
    private String[] name_car;
    private String[] colors;
    private String[] engines;
    private Button sortingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        carsAppDatabase = Room.databaseBuilder(getApplicationContext(), CarsDB.class, "CarsDB").build();
        new GetAllCarsAsyncTask().execute();
        carsAdapter = new Adapter(this, carArrayList, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(carsAdapter);
        sortingBtn = findViewById(R.id.sorting);

        sortingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(carArrayList, CarModel.CarsAZ);
                carsAdapter.notifyDataSetChanged();
            }
        });


        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditCars(false, null, -1);
            }
        });
    }

    public void addAndEditCars(final boolean isUpdate, final CarModel car, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_layout, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        //инициализируем наши поля в dialog_layout.xml
        newCarTitle = view.findViewById(R.id.newCarTitle);
        nameSpinner= view.findViewById(R.id.name_spinner);
        colorSpinner= view.findViewById(R.id.color_spinner);
        engineSpinner= view.findViewById(R.id.engine_spinner);
        priceEditText = view.findViewById(R.id.editTextPrice);
        name_car = getResources().getStringArray(R.array.array_name_car);
        colors = getResources().getStringArray(R.array.array_color);
        engines = getResources().getStringArray(R.array.array_engine);

        spinnerAdapterNameCar = new ArrayAdapter(this, android.R.layout.simple_spinner_item, name_car);
        spinnerAdapterNameCar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nameSpinner.setAdapter(spinnerAdapterNameCar);

        spinnerAdapterColor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, colors);
        spinnerAdapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(spinnerAdapterColor);

        spinnerAdapterEngine = new ArrayAdapter(this, android.R.layout.simple_spinner_item, engines);
        spinnerAdapterEngine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        engineSpinner.setAdapter(spinnerAdapterEngine);


        newCarTitle.setText(!isUpdate ? "Add Car" : "Edit Car");
        //если нужно редактировать
        if (isUpdate && car != null) {
            nameSpinner.setSelection(getIndex(nameSpinner, car.getName()));
            colorSpinner.setSelection(getIndex(colorSpinner, car.getColor()));
            engineSpinner.setSelection(getIndex(engineSpinner, car.getEngine()));
            priceEditText.setText(car.getPrice());
        }

        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton(isUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton(isUpdate ? "Delete" : "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {

                                if (isUpdate) {

                                    deleteCar(car, position);
                                } else {

                                    dialogBox.cancel();

                                }

                            }
                        });


        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameSpinner.getSelectedItemId() == Utils.NAME_DEFAULT){
                    Toast.makeText(MainActivity.this, "Укажите марку!", Toast.LENGTH_SHORT).show();
                    return;
                } else if(colorSpinner.getSelectedItemId() == Utils.COLOR_DEFAULT) {
                    Toast.makeText(MainActivity.this, "Цвет не выбран", Toast.LENGTH_SHORT).show();
                    return;
                } else if(engineSpinner.getSelectedItemId() == Utils.ENGINE_DEFAULT) {
                    Toast.makeText(MainActivity.this, "Тип двигателя не выбран", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(priceEditText.getText().toString())){
                    Toast.makeText(MainActivity.this, "Укажите цену!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }


                //если редактируем
                if (isUpdate && car != null) {
                    updateCar (
                            nameSpinner.getSelectedItem().toString(),
                            colorSpinner.getSelectedItem().toString(),
                            engineSpinner.getSelectedItem().toString(),
                            priceEditText.getText().toString(),
                            position);
                } else {
                    createCar(
                            nameSpinner.getSelectedItem().toString(),
                            colorSpinner.getSelectedItem().toString(),
                            engineSpinner.getSelectedItem().toString(),
                            priceEditText.getText().toString());
                }
            }
        });
    }

    private void deleteCar(CarModel car, int position) {
        carArrayList.remove(position);
        new DeleteCarAsyncTask().execute(car);

    }

    private void updateCar(String name, String color, String engine, String price, int position) {
        CarModel car = carArrayList.get(position);
        car.setName(name);
        car.setColor(color);
        car.setEngine(engine);
        car.setPrice(price);
        new UpdateCarAsyncTask().execute(car);
        carArrayList.set(position, car);
    }

    private void createCar(String name, String color, String engine, String price) {
        new CreateCarAsyncTask().execute(new CarModel(0,name, color, engine, price));
    }


    private class GetAllCarsAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            carArrayList.addAll(carsAppDatabase.getCarDao().getAllCars());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            carsAdapter.notifyDataSetChanged();
        }
    }


    private class CreateCarAsyncTask extends AsyncTask<CarModel, Void, Void> {
        @Override
        protected Void doInBackground(CarModel... cars) {
            long id = carsAppDatabase.getCarDao().addCar(
                    cars[0]
            );

            CarModel car = carsAppDatabase.getCarDao().getCar(id);

            if (car != null) {

                carArrayList.add(0, car);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            carsAdapter.notifyDataSetChanged();
        }
    }

    private class UpdateCarAsyncTask extends AsyncTask<CarModel, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            carsAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(CarModel... cars) {
            carsAppDatabase.getCarDao().updateCar(cars[0]);
            return null;
        }
    }

    private class DeleteCarAsyncTask extends AsyncTask<CarModel, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            carsAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(CarModel... cars) {
            carsAppDatabase.getCarDao().deleteCar(cars[0]);
            return null;
        }
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }
}