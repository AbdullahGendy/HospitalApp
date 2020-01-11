package com.example.hospitalapp.activities.Splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hospitalapp.Models.BloodTypesIds.BloodTypesIdsDataResponse;
import com.example.hospitalapp.Models.BloodTypesIds.BloodTypesIdsResponse;
import com.example.hospitalapp.Models.Cities.GetCitiesDataResponseModel;
import com.example.hospitalapp.Models.Cities.GetCitiesResponseModel;
import com.example.hospitalapp.Network.NetworkUtil;
import com.example.hospitalapp.R;
import com.example.hospitalapp.Utills.Constant;
import com.example.hospitalapp.Utills.Validation;
import com.example.hospitalapp.activities.Home.HomeActivity;
import com.example.hospitalapp.activities.Login.LoginActivity;
import com.example.hospitalapp.activities.NoInternet.NoInternetActivity;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.example.hospitalapp.Utills.Constant.CitiesArrayList;
import static com.example.hospitalapp.Utills.Constant.bloodTypeIdArrayList;
import static com.example.hospitalapp.Utills.Constant.bloodTypeArrayList;
import static com.example.hospitalapp.Utills.Constant.CitiesIdArrayList;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CompositeSubscription mSubscriptions = new CompositeSubscription();
        setContentView(R.layout.activity_splash);
        if (Validation.isConnected(this)) {
            mSubscriptions.add(NetworkUtil.getRetrofitNoHeader()
                    .GetCities()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));

            mSubscriptions.add(NetworkUtil.getRetrofitNoHeader()
                    .GetBloodTypes()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponseBloodTypes, this::handleError));

        } else {
            Intent intent = new Intent(Splash.this, NoInternetActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void handleResponseBloodTypes(BloodTypesIdsResponse bloodTypesIdsResponse) {
        switch (bloodTypesIdsResponse.getResultCode()) {
            case "1":
                ArrayList<BloodTypesIdsDataResponse> bloodTypesIdsDataResponse = bloodTypesIdsResponse.getData();
                bloodTypeArrayList = new ArrayList<>();
                bloodTypeIdArrayList = new ArrayList<>();

                for (BloodTypesIdsDataResponse BloodType :
                        bloodTypesIdsDataResponse) {
                    bloodTypeArrayList.add(BloodType.getName());
                    bloodTypeIdArrayList.add(BloodType.getBloodTypeId());
                }
                //Toast.makeText(this, bloodTypeArrayList.size() + "," + bloodTypeIdArrayList.size(), Toast.LENGTH_LONG).show();
                SplashAction();
                break;
            case "0":
            case "2":
                Intent intent = new Intent(Splash.this, NoInternetActivity.class);
                finish();
                startActivity(intent);
                break;
        }
    }

    private void handleError(Throwable throwable) {
        Intent intent = new Intent(Splash.this, NoInternetActivity.class);
        finish();
        startActivity(intent);

    }

    private void handleResponse(GetCitiesResponseModel getCitiesResponseModel) {
        switch (getCitiesResponseModel.getResultCode()) {
            case "1":
                ArrayList<GetCitiesDataResponseModel> getCitiesDataResponseModel = getCitiesResponseModel.getData();
                CitiesArrayList = new ArrayList<>();
                CitiesIdArrayList = new ArrayList<>();

                for (GetCitiesDataResponseModel city :
                        getCitiesDataResponseModel) {
                    CitiesArrayList.add(city.getName());
                    CitiesIdArrayList.add(city.getCityId());
                }
                break;
            case "0":
            case "2":
                //Toast.makeText(this, getCitiesResponseModel.getResultMessageEn(), Toast.LENGTH_LONG).show();
                break;
        }
    }

    void SplashAction() {
        new Handler().postDelayed(() -> {
            SharedPreferences mSharedPreferences = getSharedPreferences("MySharedPreference", MODE_PRIVATE);
            Intent intent;
            if ((mSharedPreferences.getBoolean(Constant.rememberMe, false))) {
                intent = new Intent(Splash.this, HomeActivity.class);
            } else {
                intent = new Intent(Splash.this, LoginActivity.class);
            }

            finish();
            startActivity(intent);
        }, 3000);
    }
}
