package com.HafizyahRayhanZulikhramJBusBR.jbus_android;


import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Bus;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.BusArrayAdapter;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.BaseApiService;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button[] btns;
    private int currentPage = 0;
    private final int pageSize = 12;
    private int listSize;
    private int noOfPages;
    private List<Bus> listBus = new ArrayList<>();
    private Button prevButton = null;
    private Button nextButton = null;
    private ListView busListView = null;
    private HorizontalScrollView pageScroll = null;
    private BaseApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pageScroll = findViewById(R.id.page_number_scroll);
        busListView = findViewById(R.id.list_item);
        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        listBus();
    }
    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        apiService = UtilsApi.getApiService();
        Call<List<Bus>> call = apiService.getAllBus();
        call.enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listBus = response.body();
                }
                else {
                    Toast.makeText(MainActivity.this, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void listBus() {
        apiService = UtilsApi.getApiService();
        Call<List<Bus>> call = apiService.getAllBus();
        call.enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    listBus = response.body();
                    listSize = listBus.size();
                    paginationFooter();
                    goToPage(currentPage);
                    buttonListener();
                } else
                {
                    Toast.makeText(MainActivity.this, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.payment_button) {
            moveActivity(this, CustomerPaymentActivity.class);
            return true;
        }
        if (id == R.id.people_button) {
            moveActivity(this, AboutMeActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    private void paginationFooter() {
        int val = listSize % pageSize;
        val = val == 0 ? 0 : 1;
        noOfPages = listSize / pageSize + val;
        LinearLayout ll = findViewById(R.id.btn_layout);
        btns = new Button[noOfPages];
        if (noOfPages <= 6) {
            ((FrameLayout.LayoutParams) ll.getLayoutParams()).gravity = Gravity.CENTER;
        }
        for (int i = 0; i < noOfPages; i++) {
            btns[i] = new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText("" + (i + 1));
            btns[i].setTextColor(getResources().getColor(R.color.black));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
            ll.addView(btns[i], lp);
            final int j = i;
            btns[j].setOnClickListener(v -> {
                currentPage = j;
                goToPage(j);
            });
        }
    }
    private void goToPage(int index) {
        for (int i = 0; i < noOfPages; i++) {
            if (i == index) {
                btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_circle_24));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
                scrollToItem(btns[index]);
                viewPaginatedList(listBus, currentPage);
            }
            else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }
    private void scrollToItem(Button item) {
        int scrollX = item.getLeft() - (pageScroll.getWidth() - item.getWidth()) / 2;
        pageScroll.smoothScrollTo(scrollX, 0);
    }
    private void viewPaginatedList(List<Bus> listBus, int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listBus.size());
        List<Bus> paginatedList = listBus.subList(startIndex, endIndex);
        BusArrayAdapter busArrayAdapter = new BusArrayAdapter(this, paginatedList);
        busListView.setAdapter(busArrayAdapter);
    }

    public class BusArrayAdapter extends ArrayAdapter<Bus> {
        public BusArrayAdapter(Context context, List<Bus> objects) {
            super(context, 0, objects);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.bus_view, parent, false);
            }
            TextView busNameTextView = convertView.findViewById(R.id.bus_name);
            Bus bus = getItem(position);
            busNameTextView.setText(bus.name);
            busNameTextView.setOnClickListener(v -> {
                Bus selectedBus = getItem(position);
                if (selectedBus != null) {
                    Intent intent = new Intent(getContext(), BusDetailActivity.class);
                    intent.putExtra("busId", selectedBus.id);
                    getContext().startActivity(intent);
                }
            });
            return convertView;
        }
    }
    private void buttonListener() {
        prevButton.setOnClickListener(v -> {
                    currentPage = currentPage != 0 ? currentPage - 1 : 0;
                    goToPage(currentPage);
                }
        );
        nextButton.setOnClickListener(v -> {
                    currentPage = currentPage != noOfPages - 1 ? currentPage + 1 : currentPage;
                    goToPage(currentPage);
                }
        );
    }

}



