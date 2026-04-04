package com.potatodevs.cropsamarica

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.potatodevs.cropsamarica.datastore.LocaleHelper

import com.potatodevs.cropsamarica.ui.index.IndexScreen
import com.potatodevs.cropsamarica.ui.theme.CropSamaricaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

fun restartApp(context: Context) {
    val intent = Intent(context,MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        setupNotificationPermissionLauncher()
        setContent {
            CropSamaricaTheme {
                LaunchedEffect(Unit) {
                    askNotificationPermission()
                }
                IndexScreen()

            }
            generateToken()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val sharedPref = newBase?.getSharedPreferences("settings", MODE_PRIVATE)
        val languageCode = sharedPref?.getString("language", "en") ?: "en"
        val wrapped = LocaleHelper.wrapContext(newBase ?: this, languageCode)
        super.attachBaseContext(wrapped)
    }

    private fun setupNotificationPermissionLauncher() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

        }
    }
    private fun generateToken() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseMessaging.getInstance().subscribeToTopic(userId)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FCM_TOKEN_LAUNCHER", "Subscribed to topic: $userId")
                    } else {
                        Log.e("FCM_TOKEN_LAUNCHER", "Subscription failed", task.exception)
                    }
                }
        } else {
            Log.w("FCM_TOKEN_LAUNCHER", "Permission denied or user is null")
        }
    }


    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    retrieveToken()
                }

                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) -> {
                    showPermissionRationaleDialog()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            retrieveToken()
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission required")
            .setMessage("This app uses notifications to alert you about important updates. Please allow notifications to stay informed.")
            .setPositiveButton("Allow") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun retrieveToken() {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.d("FCM_TOKEN_LAUNCHER", token)
            }
            .addOnFailureListener { e ->
                Log.e("FCM_TOKEN_LAUNCHER", "Failed to retrieve token", e)
            }
    }
}