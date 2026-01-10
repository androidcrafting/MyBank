package com.example.mybank.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mybank.data.local.dao.AccountDao
import com.example.mybank.data.local.dao.NotificationDao
import com.example.mybank.data.local.dao.TransactionDao
import com.example.mybank.data.local.dao.UserDao
import com.example.mybank.data.model.Account
import com.example.mybank.data.model.BankNotification
import com.example.mybank.data.model.Transaction
import com.example.mybank.data.model.User

@Database(
    entities = [
        User::class,
        Account::class,
        Transaction::class,
        BankNotification::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MyBankDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun notificationDao(): NotificationDao
    
    companion object {
        const val DATABASE_NAME = "mybank_database"
    }
}
