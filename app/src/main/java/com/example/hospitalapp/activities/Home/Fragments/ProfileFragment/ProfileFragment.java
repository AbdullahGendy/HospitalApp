package com.example.hospitalapp.activities.Home.Fragments.ProfileFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hospitalapp.Models.HospitalInfo.HospitalInfoResponse;
import com.example.hospitalapp.Network.NetworkUtil;
import com.example.hospitalapp.R;
import com.example.hospitalapp.Utills.Constant;
import com.example.hospitalapp.Utills.Validation;

import java.util.Objects;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.Context.MODE_PRIVATE;
import static com.example.hospitalapp.Utills.Constant.buildDialog;


public class ProfileFragment extends Fragment {

    private CompositeSubscription mSubscriptions;
    private TextView textViewHospitalName, textViewHospitalCity, textViewHospitalPhoneNumber, textViewHospitalAddress;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        textViewHospitalName = view.findViewById(R.id.text_view_hospital_name);
        textViewHospitalCity = view.findViewById(R.id.text_view_hospital_city);
        textViewHospitalPhoneNumber = view.findViewById(R.id.text_view_hospital_phone_number);
        textViewHospitalAddress = view.findViewById(R.id.text_view_hospital_address);
        mSubscriptions = new CompositeSubscription();
        SharedPreferences mSharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("MySharedPreference", MODE_PRIVATE);
        getHospitalInfo(mSharedPreferences.getString(Constant.accessToken, ""));

        return view;
    }

    private void getHospitalInfo(String accessToken) {
        if (Validation.isConnected(Objects.requireNonNull(getActivity()))) {

            mSubscriptions.add(NetworkUtil.getRetrofitNoHeader()
                    .getHospitalInfo(accessToken)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        } else {
            buildDialog(getActivity()).show().setCanceledOnTouchOutside(false);
        }
    }

    private void handleResponse(HospitalInfoResponse hospitalInfoResponse) {
        switch (hospitalInfoResponse.getResultCode()) {
            case "1":
                textViewHospitalAddress.setText(hospitalInfoResponse.getData().getAddress());
                textViewHospitalCity.setText(hospitalInfoResponse.getData().getCity());
                textViewHospitalName.setText(hospitalInfoResponse.getData().getName());
                textViewHospitalPhoneNumber.setText(hospitalInfoResponse.getData().getMobileNumber());
                break;
            case "0":
            case "2":
                //Toast.makeText(getActivity(), hospitalInfoResponse.getResultCode() + hospitalInfoResponse.getResultMessageEn(), Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void handleError(Throwable throwable) {
        Log.e("Network Error", throwable.getMessage(), throwable);
        //Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }


}
