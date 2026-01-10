package com.example.mybank.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.data.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isDarkTheme: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val biometricEnabled: Boolean = false,
    val userName: String = "",
    val userEmail: String = ""
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            preferencesManager.isDarkTheme.collect { isDark ->
                _uiState.value = _uiState.value.copy(isDarkTheme = isDark)
            }
        }
        
        viewModelScope.launch {
            preferencesManager.notificationsEnabled.collect { enabled ->
                _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesManager.biometricEnabled.collect { enabled ->
                _uiState.value = _uiState.value.copy(biometricEnabled = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesManager.userName.collect { name ->
                _uiState.value = _uiState.value.copy(userName = name ?: "")
            }
        }
        
        viewModelScope.launch {
            preferencesManager.userEmail.collect { email ->
                _uiState.value = _uiState.value.copy(userEmail = email ?: "")
            }
        }
    }
    
    fun toggleDarkTheme() {
        viewModelScope.launch {
            preferencesManager.setDarkTheme(!_uiState.value.isDarkTheme)
        }
    }
    
    fun toggleNotifications() {
        viewModelScope.launch {
            preferencesManager.setNotificationsEnabled(!_uiState.value.notificationsEnabled)
        }
    }
    
    fun toggleBiometric() {
        viewModelScope.launch {
            preferencesManager.setBiometricEnabled(!_uiState.value.biometricEnabled)
        }
    }
}
