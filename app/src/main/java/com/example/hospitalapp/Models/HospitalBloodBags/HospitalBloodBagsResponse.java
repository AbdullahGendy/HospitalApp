package com.example.hospitalapp.Models.HospitalBloodBags;

import java.util.ArrayList;

public class HospitalBloodBagsResponse {
    private String ResultMessageEn;

    private ArrayList<HospitalBloodBagsDataResponse> Data;

    private String ResultCode;

    public String getResultMessageEn() {
        return ResultMessageEn;
    }

    public void setResultMessageEn(String ResultMessageEn) {
        this.ResultMessageEn = ResultMessageEn;
    }

    public ArrayList<HospitalBloodBagsDataResponse> getData() {
        return Data;
    }

    public void setData(ArrayList<HospitalBloodBagsDataResponse> Data) {
        this.Data = Data;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String ResultCode) {
        this.ResultCode = ResultCode;
    }

}
