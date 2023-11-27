package com.HafizyahRayhanZulikhramJBusBR.jbus_android.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.HafizyahRayhanZulikhramJBusBR.jbus_android.R;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Bus;

import java.util.ArrayList;
import java.util.List;

public class MyBusArrayAdapter extends ArrayAdapter<Bus> {
    public MyBusArrayAdapter(@NonNull Context ctx, ArrayList<Bus> busList){
        super(ctx,0, busList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.my_bus_view, parent, false);
        }
        Bus currentBusPos = getItem(position);
        assert currentBusPos != null;
        TextView busName = currentItemView.findViewById(R.id.my_bus_name);
        busName.setText(currentBusPos.name);
        return currentItemView;
    }
}


