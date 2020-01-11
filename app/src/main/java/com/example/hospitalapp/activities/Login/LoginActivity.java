package com.example.hospitalapp.activities.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.hospitalapp.Models.Login.LoginRequest;
import com.example.hospitalapp.Models.Login.LoginResponse;
import com.example.hospitalapp.Network.NetworkUtil;
import com.example.hospitalapp.R;
import com.example.hospitalapp.Utills.Constant;
import com.example.hospitalapp.Utills.Validation;
import com.example.hospitalapp.activities.Home.HomeActivity;
import com.example.hospitalapp.activities.SignUp.SignUpActivity;

import java.util.Objects;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.example.hospitalapp.Utills.Constant.ErrorDialog;

public class LoginActivity extends AppCompatActivity {
    TextView signUpTextView;
    Button loginButton;

    AppCompatEditText usernameEditText;
    AppCompatEditText passwordEditText;

    CheckBox rememberMeCheckBox;
    private SharedPreferences mSharedPreferences;

    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSharedPreferences = getSharedPreferences("MySharedPreference", MODE_PRIVATE);
        mSubscriptions = new CompositeSubscription();

        rememberMeCheckBox = findViewById(R.id.remember_me_check_box);
        signUpTextView = findViewById(R.id.sign_up_text_view);
        loginButton = findViewById(R.id.login_button);
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        mSharedPreferences = getSharedPreferences("MySharedPreference", MODE_PRIVATE);


        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            finish();
            startActivity(intent);
        });
        loginButton.setOnClickListener(v -> {
            validation(Objects.requireNonNull(usernameEditText.getText()).toString(),
                    Objects.requireNonNull(passwordEditText.getText()).toString());

        });
    }


    private void sendData() {
        if (Validation.isConnected(this)) {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setName(usernameEditText.getText().toString());
            loginRequest.setPassword(passwordEditText.getText().toString());

            mSubscriptions.add(NetworkUtil.getRetrofitNoHeader()
                    .Login(loginRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        } else {
            Constant.buildDialog(this);
        }

    }

    private void handleError(Throwable throwable) {
        //Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void handleResponse(LoginResponse loginResponse) {
        switch (loginResponse.getResultCode()) {
            case "1":

                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putBoolean(Constant.rememberMe, rememberMeCheckBox.isChecked());
                edit.putString(Constant.accessToken, loginResponse.getData());
                edit.apply();

                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case "0":
            case "2":
                Constant.ErrorDialog(this, loginResponse.getResultMessageEn());
                break;
        }
    }

    void validation(String userName, String password) {
        if (!Validation.validateFields(userName)) {
            ErrorDialog(this, "user Name is Empty");
        } else if (!Validation.validateFields(password)) {
            ErrorDialog(this, "Password is Empty");
        } else if (!Validation.validatePassword(password)) {
            ErrorDialog(this, "Password must be greater than 6");
        } else {
            sendData();


        }
    }


}
