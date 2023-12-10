package com.HafizyahRayhanZulikhramJBusBR.jbus_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Account;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.BaseResponse;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Bus;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.BusType;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Facility;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Station;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.BaseApiService;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBusActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private BusType[] busType = BusType.values();
    private BusType selectedBusType;
    private Spinner busTypeSpinner, departureSpinner, arrivalSpinner;
    private CheckBox acCheckBox, wifiCheckBox, toiletCheckBox, lcdCheckBox;
    private CheckBox coolboxCheckBox, lunchCheckBox, baggageCheckBox,
            electricCheckBox;
    private List<Facility> selectedFacilities = new ArrayList<>();
    private List<Station> stationList = new ArrayList<>();
    private int selectedDeptStationID;
    private int selectedArrStationID;
    private BaseApiService mApiService;
    private Context mContext;
    private EditText name, capacity, price;
    private Button addBusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_bus);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add Bus");
        }
        busTypeSpinner = this.findViewById(R.id.bus_type_dropdown);
        ArrayAdapter adBus = new ArrayAdapter(this, android.R.layout.simple_list_item_1, busType);
        adBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        busTypeSpinner.setAdapter(adBus);
        busTypeSpinner.setOnItemSelectedListener(busTypeOISL);
        mContext = this;
        mApiService = UtilsApi.getApiService();
        getStations();
        departureSpinner = this.findViewById(R.id.departure_dropdown);
        ArrayAdapter deptStations = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stationList);
        deptStations.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        departureSpinner.setAdapter(deptStations);
        departureSpinner.setOnItemSelectedListener(deptOISL);
        arrivalSpinner = this.findViewById(R.id.arrival_dropdown);
        ArrayAdapter arrStations = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stationList);
        arrStations.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        arrivalSpinner.setAdapter(arrStations);
        arrivalSpinner.setOnItemSelectedListener(arrOISL);
        acCheckBox = findViewById(R.id.ac_checkbox);
        initializeCheckBoxes();
        capacity = findViewById(R.id.capacity);
        price = findViewById(R.id.price);
        name = findViewById(R.id.bus);
        addBusButton = findViewById(R.id.add_button);
        addBusButton.setOnClickListener(v -> handleBus());
    }
    private void initializeCheckBoxes() {
        acCheckBox = findViewById(R.id.ac_checkbox);
        wifiCheckBox = findViewById(R.id.wifi_checkbox);
        toiletCheckBox = findViewById(R.id.toilet_checkbox);
        lcdCheckBox = findViewById(R.id.lcd_checkbox);
        coolboxCheckBox = findViewById(R.id.coolbox_checkbox);
        lunchCheckBox = findViewById(R.id.lunch_checkbox);
        baggageCheckBox = findViewById(R.id.baggage_checkbox);
        electricCheckBox = findViewById(R.id.electric_checkbox);
    }
    AdapterView.OnItemSelectedListener busTypeOISL = new
            AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    selectedBusType = busType[position];
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };

    AdapterView.OnItemSelectedListener arrOISL = new
            AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    selectedArrStationID = stationList.get(position).id;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };
    AdapterView.OnItemSelectedListener deptOISL = new
            AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    selectedDeptStationID = stationList.get(position).id;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };


    protected void getStations() {
        mApiService.getAllStation().enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Station> res = response.body();
                if (response.isSuccessful()) {
                    stationList = response.body(); //simpan response body ke listStation
                    ArrayAdapter deptBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, stationList);
                    deptBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                    departureSpinner.setAdapter(deptBus);
                    departureSpinner.setOnItemSelectedListener(deptOISL);
                    ArrayAdapter arrBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, stationList);
                    arrBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                    arrivalSpinner.setAdapter(arrBus);
                    arrivalSpinner.setOnItemSelectedListener(arrOISL);
                }
            }
            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
        selectedBusType = busType[position];
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    private void finishActivity() {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }
    protected void handleBus() {
        if (name.getText().toString().isEmpty() || capacity.getText().toString().equals("") || price.getText().toString().equals("")) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!acCheckBox.isChecked() && !wifiCheckBox.isChecked() && !toiletCheckBox.isChecked() &&
                !lcdCheckBox.isChecked() && !coolboxCheckBox.isChecked() && !lunchCheckBox.isChecked() &&
                !baggageCheckBox.isChecked() && !electricCheckBox.isChecked()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        selectedFacilities.clear(); // Clear the list before updating
        if (acCheckBox.isChecked()) { selectedFacilities.add(Facility.AC);}
        if (wifiCheckBox.isChecked()) { selectedFacilities.add(Facility.WIFI);}
        if (toiletCheckBox.isChecked()) { selectedFacilities.add(Facility.TOILET);}
        if (lcdCheckBox.isChecked()) { selectedFacilities.add(Facility.LCD_TV);}
        if (coolboxCheckBox.isChecked()) { selectedFacilities.add(Facility.COOL_BOX);}
        if (lunchCheckBox.isChecked()) { selectedFacilities.add(Facility.LUNCH);}
        if (baggageCheckBox.isChecked()) { selectedFacilities.add(Facility.LARGE_BAGGAGE);}
        if (electricCheckBox.isChecked()) { selectedFacilities.add(Facility.ELECTRIC_SOCKET);}

        String nameS = name.getText().toString();
        int capacityV = Integer.parseInt(capacity.getText().toString());
        int priceV = Integer.parseInt(price.getText().toString());
        int accountId = LoginActivity.loggedAccount.id;
        List<Facility> facilities = selectedFacilities;
        BusType busType = selectedBusType;
        int stationDepartureId = selectedDeptStationID;
        int stationArrivalId = selectedArrStationID;


        mApiService.createBus(accountId, nameS, capacityV, facilities, busType, priceV, stationDepartureId, stationArrivalId).enqueue(new Callback<BaseResponse<Bus>>() {
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                BaseResponse<Bus> res = response.body();
                if (res.success) {
                    finishActivity();
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}