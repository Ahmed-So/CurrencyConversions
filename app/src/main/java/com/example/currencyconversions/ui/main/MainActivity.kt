package com.example.currencyconversions.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconversions.databinding.ActivityMainBinding
import com.example.currencyconversions.domain.usecase.CurrencyUseCase
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}