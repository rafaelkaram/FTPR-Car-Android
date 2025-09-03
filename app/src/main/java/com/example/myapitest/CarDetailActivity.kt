package com.example.myapitest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapitest.databinding.ActivityCarDetailBinding
import com.example.myapitest.model.Car
import com.example.myapitest.service.Result
import com.example.myapitest.service.RetrofitClient
import com.example.myapitest.service.safeApiCall
import com.example.myapitest.ui.loadUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCarDetailBinding

    private lateinit var car: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupView()
        loadCar()
    }

    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
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