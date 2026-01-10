package com.example.mybank.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

enum class AccountType {
    CHECKING,  // Compte courant
    SAVINGS,   // Compte épargne
    CREDIT     // Compte crédit
}

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("userId")
    val userId: String,
    
    @SerializedName("accountNumber")
    val accountNumber: String,
    
    @SerializedName("accountName")
    val accountName: String,
    
    @SerializedName("accountType")
    val accountType: AccountType,
    
    @SerializedName("balance")
    val balance: Double,
    
    @SerializedName("currency")
    val currency: String = "EUR",
    
    @SerializedName("iban")
    val iban: String? = null,
    
    @SerializedName("createdAt")
    val createdAt: Long = System.currentTimeMillis(),
    
    @SerializedName("isActive")
    val isActive: Boolean = true
)
