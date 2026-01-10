package com.example.mybank.data.repository

import com.example.mybank.data.local.dao.AccountDao
import com.example.mybank.data.model.Account
import com.example.mybank.data.preferences.PreferencesManager
import com.example.mybank.data.remote.MyBankApiService
import com.example.mybank.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val accountDao: AccountDao,
    private val apiService: MyBankApiService,
    private val preferencesManager: PreferencesManager
) {
    
    // Get accounts from local database (offline-first)
    fun getUserAccounts(userId: String): Flow<List<Account>> {
        return accountDao.getUserAccounts(userId)
    }
    
    fun getAccount(accountId: String): Flow<Account?> {
        return accountDao.getAccount(accountId)
    }
    
    fun getTotalBalance(userId: String): Flow<Double?> {
        return accountDao.getTotalBalance(userId)
    }
    
    // Sync accounts from API and update local database
    suspend fun syncAccounts(): Resource<List<Account>> {
        return try {
            val token = preferencesManager.authToken.first()
            if (token == null) {
                return Resource.Error("Authentication token not found")
            }
            
            val response = apiService.getUserAccounts("Bearer $token")
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data != null) {
                    // Save to local database
                    accountDao.insertAccounts(apiResponse.data)
                    Resource.Success(apiResponse.data)
                } else {
                    Resource.Error(apiResponse?.message ?: "Failed to fetch accounts")
                }
            } else {
                Resource.Error("Network error: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to sync accounts")
        }
    }
    
    // Get account from API
    suspend fun fetchAccountFromApi(accountId: String): Resource<Account> {
        return try {
            val token = preferencesManager.authToken.first()
            if (token == null) {
                return Resource.Error("Authentication token not found")
            }
            
            val response = apiService.getAccount(accountId, "Bearer $token")
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data != null) {
                    // Update local database
                    accountDao.insertAccount(apiResponse.data)
                    Resource.Success(apiResponse.data)
                } else {
                    Resource.Error(apiResponse?.message ?: "Failed to fetch account")
                }
            } else {
                Resource.Error("Network error: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to fetch account")
        }
    }
}
