package com.sparkly.headlines

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.sparkly.headlines.ui.navigation.AppNavHost
import com.sparkly.headlines.ui.theme.HeadlinesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private var isAuthenticated = false

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isBiometricAvailable()) {
            showBiometricPrompt()
        } else {
            // Fallback: allow access if no biometrics
            isAuthenticated = true
        }

        setContent {
            if(isAuthenticated) {
                HeadlinesTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) {
                        AppNavHost(
                            navController = rememberNavController()
                        )
                    }
                }
            } else {
                HeadlinesTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "No access"
                        )
                    }
                }
            }
        }
    }

    private fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setDescription("Use fingerprint to access the app")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    isAuthenticated = true
                    setContent {
                        HeadlinesTheme {
                            Scaffold(modifier = Modifier.fillMaxSize()) {
                                AppNavHost(
                                    navController = rememberNavController()
                                )
                            }
                        }
                    }
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    finish() // Exit app on cancel/error
                }
            })
        biometricPrompt.authenticate(promptInfo)
    }
}
