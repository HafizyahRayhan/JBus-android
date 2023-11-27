package com.HafizyahRayhanZulikhramJBusBR.jbus_android;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.LoginActivity;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.R;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Account;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.BaseResponse;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.BaseApiService;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.request.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRenterActivity extends AppCompatActivity {

    private EditText companyNameEditText;
    private EditText addressEditText;
    private EditText phoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_renter);

        companyNameEditText = findViewById(R.id.companyNameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyName = companyNameEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();

                Account loggedAccount = LoginActivity.loggedAccount;
                BaseApiService mApiService = UtilsApi.getApiService();
                int id = loggedAccount.id;

                mApiService.registerRenter(id, companyName, address, phoneNumber).enqueue(new Callback<BaseResponse<Account>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                        if (response.isSuccessful()) {
                            BaseResponse<Account> registerResponse = response.body();
                            if (registerResponse.success) {
                                Toast.makeText(RegisterRenterActivity.this, "Renter registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterRenterActivity.this, AboutMeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterRenterActivity.this, registerResponse.message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterRenterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                        Toast.makeText(RegisterRenterActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
