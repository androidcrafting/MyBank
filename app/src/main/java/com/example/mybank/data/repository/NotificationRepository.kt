package com.example.mybank.data.repository

import com.example.mybank.data.local.dao.NotificationDao
import com.example.mybank.data.model.BankNotification
import com.example.mybank.data.preferences.PreferencesManager
import com.example.mybank.data.remote.MyBankApiService
import com.example.mybank.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao,
    private val apiService: MyBankApiService,
    private val preferencesManager: PreferencesManager
) {
    
    fun getUserNotifications(userId: String): Flow<List<BankNotification>> {
        return notificationDao.getUserNotifications(userId)
    }
    
    fun getUnreadNotifications(userId: String): Flow<List<BankNotification>> {
        return notificationDao.getUnreadNotifications(userId)
    }
    
    fun getUnreadNotificationCount(userId: String): Flow<Int> {
        return notificationDao.getUnreadNotificationCount(userId)
    }
    
    suspend fun markAsRead(notificationId: String) {
        try {
            notificationDao.markAsRead(notificationId)
            
            // Also update on server
            val token = preferencesManager.authToken.first()
            if (token != null) {
                apiService.markNotificationAsRead(notificationId, "Bearer $token")
            }
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    suspend fun markAllAsRead(userId: String) {
        notificationDao.markAllAsRead(userId)
    }
    
    suspend fun insertNotification(notification: BankNotification) {
        notificationDao.insertNotification(notification)
    }
    
    suspend fun deleteNotification(notificationId: String) {
        notificationDao.deleteNotification(notificationId)
    }
    
    // Sync notifications from API
    suspend fun syncNotifications(): Resource<List<BankNotification>> {
        return try {
            val token = preferencesManager.authToken.first()
            if (token == null) {
                return Resource.Error("Authentication token not found")
            }
            
            val response = apiService.getNotifications("Bearer $token")
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data != null) {
                    // Save to local database
                    notificationDao.insertNotifications(apiResponse.data)
                    Resource.Success(apiResponse.data)
                } else {
                    Resource.Error(apiResponse?.message ?: "Failed to fetch notifications")
                }
            } else {
                Resource.Error("Network error: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to sync notifications")
        }
    }
}
