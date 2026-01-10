package com.example.mybank.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.data.model.BankNotification
import com.example.mybank.data.preferences.PreferencesManager
import com.example.mybank.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsUiState(
    val notifications: List<BankNotification> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()
    
    init {
        loadNotifications()
    }
    
    private fun loadNotifications() {
        viewModelScope.launch {
            preferencesManager.userId.collect { userId ->
                if (userId != null) {
                    notificationRepository.getUserNotifications(userId).collect { notifications ->
                        _uiState.value = _uiState.value.copy(notifications = notifications)
                    }
                }
            }
        }
        
        viewModelScope.launch {
            preferencesManager.userId.collect { userId ->
                if (userId != null) {
                    notificationRepository.getUnreadNotificationCount(userId).collect { count ->
                        _uiState.value = _uiState.value.copy(unreadCount = count)
                    }
                }
            }
        }
    }
    
    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            notificationRepository.markAsRead(notificationId)
        }
    }
    
    fun markAllAsRead() {
        viewModelScope.launch {
            preferencesManager.userId.collect { userId ->
                if (userId != null) {
                    notificationRepository.markAllAsRead(userId)
                }
            }
        }
    }
    
    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            notificationRepository.syncNotifications()
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}
