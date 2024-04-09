package com.example.apppokeapi.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.apppokeapi.R
import com.example.apppokeapi.domain.usecases.AddPokemonListUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.schedule

@AndroidEntryPoint
class MyBackgroundService : Service() {
    companion object {
        private const val TAG = "MyBackgroundService"
        const  val CHANNEL_ID = "PokemonChannel"
        private val NOTIFICATION_ID = 123
    }

    @Inject
    lateinit var useCase: AddPokemonListUseCase

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        Log.d(TAG, "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started")
        startLoop()
        return START_STICKY // Service will be restarted if terminated by the system
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed")
    }

    private fun startLoop() {
        val mainHandler = Handler(Looper.getMainLooper())
        Timer().schedule(30000) {
            mainHandler.post(object : Runnable {
                override fun run() {
                    CoroutineScope(Dispatchers.IO).launch {
                        useCase()
                        showNotification()
                        Log.d("POKEMON SERVICE", "10 more pokemons added")
                        val intent = Intent("YOUR_ACTION_NAME")
                        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                    }
                    mainHandler.postDelayed(this, 30000)
                }
            })
        }
    }

    fun showNotification(){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Lista de pokemons actualizada")
            .setSmallIcon(R.drawable.pokeball)
            .setContentText("Se han agregado 10 pokemons")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Se han agregado 10 pokemons"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "Pokemon Channel", importance)
            val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
