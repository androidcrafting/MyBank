package com.example.mybank.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

enum class TransactionType {
    DEBIT,   // Débit (sortie d'argent)
    CREDIT   // Crédit (entrée d'argent)
}

enum class TransactionCategory {
    TRANSFER,
    PAYMENT,
    DEPOSIT,
    WITHDRAWAL,
    SALARY,
    BILL,
    SHOPPING,
    FOOD,
    TRANSPORT,
    OTHER
}

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("accountId")
    val accountId: String,
    
    @SerializedName("type")
    val type: TransactionType,
    
    @SerializedName("category")
    val category: TransactionCategory,
    
    @SerializedName("amount")
    val amount: Double,
    
    @SerializedName("currency")
    val currency: String = "EUR",
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("recipientName")
    val recipientName: String? = null,
    
    @SerializedName("recipientAccount")
    val recipientAccount: String? = null,
    
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis(),
    
    @SerializedName("status")
    val status: String = "COMPLETED",
    
    @SerializedName("balanceAfter")
    val balanceAfter: Double? = null
)
