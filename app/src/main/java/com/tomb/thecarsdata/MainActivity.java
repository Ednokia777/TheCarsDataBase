package com.tomb.thecarsdata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tomb.thecarsdata.adapter.CarAdapter;
import com.tomb.thecarsdata.db.CarContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CAR_LOADER = 1147;
    CarAdapter carAdapter;
    FloatingActionButton floatingActionButton;
    ListView listView;
    EditText searcherFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton = findViewById(R.id.fb);
        listView = findViewById(R.id.mylv);
        searcherFilter = findViewById(R.id.edit);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddOrEditCarActivity.class);
                startActivity(intent);
            }
        });

        carAdapter = new CarAdapter(this, null, false);
        //
        listView.setTextFilterEnabled(true);
        //
        listView.setAdapter(carAdapter);

        searcherFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.this.carAdapter.getFilter().filter(charSequence);
                carAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(MainActivity.this, AddOrEditCarActivity.class);
                Uri uri = ContentUris.withAppendedId(CarContract.Utils.CONTENT_URI, id);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        getSupportLoaderManager().initLoader(CAR_LOADER, null, this);
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

        CursorLoader cursorLoader = new CursorLoader(
                this,
                CarContract.Utils.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        carAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        carAdapter.swapCursor(null);
    }
}