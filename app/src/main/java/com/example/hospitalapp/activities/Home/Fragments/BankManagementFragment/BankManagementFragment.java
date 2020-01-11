package com.example.hospitalapp.activities.Home.Fragments.BankManagementFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hospitalapp.Models.CreateOperation.CreateOperationRequest;
import com.example.hospitalapp.Models.CreateOperation.CreateOperationResponse;
import com.example.hospitalapp.Network.NetworkUtil;
import com.example.hospitalapp.R;
import com.example.hospitalapp.Utills.Constant;
import com.example.hospitalapp.Utills.Validation;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.Context.MODE_PRIVATE;
import static com.example.hospitalapp.Utills.Constant.buildDialog;


public class BankManagementFragment extends Fragment {
    private Spinner spinnerOperationType, spinnerBloodType, spinnerBloodBagsNumber;
    private Button submitButton;
    private CreateOperationRequest createOperationRequest;
    private CompositeSubscription mSubscriptions;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_managment, container, false);
        spinnerOperationType = view.findViewById(R.id.spinner_operation_type);
        spinnerBloodType = view.findViewById(R.id.spinner_blood_type);
        spinnerBloodBagsNumber = view.findViewById(R.id.spinner_blood_bags_number);
        submitButton = view.findViewById(R.id.submit_button);
        mSubscriptions = new CompositeSubscription();

        ArrayList<String> bloodTypesSpinnerArray = Constant.bloodTypeArrayList;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, bloodTypesSpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodType.setAdapter(adapter);
        submitButton.setOnClickListener(v -> sendOperation());

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void sendOperation() {
        if (Validation.isConnected(Objects.requireNonNull(getActivity())) && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("MySharedPreference", MODE_PRIVATE);

            //String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
            String date = "01-13-2020";
            createOperationRequest = new CreateOperationRequest();
            createOperationRequest.setBloodTypeId(Constant.GetBloodTypeId(spinnerBloodType.getSelectedItem().toString()));
            createOperationRequest.setNumberOfBags(spinnerBloodBagsNumber.getSelectedItem().toString());
            createOperationRequest.setHospitalId(mSharedPreferences.getString(Constant.accessToken, ""));
            createOperationRequest.setOperationType(spinnerOperationType.getSelectedItem().toString());
            createOperationRequest.setDate(date);
            mSubscriptions.add(NetworkUtil.getRetrofitNoHeader()
                    .CreateOperation(createOperationRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        } else if (!(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)) {
            SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("MySharedPreference", MODE_PRIVATE);

            String date = "01-13-2020";
            createOperationRequest = new CreateOperationRequest();
            createOperationRequest.setBloodTypeId(Constant.GetBloodTypeId(spinnerBloodType.getSelectedItem().toString()));
            createOperationRequest.setNumberOfBags(spinnerBloodBagsNumber.getSelectedItem().toString());
            createOperationRequest.setHospitalId(mSharedPreferences.getString(Constant.accessToken, ""));
            createOperationRequest.setOperationType(spinnerOperationType.getSelectedItem().toString());
            createOperationRequest.setDate(date);
            mSubscriptions.add(NetworkUtil.getRetrofitNoHeader()
                    .CreateOperation(createOperationRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        } else {
            buildDialog(getActivity()).show().setCanceledOnTouchOutside(false);
        }
    }

    private void handleResponse(CreateOperationResponse createOperationResponse) {
        switch (createOperationResponse.getResultCode()) {
            case "1":
                //Toast.makeText(getActivity(), "Success Operation.", Toast.LENGTH_SHORT).show();
                break;
            case "0":
            case "2":
                //Toast.makeText(getActivity(), createOperationResponse.getResultMessageEn(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void handleError(Throwable throwable) {
        //Toast.makeText(getActivity(), "Error, Try again.", Toast.LENGTH_SHORT).show();


    }


}