package com.HafizyahRayhanZulikhramJBusBR.jbus_android;

import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Account;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.BaseResponse;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.BaseApiService;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.UtilsApi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutMeActivity extends AppCompatActivity {
    private Button topupButton;
    private TextView isRenter;
    private Button btnManageBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        // Ambil referensi UI
        TextView usernameTextView = findViewById(R.id.username);
        TextView emailTextView = findViewById(R.id.email);
        TextView balanceTextView = findViewById(R.id.balance);
        TextView initialTextView = findViewById(R.id.textInitials);
        isRenter = findViewById(R.id.tvRegisteredRenter);
        btnManageBus = findViewById(R.id.btnManageBus);

        // Ambil data akun dari LoginActivity.loggedAccount
        Account loggedAccount = LoginActivity.loggedAccount;

        // Set data akun ke UI
        usernameTextView.setText(loggedAccount.name);
        emailTextView.setText(loggedAccount.email);
        balanceTextView.setText(String.valueOf(loggedAccount.balance));

        if (loggedAccount.name.length() > 0) {
            initialTextView.setText(String.valueOf(loggedAccount.name.charAt(0)).toUpperCase());
        }
        if (loggedAccount.company != null) {
            isRenter.setVisibility(View.VISIBLE);
            btnManageBus.setVisibility(View.VISIBLE);
        } else {
            isRenter.setVisibility(View.GONE);
            btnManageBus.setVisibility(View.GONE);
            TextView notRenter = findViewById(R.id.tvNotRegisteredRenter);
            notRenter.setVisibility(View.VISIBLE);

            TextView registeredNow = findViewById(R.id.tvRegisteredNow);
            registeredNow.setVisibility(View.VISIBLE);
            registeredNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent registerIntent = new Intent(AboutMeActivity.this, RegisterRenterActivity.class);
                    startActivity(registerIntent);
                    Toast.makeText(AboutMeActivity.this, "Register as a renter", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Handle Top-Up Button
        topupButton = findViewById(R.id.btnTopUp);
        topupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTopup(v);
            }
        });

        // Handle Manage Bus Button
        btnManageBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutMeActivity.this, ManageBusActivity.class);
                startActivity(intent);
            }
        });
    }

    public void handleTopup(View view) {
        EditText amountEditText = findViewById(R.id.editTopUpAmount);
        String amountStr = amountEditText.getText().toString();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        if (amount <= 0) {
            Toast.makeText(this, "Topup amount must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Account loggedAccount = LoginActivity.loggedAccount;
        double newBalance = loggedAccount.balance + amount;
        BaseApiService mApiService = UtilsApi.getApiService();
        int id = loggedAccount.id;

        mApiService.topUp(id, amount).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call, Response<BaseResponse<Double>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AboutMeActivity.this, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                BaseResponse<Double> res = response.body();

                if (res.success) {
                    TextView balanceTextView = findViewById(R.id.balance);
                    loggedAccount.balance = newBalance;
                    balanceTextView.setText(String.valueOf(loggedAccount.balance));

                    Toast.makeText(AboutMeActivity.this, "Topup Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AboutMeActivity.this, res.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Double>> call, Throwable t) {
                Toast.makeText(AboutMeActivity.this, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
