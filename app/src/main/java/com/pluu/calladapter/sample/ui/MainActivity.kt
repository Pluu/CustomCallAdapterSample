package com.pluu.calladapter.sample.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pluu.calladapter.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViews()
        setUpObserves()
    }

    private fun setUpViews() {
        binding.btnCoroutineNetworkErrorDefault.setOnClickListener {
            viewModel.tryCoroutineNetworkErrorDefault()
        }
        binding.btnSuccessCaseDefault.setOnClickListener {
            viewModel.trySuccessCaseDefault()
        }
        binding.btnCoroutineNetworkError.setOnClickListener {
            viewModel.tryCoroutineNetworkError()
        }
        binding.btnSuccessCase.setOnClickListener {
            viewModel.trySuccessCase()
        }
    }

    private fun setUpObserves() {
        viewModel.showErrorBundle.observe(this) {
            showToast("Error Receive = ${it.throwable.message}")
        }
        viewModel.userEvent.observe(this) { success ->
            showToast("Receive: $success")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}