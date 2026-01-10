package com.example.mybank.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

enum class NotificationType {
    TRANSACTION,
    LOW_BALANCE,
    SECURITY_ALERT,
    PROMOTION,
    INFO
}

@Entity(tableName = "notifications")
data class BankNotification(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("userId")
    val userId: String,
    
    @SerializedName("type")
    val type: NotificationType,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis(),
    
    @SerializedName("isRead")
    val isRead: Boolean = false,
    
    @SerializedName("relatedTransactionId")
    val relatedTransactionId: String? = null,
    
    @SerializedName("relatedAccountId")
    val relatedAccountId: String? = null,
    
    @SerializedName("actionUrl")
    val actionUrl: String? = null
)
