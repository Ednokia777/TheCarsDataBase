package com.tomb.thecarsdata.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.tomb.thecarsdata.R;
import com.tomb.thecarsdata.db.CarContract;

public class CarAdapter extends CursorAdapter {
    public CarAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.lv_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = view.findViewById(R.id.text_name);
        TextView color = view.findViewById(R.id.text_color);
        TextView engine = view.findViewById(R.id.text_engine);
        TextView price = view.findViewById(R.id.text_price);


        String name_string = cursor.getString(cursor.getColumnIndexOrThrow(CarContract.Utils.NAME_KEY));
        String color_string = cursor.getString(cursor.getColumnIndexOrThrow(CarContract.Utils.COLOR_KEY));
        String engine_string = cursor.getString(cursor.getColumnIndexOrThrow(CarContract.Utils.ENGINE_KEY));
        String price_string = cursor.getString(cursor.getColumnIndexOrThrow(CarContract.Utils.PRICE_KEY));


        name.setText(name_string);
        color.setText(color_string);
        engine.setText(engine_string);
        price.setText(price_string);
    }
}
