package com.example.hospitalapp.activities.Home.Fragments.BankInfoFragment;

import android.annotation.SuppressLint;
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

import com.example.hospitalapp.Models.HospitalBloodBags.HospitalBloodBagsResponse;
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


public class BankInfoFragment extends Fragment {
    private View view;
    private TextView editTextOPlus, editTextOMinus, editTextAbPlus, editTextAbMinus,
            editTextAMinus, editTextAPlus, editTextBPlus, editTextBMinus;
    private CompositeSubscription mSubscriptions;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bank_info, container, false);
        editTextOPlus = view.findViewById(R.id.edit_text_o_plus);
        editTextOMinus = view.findViewById(R.id.edit_text_o_minus);
        editTextAbPlus = view.findViewById(R.id.edit_text_ab_plus);
        editTextAbMinus = view.findViewById(R.id.edit_text_ab_minus);
        editTextAMinus = view.findViewById(R.id.edit_text_a_minus);
        editTextAPlus = view.findViewById(R.id.edit_text_a_plus);
        editTextBPlus = view.findViewById(R.id.edit_text_b_plus);
        editTextBMinus = view.findViewById(R.id.edit_text_b_minus);
        mSubscriptions = new CompositeSubscription();
        SharedPreferences mSharedPreferences;

        mSharedPreferences = getActivity().getSharedPreferences("MySharedPreference", MODE_PRIVATE);

        getBankInfo(mSharedPreferences.getString(Constant.accessToken, ""));
        return view;
    }

    private void getBankInfo(String accessToken) {
        if (Validation.isConnected(Objects.requireNonNull(getActivity()))) {

            mSubscriptions.add(NetworkUtil.getRetrofitNoHeader()
                    .getHospitalBloodBags(accessToken)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        } else {
            buildDialog(getActivity()).show().setCanceledOnTouchOutside(false);
        }
    }


    private void handleError(Throwable throwable) {
        Log.e("Network Error", throwable.getMessage(), throwable);
    }

    @SuppressLint("SetTextI18n")
    private void handleResponse(HospitalBloodBagsResponse hospitalBloodBagsResponse) {
        switch (hospitalBloodBagsResponse.getResultCode()) {
            case "1":
                editTextAPlus.setText(hospitalBloodBagsResponse.getData().get(0).getNumberOfBags() + " Bags");
                editTextAMinus.setText(hospitalBloodBagsResponse.getData().get(1).getNumberOfBags() + " Bags");
                editTextBPlus.setText(hospitalBloodBagsResponse.getData().get(2).getNumberOfBags() + " Bags");
                editTextBMinus.setText(hospitalBloodBagsResponse.getData().get(3).getNumberOfBags() + " Bags");
                editTextOPlus.setText(hospitalBloodBagsResponse.getData().get(4).getNumberOfBags() + " Bags");
                editTextOMinus.setText(hospitalBloodBagsResponse.getData().get(5).getNumberOfBags() + " Bags");
                editTextAbPlus.setText(hospitalBloodBagsResponse.getData().get(6).getNumberOfBags() + " Bags");
                editTextAbMinus.setText(hospitalBloodBagsResponse.getData().get(7).getNumberOfBags() + " Bags");
                break;
            case "0":
            case "2":
                Log.e("InfoError", hospitalBloodBagsResponse.getResultCode() + hospitalBloodBagsResponse.getResultMessageEn());
                break;
        }


    }
}
