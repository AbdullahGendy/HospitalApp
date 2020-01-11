package com.example.hospitalapp.Models.HospitalBloodBags;

public class HospitalBloodBagsDataResponse {
    private String BloodTypeName;

    private String HospitalId;

    private String NumberOfBags;

    public String getBloodTypeName() {
        return BloodTypeName;
    }

    public void setBloodTypeName(String BloodTypeName) {
        this.BloodTypeName = BloodTypeName;
    }

    public String getHospitalId() {
        return HospitalId;
    }

    public void setHospitalId(String HospitalId) {
        this.HospitalId = HospitalId;
    }

    public String getNumberOfBags() {
        return NumberOfBags;
    }

    public void setNumberOfBags(String NumberOfBags) {
        this.NumberOfBags = NumberOfBags;
    }

}
