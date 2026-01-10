package com.example.mybank.data.repository

import com.example.mybank.data.local.dao.TransactionDao
import com.example.mybank.data.model.Transaction
import com.example.mybank.data.model.TransactionType
import com.example.mybank.data.preferences.PreferencesManager
import com.example.mybank.data.remote.MyBankApiService
import com.example.mybank.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val apiService: MyBankApiService,
    private val preferencesManager: PreferencesManager
) {
    
    // Get transactions from local database (offline-first)
    fun getAccountTransactions(accountId: String): Flow<List<Transaction>> {
        return transactionDao.getAccountTransactions(accountId)
    }
    
    fun getRecentAccountTransactions(accountId: String, limit: Int = 10): Flow<List<Transaction>> {
        return transactionDao.getRecentAccountTransactions(accountId, limit)
    }
    
    fun getRecentUserTransactions(userId: String, limit: Int = 20): Flow<List<Transaction>> {
        return transactionDao.getRecentUserTransactions(userId, limit)
    }
    
    fun getTransaction(transactionId: String): Flow<Transaction?> {
        return transactionDao.getTransaction(transactionId)
    }
    
    fun getTransactionsByDateRange(
        accountId: String,
        startTime: Long,
        endTime: Long
    ): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(accountId, startTime, endTime)
    }
    
    fun getTransactionsByType(accountId: String, type: TransactionType): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByType(accountId, type)
    }
    
    // Sync transactions from API
    suspend fun syncAccountTransactions(accountId: String, limit: Int? = null): Resource<List<Transaction>> {
        return try {
            val token = preferencesManager.authToken.first()
            if (token == null) {
                return Resource.Error("Authentication token not found")
            }
            
            val response = apiService.getAccountTransactions(accountId, "Bearer $token", limit)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data != null) {
                    // Save to local database
                    transactionDao.insertTransactions(apiResponse.data)
                    Resource.Success(apiResponse.data)
                } else {
                    Resource.Error(apiResponse?.message ?: "Failed to fetch transactions")
                }
            } else {
                Resource.Error("Network error: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to sync transactions")
        }
    }
    
    suspend fun syncAllTransactions(limit: Int? = null): Resource<List<Transaction>> {
        return try {
            val token = preferencesManager.authToken.first()
            if (token == null) {
                return Resource.Error("Authentication token not found")
            }
            
            val response = apiService.getAllTransactions("Bearer $token", limit)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data != null) {
                    // Save to local database
                    transactionDao.insertTransactions(apiResponse.data)
                    Resource.Success(apiResponse.data)
                } else {
                    Resource.Error(apiResponse?.message ?: "Failed to fetch transactions")
                }
            } else {
                Resource.Error("Network error: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to sync transactions")
        }
    }
}
