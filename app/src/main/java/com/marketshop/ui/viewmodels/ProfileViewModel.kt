package com.marketshop.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.marketshop.data.UserPreferences
import com.marketshop.data.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val isDarkMode: Boolean = false,
    val isEditing: Boolean = false
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = UserPreferences(application)
    private val repository = ShopRepository(application)
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    init {
        _state.value = _state.value.copy(
            name = prefs.getUserName(),
            email = prefs.getUserEmail(),
            phone = prefs.getUserPhone(),
            isDarkMode = prefs.isDarkMode()
        )
    }

    fun updateName(name: String) { _state.value = _state.value.copy(name = name) }
    fun updateEmail(email: String) { _state.value = _state.value.copy(email = email) }
    fun updatePhone(phone: String) { _state.value = _state.value.copy(phone = phone) }

    fun toggleEditing() {
        if (_state.value.isEditing) saveProfile()
        _state.value = _state.value.copy(isEditing = !_state.value.isEditing)
    }

    private fun saveProfile() {
        prefs.saveUser(_state.value.name, _state.value.email, _state.value.phone)
    }

    fun toggleDarkMode() {
        val newMode = !_state.value.isDarkMode
        _state.value = _state.value.copy(isDarkMode = newMode)
        prefs.setDarkMode(newMode)
    }

    fun clearUserData(onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.clearCart()
            prefs.clearAllData()
            _state.value = ProfileState()
            onComplete()
        }
    }
}
