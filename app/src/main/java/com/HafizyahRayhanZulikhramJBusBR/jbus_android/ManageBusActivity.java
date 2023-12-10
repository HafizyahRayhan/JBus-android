package com.HafizyahRayhanZulikhramJBusBR.jbus_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Bus;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.BusArrayAdapter;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.MyBusArrayAdapter;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.BaseApiService;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBusActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private ListView myBusListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);
        getSupportActionBar().setTitle("Manage Bus");
        mContext = this;
        mApiService = UtilsApi.getApiService();
        myBusListView = this.findViewById(R.id.list_bus_items);
        loadMyBus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_manage_bus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.add_button) {
            Intent intent = new Intent(mContext, AddBusActivity.class);
            intent.putExtra("type", "addBus");
            startActivity(intent);
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    protected void loadMyBus() {
        mApiService.getMyBus(LoginActivity.loggedAccount.id).enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (!response.isSuccessful()) return;
                List<Bus> myBusList = response.body();
                MyArrayAdapter adapter = new MyArrayAdapter(mContext, myBusList);
                myBusListView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
            }
        }
        );
    }

    private class MyArrayAdapter extends ArrayAdapter<Bus> {
        public MyArrayAdapter(@NonNull Context context, @NonNull List<Bus> objects) {
            super(context, 0, objects);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View currentItemView = convertView;
            if (currentItemView == null) {
                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.my_bus_view, parent, false);
            }
            Bus currentBus = getItem(position);
            TextView busName = currentItemView.findViewById(R.id.my_bus_name);
            busName.setText(currentBus.name);
            ImageView addSched = currentItemView.findViewById(R.id.schedule_button);
            addSched.setOnClickListener(v -> {
                Intent i = new Intent(mContext, ManageScheduleActivity.class);
                i.putExtra("busId", currentBus.id);
                mContext.startActivity(i);
            });
            return currentItemView;
        }
    }
}