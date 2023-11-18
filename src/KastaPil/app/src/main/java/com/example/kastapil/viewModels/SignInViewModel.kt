package com.example.kastapil.viewModels

import android.app.Application
import android.content.Intent
import android.content.IntentSender
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kastapil.models.GoogleAuthClient
import com.example.kastapil.models.SignInResult
import com.example.kastapil.models.UserData
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)

class SignInViewModel(application: Application) : AndroidViewModel(application) {
    val googleAuthClient by lazy {
        GoogleAuthClient(
            context = application,
            oneTapClient = Identity.getSignInClient(application)
        )
    }

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    suspend fun initiateSignIn(): IntentSender? {
        return googleAuthClient.signIn()
    }

    // Function to handle the sign-in result
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