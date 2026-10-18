package com.example.kastapil.viewModels

import android.app.Application
import android.content.Intent
import android.content.IntentSender
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kastapil.models.GameInfo
import com.example.kastapil.models.GoogleAuthClient
import com.example.kastapil.models.SignInResult
import com.example.kastapil.models.UserData
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModelFactory(private val googleAuthClient: GoogleAuthClient) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SignInViewModel(googleAuthClient) as T
}

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)

class SignInViewModel(private val googleAuthClient: GoogleAuthClient) : ViewModel() {

    // Holds the state of the sign-in process
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    // Updates the state based on the result of the sign-in process
    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    suspend fun initiateSignIn(): IntentSender? {
        return googleAuthClient.signIn()
    }

    // Handles the result from the sign-in intent and updates the state
    fun handleSignInResult(data: Intent?) {
        viewModelScope.launch {
            val signInResult = googleAuthClient.signInWithIntent(
                intent = data?: return@launch
            )
            onSignInResult(signInResult)
        }
    }

    fun getSignedInUser(): Boolean {
        return googleAuthClient.getSignedInUser() != null
    }

    // Reset state to standard values (false)
    fun resetState() {
        _state.update { SignInState() }
    }
}