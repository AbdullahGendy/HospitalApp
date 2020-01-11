package com.example.hospitalapp.Models.BloodTypesIds;

import java.util.ArrayList;

public class BloodTypesIdsResponse {
    private String ResultMessageEn;

    private ArrayList<BloodTypesIdsDataResponse> Data;

    private String ResultCode;

    public String getResultMessageEn() {
        return ResultMessageEn;
    }

    public void setResultMessageEn(String ResultMessageEn) {
        this.ResultMessageEn = ResultMessageEn;
    }

    public ArrayList<BloodTypesIdsDataResponse> getData() {
        return Data;
    }

    public void setData(ArrayList<BloodTypesIdsDataResponse> Data) {
        this.Data = Data;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String ResultCode) {
        this.ResultCode = ResultCode;
    }
}
