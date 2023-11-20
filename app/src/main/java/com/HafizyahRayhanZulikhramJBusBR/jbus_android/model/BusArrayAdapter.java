package com.HafizyahRayhanZulikhramJBusBR.jbus_android.model;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.HafizyahRayhanZulikhramJBusBR.jbus_android.R;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Bus;
import java.util.ArrayList;

public class BusArrayAdapter extends ArrayAdapter<Bus> {

    public BusArrayAdapter(@NonNull Context context, ArrayList<Bus> busList) {
        super(context, 0, busList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bus bus = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bus_view, parent, false);
        }
        TextView BusName = convertView.findViewById(R.id.bus_name);
        BusName.setText(bus.name);
        return convertView;
    }
}