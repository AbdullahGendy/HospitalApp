package com.example.hospitalapp.Network;


import com.example.hospitalapp.Models.BloodTypesIds.BloodTypesIdsResponse;
import com.example.hospitalapp.Models.Cities.GetCitiesResponseModel;
import com.example.hospitalapp.Models.CreateOperation.CreateOperationRequest;
import com.example.hospitalapp.Models.CreateOperation.CreateOperationResponse;
import com.example.hospitalapp.Models.HospitalBloodBags.HospitalBloodBagsResponse;
import com.example.hospitalapp.Models.HospitalInfo.HospitalInfoResponse;
import com.example.hospitalapp.Models.Login.LoginRequest;
import com.example.hospitalapp.Models.Login.LoginResponse;
import com.example.hospitalapp.Models.Registration.RegistrationRequest;
import com.example.hospitalapp.Models.Registration.RegistrationResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface RetrofitInterface {


    @POST("/api/HospitalAccount/Registration")
    Observable<RegistrationResponse> Registration(@Body RegistrationRequest registrationRequest);

    @POST("/api/HospitalAccount/Login")
    Observable<LoginResponse> Login(@Body LoginRequest loginRequest);

    @POST("/api/Donation/CreateNewOperation")
    Observable<CreateOperationResponse> CreateOperation(@Body CreateOperationRequest createOperationRequest);

    @GET("api/BloodType/GetBloodTypes")
    Observable<BloodTypesIdsResponse> GetBloodTypes();

    @GET("/api/City/GetCities")
    Observable<GetCitiesResponseModel> GetCities();

    @GET("/api/HospitalAccount/GetHospitalBloodTypes/{id}")
    Observable<HospitalBloodBagsResponse> getHospitalBloodBags(@Path("id") String id);

    @GET("/api/HospitalAccount/GetHospitalInfoByHospitalId/{id}")
    Observable<HospitalInfoResponse> getHospitalInfo(@Path("id") String id);

}