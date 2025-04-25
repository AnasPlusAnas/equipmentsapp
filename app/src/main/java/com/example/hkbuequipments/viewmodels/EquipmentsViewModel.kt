package com.example.hkbuequipments.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hkbuequipments.KtorClient
import com.example.hkbuequipments.models.Equipment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EquipmentsViewModel : ViewModel() {
    private val _currentPage = MutableStateFlow<Int>(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _equipmentList = MutableStateFlow<List<Equipment>>(emptyList())
    val equipmentList: StateFlow<List<Equipment>> = _equipmentList

    private val _equipmentDetail = MutableStateFlow<Equipment>(Equipment())
    val equipmentDetail: StateFlow<Equipment> = _equipmentDetail

    private val _reserveResult = MutableStateFlow<Boolean?>(null)
    val reserveResult: StateFlow<Boolean?> = _reserveResult

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun nextPage(
        keyword: String = "",
        location: String = "",
        highlighted: String = "",
        perPage: String = ""
    ) {
        _currentPage.value += 1
        loadEquipments(
            keyword = keyword,
            location = location,
            highlighted = highlighted,
            page = _currentPage.value,
            perPage = perPage
        )
    }

    fun prevPage(
        keyword: String = "",
        location: String = "",
        highlighted: String = "",
        perPage: String = ""
    ) {
        if (_currentPage.value == 1) return
        _currentPage.value -= 1
        loadEquipments(
            keyword = keyword,
            location = location,
            highlighted = highlighted,
            page = _currentPage.value,
            perPage = perPage
        )
    }

    fun loadEquipments(
        keyword: String = "",
        location: String = "",
        highlighted: String = "",
        page: Int = _currentPage.value,
        perPage: String = ""
    ) {

        if (_isLoading.value || page < 1) return

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val equipments = KtorClient.getEquipments(
                    keyword = keyword,
                    location = location,
                    highlighted = highlighted,
                    page = page,
                    perPage = perPage
                )
                _equipmentList.value = equipments.equipments
                _currentPage.value = equipments.page
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("VM:", "Failed to load equipments: ${e.message}")
            }
        }
    }

    fun loadEquipmentById(
        id: String
    ) {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val equipment = KtorClient.getEquipmentById(id)
                _equipmentDetail.value = equipment
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("VM:", "Failed to load equipment by ID: ${e.localizedMessage}")
            }
        }
    }

    fun reserveEquipment(id: String) {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = KtorClient.reserveEquipment(id)
                _reserveResult.value = response
            } catch (e: Exception) {
                Log.e("VM:", "Failed to reserve equipment: ${e.message}")
                _errorMessage.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun unreserveEquipment(id: String) {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = KtorClient.unreserveEquipment(id)
                _reserveResult.value = response
            } catch (e: Exception) {
                Log.e("VM:", "Failed to unreserve equipment: ${e.message}")
                _errorMessage.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun getUserEquipments(id: String = "any") {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val equipments = KtorClient.getUserById(id)
                _equipmentList.value = equipments
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("VM:", "Failed to load user equipments: ${e.message}")
            }
        }
    }

    fun resetReserveResult() {
        _reserveResult.value = null
    }
}