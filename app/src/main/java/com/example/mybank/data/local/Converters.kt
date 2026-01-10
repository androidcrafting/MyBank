package com.example.mybank.data.local

import androidx.room.TypeConverter
import com.example.mybank.data.model.AccountType
import com.example.mybank.data.model.NotificationType
import com.example.mybank.data.model.TransactionCategory
import com.example.mybank.data.model.TransactionType

class Converters {
    
    @TypeConverter
    fun fromAccountType(value: AccountType): String {
        return value.name
    }
    
    @TypeConverter
    fun toAccountType(value: String): AccountType {
        return AccountType.valueOf(value)
    }
    
    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }
    
    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }
    
    @TypeConverter
    fun fromTransactionCategory(value: TransactionCategory): String {
        return value.name
    }
    
    @TypeConverter
    fun toTransactionCategory(value: String): TransactionCategory {
        return TransactionCategory.valueOf(value)
    }
    
    @TypeConverter
    fun fromNotificationType(value: NotificationType): String {
        return value.name
    }
    
    @TypeConverter
    fun toNotificationType(value: String): NotificationType {
        return NotificationType.valueOf(value)
    }
}
