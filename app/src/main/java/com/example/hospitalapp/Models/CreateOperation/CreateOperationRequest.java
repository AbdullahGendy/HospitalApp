package com.example.hospitalapp.Models.CreateOperation;

public class CreateOperationRequest {
    private String HospitalId;

    private String OperationType;

    private String BloodTypeId;

    private String NumberOfBags;

    private String Date;

    public String getHospitalId() {
        return HospitalId;
    }

    public void setHospitalId(String HospitalId) {
        this.HospitalId = HospitalId;
    }

    public String getOperationType() {
        return OperationType;
    }

    public void setOperationType(String OperationType) {
        this.OperationType = OperationType;
    }

    public String getBloodTypeId() {
        return BloodTypeId;
    }

    public void setBloodTypeId(String BloodTypeId) {
        this.BloodTypeId = BloodTypeId;
    }

    public String getNumberOfBags() {
        return NumberOfBags;
    }

    public void setNumberOfBags(String NumberOfBags) {
        this.NumberOfBags = NumberOfBags;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

}
