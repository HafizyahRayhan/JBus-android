package com.HafizyahRayhanZulikhramJBusBR.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.BaseResponse;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Bus;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Payment;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Schedule;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Station;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.BaseApiService;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.UtilsApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusDetailActivity extends AppCompatActivity {
    private BaseApiService apiService;
    private Bus detailedBus;

    private TextView busNameTextView;
    private TextView capacityTextView;
    private TextView priceTextView;
    private TextView facilitiesTextView;
    private TextView busTypeTextView;
    private TextView departureTextView;
    private TextView arrivalTextView;
    private Spinner scheduleSpinner;
    private Spinner seatSpinner;
    private Button makeBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);
        int busId = getIntent().getIntExtra("busId", -1);
        if (busId != -1) {
            getBusDetails(busId);
        }
        else {
            Toast.makeText(this, "Bus ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        initializeViews();
        updateUIComponents();
        scheduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateSeatSpinner(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        makeBooking = findViewById(R.id.booking_button);
        makeBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleMakeBooking();
            }
        });
    }
    private void initializeViews() {
        scheduleSpinner = findViewById(R.id.schedule_dropdown);
        seatSpinner = findViewById(R.id.seat_dropdown);
        makeBooking = findViewById(R.id.booking_button);
        busNameTextView = findViewById(R.id.detail_bus_name);
        busTypeTextView = findViewById(R.id.detail_bus_type);
        departureTextView = findViewById(R.id.detail_departure);
        arrivalTextView = findViewById(R.id.detail_arrival);
        capacityTextView = findViewById(R.id.detail_capacity);
        priceTextView = findViewById(R.id.detail_price);
        facilitiesTextView = findViewById(R.id.detail_facilities);
    }

    private void getBusDetails(int busId) {
        apiService = UtilsApi.getApiService();
        Call<Bus> call = apiService.getBusbyId(busId);
        call.enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call, Response<Bus> response) {
                if (response.isSuccessful() && response.body() != null) {
                    detailedBus = response.body();
                    updateUIComponents();
                }
                else {
                    Toast.makeText(BusDetailActivity.this, "Failed to get bus details", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Bus> call, Throwable t) {
                Toast.makeText(BusDetailActivity.this, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        }
        );
    }
    private void updateUIComponents() {
        if (detailedBus != null) {
            busNameTextView.setText(detailedBus.name);
            capacityTextView.setText(String.valueOf(detailedBus.capacity));
            String priceText =  "" + detailedBus.price.price;
            String facilitiesText = TextUtils.join(", ", detailedBus.facilities);
            String busTypeText = detailedBus.busType.toString();
            String departureText = formatStationText(detailedBus.departure);
            String arrivalText = formatStationText(detailedBus.arrival);

            List<Schedule> schedules = detailedBus.schedules;
            ArrayList<String> formattedTimestamps = new ArrayList<>();
            priceTextView.setText(priceText);
            facilitiesTextView.setText(facilitiesText);
            busTypeTextView.setText(busTypeText);
            departureTextView.setText(departureText);
            arrivalTextView.setText(arrivalText);

            for (Schedule schedule : schedules) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedTimestamp = dateFormat.format(schedule.departureSchedule);
                formattedTimestamps.add(formattedTimestamp);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formattedTimestamps);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            scheduleSpinner.setAdapter(adapter);
        }
    }


    private void handleMakeBooking() {
        if (detailedBus != null && scheduleSpinner.getSelectedItemPosition() >= 0 && scheduleSpinner.getSelectedItemPosition() < detailedBus.schedules.size()) {
            Schedule selectedSchedule = detailedBus.schedules.get(scheduleSpinner.getSelectedItemPosition());
            String selectedSeat = seatSpinner.getSelectedItem().toString();
            if (selectedSchedule.seatAvailability.containsKey(selectedSeat) && selectedSchedule.seatAvailability.get(selectedSeat)) {
                makeBooking(selectedSchedule, selectedSeat);
            } else {
                Toast.makeText(BusDetailActivity.this, "Selected seat is not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void makeBooking(Schedule selectedSchedule, String selectedSeat) {
        int renterId = detailedBus.accountId;
        int busId = detailedBus.id;
        int buyerId = LoginActivity.loggedAccount.id;
        String departureDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectedSchedule.departureSchedule);
        apiService.makeBooking(buyerId, renterId, busId, Arrays.asList(selectedSeat), departureDate).enqueue(new Callback<BaseResponse<Payment>>() {
            @Override
            public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Payment> bookingResponse = response.body();
                    if (bookingResponse != null && bookingResponse.success) {
                        Toast.makeText(BusDetailActivity.this, "Booking successful", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(BusDetailActivity.this, bookingResponse.message, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(BusDetailActivity.this, "Failed to make a booking", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                Toast.makeText(BusDetailActivity.this, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String formatStationText(Station station) {
        if (station != null) {
            return station.stationName + " - " + station.city.toString();
        } else {
            return "N/A";
        }
    }

    private void updateSeatSpinner(int selectedPosition) {
        if (detailedBus != null && selectedPosition >= 0 && selectedPosition < detailedBus.schedules.size()) {
            Schedule selectedSchedule = detailedBus.schedules.get(selectedPosition);
            Map<String, Boolean> seatAvailability = selectedSchedule.seatAvailability;
            List<String> availableSeats = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry : seatAvailability.entrySet()) {
                if (entry.getValue()) {
                    availableSeats.add(entry.getKey());
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableSeats);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            seatSpinner.setAdapter(adapter);
        }
    }
}
