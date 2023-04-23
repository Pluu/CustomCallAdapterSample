package com.pluu.calladapter.sample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.calladapter.sample.data.DataSource
import com.pluu.calladapter.sample.data.GitHubService
import com.pluu.calladapter.sample.data.model.User
import com.pluu.calladapter.sample.ui.model.ErrorBundle
import com.pluu.retrofit.adapter.onFailure
import com.pluu.retrofit.adapter.onSuccess
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import logcat.logcat

class MainViewModel : ViewModel() {
    private val api: GitHubService = DataSource.service

    private val _showErrorBundle = MutableLiveData<ErrorBundle>()
    val showErrorBundle: LiveData<ErrorBundle> get() = _showErrorBundle

    private val _userEvent = MutableLiveData<User>()
    val userEvent: LiveData<User> get() = _userEvent

    fun tryCoroutineNetworkErrorDefault() {
        val ceh = CoroutineExceptionHandler { _, t ->
            _showErrorBundle.value = ErrorBundle(t)
        }
        viewModelScope.launch(ceh) {
            val result = api.tryNetworkErrorDefault()
            logcat { "Success: $result" }
            _userEvent.value = result
        }
    }

    fun trySuccessCaseDefault() {
        val ceh = CoroutineExceptionHandler { _, t ->
            _showErrorBundle.value = ErrorBundle(t)
        }
        viewModelScope.launch(ceh) {
            val result = api.getUserDefault()
            logcat { "Success: $result" }
            _userEvent.value = result
        }
    }

    fun tryCoroutineNetworkError() {
        viewModelScope.launch {
            api.tryNetworkError()
                .onSuccess { result ->
                    logcat { "Success: $result" }
                    _userEvent.value = result
                }.onFailure { error ->
                    _showErrorBundle.value = ErrorBundle(error.safeThrowable())
                }
        }
    }

    fun trySuccessCase() {
        viewModelScope.launch {
            api.getUser()
                .onSuccess { result ->
                    logcat { "Success: $result" }
                    _userEvent.value = result
                }.onFailure { error ->
                    _showErrorBundle.value = ErrorBundle(error.safeThrowable())
                }
        }
    }
}
