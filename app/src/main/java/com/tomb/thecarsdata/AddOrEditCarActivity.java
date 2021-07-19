package com.tomb.thecarsdata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.tomb.thecarsdata.db.CarContract;

public class AddOrEditCarActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDIT_CAR_LOADER = 1861;
    Uri uri;
    private EditText editName, editPrice;
    private Spinner spinnerColor, spinnerEngine;
    private String color = "не выбран";
    private String engine = "не выбран";
    private ArrayAdapter spinnerAdapterColor;
    private ArrayAdapter spinnerAdapterEngine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        Intent intent = getIntent();
        uri = intent.getData();
        if(uri == null) {
            setTitle("Добавление нового авто");
            invalidateOptionsMenu();
        }
        else {
            setTitle("Редактирование авто");
            getSupportLoaderManager().initLoader(EDIT_CAR_LOADER, null, this);

        }

        editName = findViewById(R.id.editName);
        spinnerColor = findViewById(R.id.editColor);
        spinnerEngine = findViewById(R.id.editEngine);
        editPrice = findViewById(R.id.editPrice);

        final String[] colors = getResources().getStringArray(R.array.array_color);
        final String[] engines = getResources().getStringArray(R.array.array_engine);

        spinnerAdapterColor = new ArrayAdapter(this, android.R.layout.simple_spinner_item, colors);
        spinnerAdapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(spinnerAdapterColor);

        spinnerAdapterEngine = new ArrayAdapter(this, android.R.layout.simple_spinner_item, engines);
        spinnerAdapterEngine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEngine.setAdapter(spinnerAdapterEngine);

            //обрабатываем данные из спинера Цвет

        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedColored = (String) adapterView.getItemAtPosition(i);
                if(!TextUtils.isEmpty(selectedColored)) {
                    if(selectedColored.equals("белый")) {
                        color = "белый";
                    }
                    else if (selectedColored.equals("черный")) {
                        color = "черный";
                    }
                    else if (selectedColored.equals("серый")) {
                        color = "серый";
                    }
                    else if (selectedColored.equals("красный")) {
                        color = "красный";
                    }
                    else if (selectedColored.equals("синий")) {
                        color = "синий";
                    }
                    else if (selectedColored.equals("желтый")) {
                        color = "желтый";;
                    }
                    else {
                        color = "не выбран";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                color = CarContract.Utils.COLOR_DEFAULT;
            }
        });

        //обрабатываем данные из спинера тип двигателя

        spinnerEngine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedEngine = (String) adapterView.getItemAtPosition(i);
                if(!TextUtils.isEmpty(selectedEngine)) {
                    if(selectedEngine.equals("бензин")) {
                        engine = CarContract.Utils.ENGINE_PETROL;
                    }
                    else if (selectedEngine.equals("гибрид")) {
                        engine = CarContract.Utils.ENGINE_COMBINE;
                    }
                    else if (selectedEngine.equals("электро")) {
                        engine = CarContract.Utils.ENGINE_ELECTRO;
                    }
                    else {
                        engine = CarContract.Utils.ENGINE_DEFAULT;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                engine = CarContract.Utils.ENGINE_DEFAULT;
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if(uri == null) {
            MenuItem menuItem = menu.findItem(R.id.del_car);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_car:
                saveCar();
                return true;
            case R.id.del_car:
                showDeleteMemberDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCar() {
        String name = editName.getText().toString().trim();
        String price = editPrice.getText().toString().trim();

        if(TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Введите марке автомобиля", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Введите цену", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (color == CarContract.Utils.COLOR_DEFAULT){
            Toast.makeText(this, "укажите цвет", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (engine == CarContract.Utils.ENGINE_DEFAULT){
            Toast.makeText(this, "укажите цвет", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(CarContract.Utils.NAME_KEY, name);
        contentValues.put(CarContract.Utils.COLOR_KEY, color);
        contentValues.put(CarContract.Utils.ENGINE_KEY, engine);
        contentValues.put(CarContract.Utils.PRICE_KEY, price);

        if(uri == null) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(CarContract.Utils.CONTENT_URI, contentValues);
            if(uri == null) {
                Toast.makeText(this, "При добавлении данных в таблицу возникла ошибка", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsChanged = getContentResolver().update(uri, contentValues, null, null);
            if(rowsChanged == 0) {
                Toast.makeText(this, "При сохранении возникла ошибка", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                CarContract.Utils.ID_KEY,
                CarContract.Utils.NAME_KEY,
                CarContract.Utils.COLOR_KEY,
                CarContract.Utils.ENGINE_KEY,
                CarContract.Utils.PRICE_KEY,
        };

        return new CursorLoader(
                this,
                uri,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()) {
            int nameInt = data.getColumnIndex(CarContract.Utils.NAME_KEY);
            int colorInt = data.getColumnIndex(CarContract.Utils.COLOR_KEY);
            int engineInt = data.getColumnIndex(CarContract.Utils.ENGINE_KEY);
            int priceInt = data.getColumnIndex(CarContract.Utils.PRICE_KEY);

            String nameS = data.getString(nameInt);
            String colorS = data.getString(colorInt);
            String engineS = data.getString(engineInt);
            String priceS = data.getString(priceInt);

            editName.setText(nameS);

            switch (colorS) {
                case CarContract.Utils.COLOR_WHITE:
                    spinnerColor.setSelection(1);
                    break;
                case CarContract.Utils.COLOR_BLACK:
                    spinnerColor.setSelection(2);
                    break;
                case CarContract.Utils.COLOR_GRAY:
                    spinnerColor.setSelection(3);
                    break;
                case CarContract.Utils.COLOR_RED:
                    spinnerColor.setSelection(4);
                    break;
                case CarContract.Utils.COLOR_BLUE:
                    spinnerColor.setSelection(5);
                    break;
                case CarContract.Utils.COLOR_YELLOW:
                    spinnerColor.setSelection(6);
                    break;
                case CarContract.Utils.COLOR_DEFAULT:
                    spinnerColor.setSelection(0);
                    break;
            }

            switch (engineS) {
                case CarContract.Utils.ENGINE_PETROL:
                    spinnerEngine.setSelection(1);
                    break;
                case CarContract.Utils.ENGINE_COMBINE:
                    spinnerEngine.setSelection(2);
                    break;
                case CarContract.Utils.ENGINE_ELECTRO:
                    spinnerEngine.setSelection(3);
                    break;
                case CarContract.Utils.ENGINE_DEFAULT:
                    spinnerEngine.setSelection(0);
                    break;
            }
            editPrice.setText(priceS);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void showDeleteMemberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want delete the member?");
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMember();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteMember() {
        if (uri != null) {
            int rowsDeleted = getContentResolver().delete(uri,
                    null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this,
                        "Deleting of data from the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Member is deleted",
                        Toast.LENGTH_LONG).show();
            }

            finish();

        }
    }
}