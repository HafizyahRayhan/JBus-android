package com.HafizyahRayhanZulikhramJBusBR.jbus_android;

import static com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.RetrofitClient.getCustomGson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.BaseResponse;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Bus;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Schedule;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.BaseApiService;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageScheduleActivity extends AppCompatActivity {
    private Spinner yearSpinner;
    private Spinner monthSpinner;
    private Spinner dateSpinner;
    private Spinner hourSpinner;
    private BaseApiService apiService;
    private Context mContext;
    private int busId;
    private ListView scheduleList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedule);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Bus Schedule");
        }
        busId = getIntent().getIntExtra("busId", 0);
        if (LoginActivity.loggedAccount == null) {
            finish();
            Toast.makeText(this, "Anda belum login", Toast.LENGTH_SHORT).show();
            return;
        }

        scheduleList = this.findViewById(R.id.schedule_list_view);
        showSchedule();
        yearSpinner = findViewById(R.id.year_dropdown);
        monthSpinner = findViewById(R.id.month_dropdown);
        dateSpinner = findViewById(R.id.date_dropdown);
        hourSpinner = findViewById(R.id.hour_dropdown);
        List<String> yearValues = Arrays.asList("2023", "2024", "2025");
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, yearValues);
        yearSpinner.setAdapter(yearAdapter);
        List<String> monthValues = Arrays.asList("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, monthValues);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDateSpinner(monthValues.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        List<String> hourValues = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String formattedHour = i < 10 ? "0" + i : String.valueOf(i);
            hourValues.add(formattedHour);
        }
        ArrayAdapter<String> hourAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hourValues);
        hourSpinner.setAdapter(hourAdapter);

        Button addScheduleButton = findViewById(R.id.add_schedule_button);

        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddSchedule();
            }
        });
    }
    private void updateDateSpinner(String selectedMonth) {
        int selectedYear = Integer.parseInt(SpinnerUtils.getSelectedItem(yearSpinner));
        int maxDaysInMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        yearSpinner = findViewById(R.id.year_dropdown);
        dateSpinner = findViewById(R.id.date_dropdown);
        switch (selectedMonth) {
            case "FEBRUARY":
                if (isLeapYear(selectedYear)) {
                    maxDaysInMonth = 29;
                } else {
                    maxDaysInMonth = 28;
                }
                break;
            case "APRIL":
            case "JUNE":
            case "SEPTEMBER":
            case "NOVEMBER":
                maxDaysInMonth = 30;
                break;
            default:
                maxDaysInMonth = 31;
                break;
        }
        List<String> dateValues = new ArrayList<>();
        for (int i = 1; i <= maxDaysInMonth; i++) {
            String formattedDate = i < 10 ? "0" + i : String.valueOf(i);
            dateValues.add(formattedDate);
        }
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dateValues);
        dateSpinner.setAdapter(dateAdapter);
    }
        private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    private String getFormattedTime() {
        String date = SpinnerUtils.getSelectedItem(dateSpinner);
        String hour = SpinnerUtils.getSelectedItem(hourSpinner);
        String year = SpinnerUtils.getSelectedItem(yearSpinner);
        String month = getMonthNumber(SpinnerUtils.getSelectedItem(monthSpinner));
        return year + "-" + month + "-" + date + " " + hour + ":00:00";
    }
    private String getMonthNumber(String monthName) {
        switch (monthName) {
            case "JANUARY": return "01";
            case "FEBRUARY": return "02";
            case "MARCH": return "03";
            case "APRIL": return "04";
            case "MAY": return "05";
            case "JUNE": return "06";
            case "JULY": return "07";
            case "AUGUST": return "08";
            case "SEPTEMBER": return "09";
            case "OCTOBER": return "10";
            case "NOVEMBER": return "11";
            case "DECEMBER": return "12";
            default: return "";
        }
    }
    private void handleAddSchedule() {
        mContext = this;
        apiService = UtilsApi.getApiService();
        String time = getFormattedTime();
        int busId = getIntent().getIntExtra("busId", 0);
        apiService.addSchedule(busId, time).enqueue(new Callback<BaseResponse<Bus>>() {
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                // Handle the response
                if (response.isSuccessful()) {
                    BaseResponse<Bus> res = response.body();
                    if (res.success) {
                        List<Schedule> l = response.body().payload.schedules;
                        ArrayAdapter<Schedule> adapter = new ArrayAdapter<>(mContext, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, l);
                        scheduleList.setAdapter(adapter);
                        Toast.makeText(ManageScheduleActivity.this, "Add schedule berhasil", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                Toast.makeText(ManageScheduleActivity.this, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class ScheduleListAdapter extends ArrayAdapter<Schedule> {

        public ScheduleListAdapter(Context context, List<Schedule> schedule) {
            super(context, 0, schedule);
            mContext = context;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.schedule_item, parent, false);
            }
            Schedule s = getItem(position);
            TextView scheduleTextView = convertView.findViewById(R.id.schedule_list);
            scheduleTextView.setText(s.toString());
            ImageView deleteIconImageView = convertView.findViewById(R.id.delete_icon);
            deleteIconImageView.setTag(position);
            deleteIconImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            return convertView;
        }
    }
    public static class SpinnerUtils {
        public static <T> T getSelectedItem(Spinner spinner) {
            return (T) spinner.getSelectedItem();
        }
    }
    protected void showSchedule() {
        mContext = this;
        apiService = UtilsApi.getApiService();
        int busId = getIntent().getIntExtra("busId", 0);
        apiService.getBusbyId(busId).enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call, Response<Bus> response) {
                if (!response.isSuccessful()) return;
                List<Schedule> l = response.body().schedules;
                ScheduleListAdapter adapter = new ScheduleListAdapter(mContext, l);
                scheduleList.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<Bus> call, Throwable t) {
            }
        });
    }
}