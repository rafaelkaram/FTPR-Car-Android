package com.example.myapitest.service

import com.example.myapitest.model.Car
import com.example.myapitest.model.CarResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("car") suspend fun getCars(): List<Car>

    @GET("car/{id}") suspend fun getCarById(@Path("id") id: String): CarResponse

    @POST("car") suspend fun createCar(@Body car: Car): Car

    @DELETE("car/{id}") suspend fun deleteCar(@Path("id") id: String)
}
