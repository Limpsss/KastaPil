package com.example.kastapil.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kastapil.models.GoogleAuthClient
import com.example.kastapil.models.UserData
import kotlinx.coroutines.launch

class HomeViewModelFactory(private val googleAuthClient: GoogleAuthClient) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(googleAuthClient) as T
}

class HomeViewModel(private val googleAuthClient: GoogleAuthClient): ViewModel() {
    val userData = googleAuthClient.getSignedInUser()
    val isSignedIn = userData != null

    init {
        println(userData?.userId)
    }

    fun signOut() {
        if (isSignedIn) {
            viewModelScope.launch {
                googleAuthClient.signOut()
            }
        }
    }
}