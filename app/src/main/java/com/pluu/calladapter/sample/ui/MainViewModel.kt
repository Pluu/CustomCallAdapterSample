package com.pluu.calladapter.sample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.calladapter.sample.data.DataSource
import com.pluu.calladapter.sample.data.GitHubService
import com.pluu.calladapter.sample.data.model.User
import com.pluu.retrofit.adapter.apiresult.onFailure
import com.pluu.retrofit.adapter.apiresult.onSuccess
import kotlinx.coroutines.launch
import logcat.logcat

class MainViewModel : ViewModel() {
    private val api: GitHubService = DataSource.service

    private val _showErrorBundle = MutableLiveData<ErrorBundle>()
    val showErrorBundle: LiveData<ErrorBundle> get() = _showErrorBundle

    private val _userEvent = MutableLiveData<User>()
    val userEvent: LiveData<User> get() = _userEvent

    fun tryCoroutineNetworkError() {
        viewModelScope.launch {
            api.suspendTryNetworkError()
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
            api.suspendGetUser()
                .onSuccess { result ->
                    logcat { "Success: $result" }
                    _userEvent.value = result
                }.onFailure { error ->
                    _showErrorBundle.value = ErrorBundle(error.safeThrowable())
                }
        }
    }
}

data class ErrorBundle(
    val throwable: Throwable
)

private fun Throwable.printError() {
    logcat { "Error [Thread=${Thread.currentThread().name}]: $message" }
}