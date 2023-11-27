package com.HafizyahRayhanZulikhramJBusBR.jbus_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Bus;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.BusArrayAdapter;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.MyBusArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class ManageBusActivity extends AppCompatActivity {
    ListView Listbus;
    private Button[] btns;
    private int currentPage = 0;
    private final int pageSize = 6;
    private int listSize;
    private int noOfPages;
    private List<Bus> listBus = new ArrayList<>();
    private Button prevButton = null;
    private Button nextButton = null;
    private ListView busListView = null;
    private HorizontalScrollView pageScroll = null;
    private MyBusArrayAdapter mybusArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);
        Listbus = findViewById(R.id.list_bus_items);
        mybusArrayAdapter= new MyBusArrayAdapter(this, (ArrayList<Bus>) listBus);
        Listbus.setAdapter(mybusArrayAdapter);

        listBus = Bus.sampleBusList(2);
        listSize = listBus.size();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_manage_bus, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_button) {
            openAddBusActivity();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    private void openAddBusActivity() {
        Intent intent = new Intent(this, AddBusActivity.class);
        startActivity(intent);
    }
}