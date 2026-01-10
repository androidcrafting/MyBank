package com.example.mybank.data.firebase

import com.example.mybank.data.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    
    // ==================== USERS ====================
    
    suspend fun createUser(user: User) {
        firestore.collection("users")
            .document(user.id)
            .set(user)
            .await()
    }
    
    suspend fun getUser(userId: String): User? {
        return firestore.collection("users")
            .document(userId)
            .get()
            .await()
            .toObject(User::class.java)
    }
    
    suspend fun updateUser(userId: String, updates: Map<String, Any>) {
        firestore.collection("users")
            .document(userId)
            .update(updates)
            .await()
    }
    
    // ==================== ACCOUNTS ====================
    
    suspend fun createAccount(account: Account) {
        firestore.collection("accounts")
            .document(account.id)
            .set(account)
            .await()
    }
    
    fun getUserAccounts(userId: String): Flow<List<Account>> = callbackFlow {
        val listener = firestore.collection("accounts")
            .whereEqualTo("userId", userId)
            .whereEqualTo("isActive", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val accounts = snapshot?.documents?.mapNotNull { 
                    it.toObject(Account::class.java) 
                } ?: emptyList()
                trySend(accounts)
            }
        awaitClose { listener.remove() }
    }
    
    suspend fun getAccount(accountId: String): Account? {
        return firestore.collection("accounts")
            .document(accountId)
            .get()
            .await()
            .toObject(Account::class.java)
    }
    
    suspend fun updateAccountBalance(accountId: String, newBalance: Double) {
        firestore.collection("accounts")
            .document(accountId)
            .update("balance", newBalance)
            .await()
    }
    
    // ==================== TRANSACTIONS ====================
    
    suspend fun createTransaction(transaction: Transaction) {
        firestore.collection("transactions")
            .document(transaction.id)
            .set(transaction)
            .await()
    }
    
    fun getAccountTransactions(accountId: String, limit: Int = 50): Flow<List<Transaction>> = callbackFlow {
        val listener = firestore.collection("transactions")
            .whereEqualTo("accountId", accountId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val transactions = snapshot?.documents?.mapNotNull { 
                    it.toObject(Transaction::class.java) 
                } ?: emptyList()
                trySend(transactions)
            }
        awaitClose { listener.remove() }
    }
    
    fun getUserTransactions(userId: String, limit: Int = 50): Flow<List<Transaction>> = callbackFlow {
        val listener = firestore.collection("transactions")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val transactions = snapshot?.documents?.mapNotNull { 
                    it.toObject(Transaction::class.java) 
                } ?: emptyList()
                trySend(transactions)
            }
        awaitClose { listener.remove() }
    }
    
    // ==================== NOTIFICATIONS ====================
    
    suspend fun createNotification(notification: BankNotification) {
        firestore.collection("notifications")
            .document(notification.id)
            .set(notification)
            .await()
    }
    
    fun getUserNotifications(userId: String): Flow<List<BankNotification>> = callbackFlow {
        val listener = firestore.collection("notifications")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(100)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val notifications = snapshot?.documents?.mapNotNull { 
                    it.toObject(BankNotification::class.java) 
                } ?: emptyList()
                trySend(notifications)
            }
        awaitClose { listener.remove() }
    }
    
    suspend fun markNotificationAsRead(notificationId: String) {
        firestore.collection("notifications")
            .document(notificationId)
            .update("isRead", true)
            .await()
    }
    
    suspend fun deleteNotification(notificationId: String) {
        firestore.collection("notifications")
            .document(notificationId)
            .delete()
            .await()
    }
    
    // ==================== CARDS ====================
    
    suspend fun createCard(card: Card) {
        firestore.collection("cards")
            .document(card.id)
            .set(card)
            .await()
    }
    
    fun getUserCards(userId: String): Flow<List<Card>> = callbackFlow {
        val listener = firestore.collection("cards")
            .whereEqualTo("userId", userId)
            .whereEqualTo("isActive", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val cards = snapshot?.documents?.mapNotNull { 
                    it.toObject(Card::class.java) 
                } ?: emptyList()
                trySend(cards)
            }
        awaitClose { listener.remove() }
    }
    
    suspend fun updateCard(cardId: String, updates: Map<String, Any>) {
        firestore.collection("cards")
            .document(cardId)
            .update(updates)
            .await()
    }
}

// Card data class
data class Card(
    val id: String = "",
    val userId: String = "",
    val accountId: String = "",
    val cardNumber: String = "", // Last 4 digits only
    val cardType: String = "DEBIT", // DEBIT or CREDIT
    val expiryDate: String = "",
    val cardHolderName: String = "",
    val isActive: Boolean = true,
    val dailyLimit: Double = 1000.0,
    val monthlyLimit: Double = 10000.0,
    val createdAt: Long = System.currentTimeMillis()
)
