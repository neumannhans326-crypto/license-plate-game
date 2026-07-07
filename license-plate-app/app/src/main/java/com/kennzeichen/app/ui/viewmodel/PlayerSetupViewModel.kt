package com.kennzeichen.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerSetupViewModel : ViewModel() {

    private val _playerNames = MutableStateFlow<List<String>>(listOf("", ""))
    val playerNames = _playerNames.asStateFlow()

    fun addPlayer() {
        if (_playerNames.value.size < 6) {
            _playerNames.value = _playerNames.value + ""
        }
    }

    fun removePlayer(index: Int) {
        if (_playerNames.value.size > 1) {
            val newList = _playerNames.value.toMutableList()
            newList.removeAt(index)
            _playerNames.value = newList
        }
    }

    fun updatePlayerName(index: Int, name: String) {
        val newList = _playerNames.value.toMutableList()
        if (index < newList.size) {
            newList[index] = name
            _playerNames.value = newList
        }
    }
}