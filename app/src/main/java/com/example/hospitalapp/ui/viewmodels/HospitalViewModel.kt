package com.example.hospitalapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hospitalapp.data.Hospital
import com.example.hospitalapp.repository.HospitalRepository

class HospitalViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var hospitalList : MutableLiveData<List<Hospital>> = MutableLiveData()

    init {
        HospitalRepository.get().hospitalList.observeForever{
            hospitalList.postValue(it)
        }
    }
    fun newHospital(name: String) =
        HospitalRepository.get().newHospital(name)
}