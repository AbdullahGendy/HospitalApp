package com.example.hospitalapp.Models.HospitalInfo;

public class HospitalInfoResponse {
    private String ResultMessageEn;

    private HospitalInfoDataResponse Data;

    private String ResultCode;

    public String getResultMessageEn() {
        return ResultMessageEn;
    }

    public void setResultMessageEn(String ResultMessageEn) {
        this.ResultMessageEn = ResultMessageEn;
    }

    public HospitalInfoDataResponse getData() {
        return Data;
    }

    public void setData(HospitalInfoDataResponse Data) {
        this.Data = Data;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String ResultCode) {
        this.ResultCode = ResultCode;
    }
}
