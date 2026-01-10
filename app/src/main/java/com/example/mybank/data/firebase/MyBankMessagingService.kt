package com.example.mybank.data.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.mybank.MainActivity
import com.example.mybank.R
import com.example.mybank.data.model.BankNotification
import com.example.mybank.data.model.NotificationType
import com.example.mybank.data.preferences.PreferencesManager
import com.example.mybank.data.remote.MyBankApiService
import com.example.mybank.data.repository.NotificationRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class MyBankMessagingService : FirebaseMessagingService() {
    
    @Inject
    lateinit var notificationRepository: NotificationRepository
    
    @Inject
    lateinit var preferencesManager: PreferencesManager
    
    @Inject
    lateinit var apiService: MyBankApiService
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    companion object {
        private const val CHANNEL_ID = "mybank_notifications"
        private const val CHANNEL_NAME = "MyBank Notifications"
    }
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        
        // Send token to server
        serviceScope.launch {
            try {
                val authToken = preferencesManager.authToken.first()
                if (authToken != null) {
                    apiService.registerFcmToken(
                        "Bearer $authToken",
                        mapOf("fcmToken" to token)
                    )
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        val title = message.notification?.title ?: "MyBank"
        val body = message.notification?.body ?: ""
        val notificationType = message.data["type"] ?: "INFO"
        val transactionId = message.data["transactionId"]
        val accountId = message.data["accountId"]
        
        // Save notification to local database
        serviceScope.launch {
            try {
                val userId = preferencesManager.userId.first() ?: return@launch
                
                val notification = BankNotification(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    type = try {
                        NotificationType.valueOf(notificationType)
                    } catch (e: Exception) {
                        NotificationType.INFO
                    },
                    title = title,
                    message = body,
                    timestamp = System.currentTimeMillis(),
                    isRead = false,
                    relatedTransactionId = transactionId,
                    relatedAccountId = accountId
                )
                
                notificationRepository.insertNotification(notification)
            } catch (e: Exception) {
                // Handle error
            }
        }
        
        // Show notification to user
        showNotification(title, body)
    }
    
    private fun showNotification(title: String, message: String) {
        createNotificationChannel()
        
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "MyBank notifications for transactions and alerts"
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
