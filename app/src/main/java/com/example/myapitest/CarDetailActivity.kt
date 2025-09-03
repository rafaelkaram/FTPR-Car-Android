package com.example.myapitest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapitest.databinding.ActivityCarDetailBinding
import com.example.myapitest.model.Car
import com.example.myapitest.service.Result
import com.example.myapitest.service.RetrofitClient
import com.example.myapitest.service.safeApiCall
import com.example.myapitest.ui.loadUrl
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityCarDetailBinding

    private lateinit var car: Car
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupView()
        loadCar()
        setupGoogleMap()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if(::car.isInitialized){
            loadCarPlaceInGoogleMap()
        }
    }

    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupGoogleMap(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun loadCarPlaceInGoogleMap(){
        car.place.apply {
            binding.googleMapContent.visibility = View.VISIBLE
            val latLong = LatLng(lat, long)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLong)
                    .title(name)
            )
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(latLong,15f)
            )
        }
    }

    private fun loadCar() {
        val carId = intent.getStringExtra(ARG_ID) ?: ""

        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall {
                RetrofitClient.apiService.getCarById(carId)
            }

            withContext(Dispatchers.Main){
                when(result){
                    is Result.Success -> {
                        car = result.data.value
                        handleSuccess()
                        if(::mMap.isInitialized){
                            loadCarPlaceInGoogleMap()
                        }
                    }
                    is Result.Error ->{
                        handleError()
                    }
                }
            }
        }
    }

    private fun handleSuccess() {
        binding.name.text  = car.name
        binding.year.text  = car.year
        binding.licence.setText(car.licence)
        if (car.imageUrl.isNotEmpty()) {
            binding.image.loadUrl(car.imageUrl)
        }
    }

    private fun handleError() {
        TODO("Not yet implemented")
    }

    companion object {
        private const val ARG_ID = "arg_id"

        fun newIntent(
            context: Context,
            itemId:String,
        ) = Intent(context,CarDetailActivity::class.java).apply{
            putExtra(ARG_ID, itemId)
        }

    }
}