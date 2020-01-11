package com.example.hospitalapp.activities.SignUp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hospitalapp.Models.Registration.RegistrationRequest;
import com.example.hospitalapp.Models.Registration.RegistrationResponse;
import com.example.hospitalapp.Network.NetworkUtil;
import com.example.hospitalapp.R;
import com.example.hospitalapp.Utills.Constant;
import com.example.hospitalapp.Utills.Validation;
import com.example.hospitalapp.activities.Home.HomeActivity;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.example.hospitalapp.Utills.Constant.ErrorDialog;

public class SignUpActivity extends AppCompatActivity {

    Button SignUpButton;
    EditText nameTextView;
    EditText passwordTextView;
    EditText confirmPasswordTextView;
    EditText mobileTextView;
    EditText addressTextView;
    private SharedPreferences mSharedPreferences;
    Spinner spinnerCities;
    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mSharedPreferences = getSharedPreferences("MySharedPreference", MODE_PRIVATE);
        mSubscriptions = new CompositeSubscription();
        nameTextView = findViewById(R.id.name_text_view);
        addressTextView = findViewById(R.id.address_text_view);
        passwordTextView = findViewById(R.id.password_edit_text);
        confirmPasswordTextView = findViewById(R.id.confirm_password_text_view);
        mobileTextView = findViewById(R.id.mobile_text_view);
        SignUpButton = findViewById(R.id.sign_up_button);
        spinnerCities = findViewById(R.id.spinner_governorate);

        ArrayList<String> CitiesSpinnerArray = Constant.CitiesArrayList;
        ArrayAdapter<String> CitiesAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, CitiesSpinnerArray);
        CitiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCities.setAdapter(CitiesAdapter);
        SignUpButton.setOnClickListener(v -> {

            validation(
                    nameTextView.getText().toString()
                    , passwordTextView.getText().toString()
                    , confirmPasswordTextView.getText().toString()
                    , mobileTextView.getText().toString()
                    , addressTextView.getText().toString()
            );

        });


    }

    private void sendData() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setName(nameTextView.getText().toString());
        registrationRequest.setMobileNumber(mobileTextView.getText().toString());
        registrationRequest.setPassword(passwordTextView.getText().toString());
        registrationRequest.setCityId(Constant.GetCityId(spinnerCities.getSelectedItem().toString()));
        registrationRequest.setAddress(addressTextView.getText().toString());

        if (Validation.isConnected(this)) {
            mSubscriptions.add(NetworkUtil.getRetrofitNoHeader()
                    .Registration(registrationRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        } else {
            Constant.buildDialog(this);
        }
    }

    private void handleError(Throwable throwable) {

    }

    private void handleResponse(RegistrationResponse registrationResponse) {
        switch (registrationResponse.getResultCode()) {
            case "1":
                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putString(Constant.accessToken, registrationResponse.getData());
                edit.apply();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case "0":
            case "2":
                //Toast.makeText(this, registrationResponse.getResultMessageEn(), Toast.LENGTH_LONG).show();

                break;
        }
    }

    void validation(String name, String password, String confirmPassword, String mobile, String address) {
        if (!Validation.validateFields(name)) {
            ErrorDialog(this, "Name is Empty");
        } else if (!Validation.validateFields(password)) {
            ErrorDialog(this, "Password is Empty");
        } else if (!Validation.validatePassword(password)) {
            ErrorDialog(this, "Password must be greater than 6");
        } else if (!Validation.validateFields(confirmPassword)) {
            ErrorDialog(this, "confirmed Password is Empty");
        } else if (!Validation.validatePassword(confirmPassword)) {
            ErrorDialog(this, "confirmed Password must be greater than 6");
        } else if (!password.equals(confirmPassword)) {
            ErrorDialog(this, "password not match");
        } else if (!Validation.validateFields(mobile)) {
            ErrorDialog(this, "phone is Empty");
        } else if (!Validation.validatePhone(mobile)) {
            ErrorDialog(this, "please check phone");
        } else if (!Validation.validateFields(address)) {
            ErrorDialog(this, "Address is Empty");
        } else sendData();
    }

}
