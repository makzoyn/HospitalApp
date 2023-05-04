package com.example.hospitalapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hospitalapp.data.Doctor
import com.example.hospitalapp.data.Hospital
import com.example.hospitalapp.repository.HospitalRepository
import java.util.UUID

class DoctorViewModel : ViewModel() {
    var hospital: MutableLiveData<Hospital> = MutableLiveData()
    private lateinit var _hospitalID : UUID

    fun setHospital(hospitalID : UUID){
        _hospitalID = hospitalID

        HospitalRepository.get().hospitalList.observeForever{
            hospital.postValue(it.find {it.id == _hospitalID})
        }
    }

    fun deleteDoctor(hospitalID: UUID, doctor: Doctor) =
        HospitalRepository.get().deleteDoctor(hospitalID, doctor)
}