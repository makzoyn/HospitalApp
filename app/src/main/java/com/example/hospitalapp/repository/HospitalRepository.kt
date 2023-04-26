package com.example.hospitalapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.hospitalapp.data.Doctor
import com.example.hospitalapp.data.Hospital
import com.example.hospitalapp.data.Write
import java.util.UUID
import kotlin.collections.ArrayList

class HospitalRepository private constructor() {
    var hospitalList: MutableLiveData<List<Hospital>> = MutableLiveData()

    companion object {
        private var INSTANCE: HospitalRepository? = null
        fun newInstance() {
            if (INSTANCE == null) {
                INSTANCE = HospitalRepository()
            }
        }
        fun get(): HospitalRepository =
            INSTANCE ?: throw IllegalStateException("Репозиторий не инициализирован")
    }

    fun newHospital(name: String) {
        val hospital = Hospital(name = name)
        val list: MutableList<Hospital>
        if (hospitalList.value != null) {
            list = (hospitalList.value as ArrayList<Hospital>)
        } else {
            list = ArrayList<Hospital>()
        }
        list.add(hospital)
        hospitalList.postValue(list)
    }

    fun newDoctor(hospitalID: UUID, name: String) {
        if (hospitalList.value == null) return
        val h = hospitalList.value!!
        val hospital = h.find { it.id == hospitalID }
        if (hospital == null) return
        val doctor = Doctor(name = name)
        val list: ArrayList<Doctor>
        if (hospital.doctors != null) {
            list = (hospital.doctors as ArrayList<Doctor>)
        } else
            list = ArrayList<Doctor>()
        list.add(doctor)
        hospital.doctors = list
        hospitalList.postValue(h)
    }

    fun newWrite(doctorID: UUID, write: Write) {
        val h = hospitalList.value ?: return
        val hospital = h.find { it.doctors?.find { it.id == doctorID } != null } ?: return
        val doctor = hospital.doctors?.find { it.id == doctorID } ?: return
        val list: ArrayList<Write> = if ((doctor.writes?.isEmpty() ?: true) == true)
            ArrayList()
        else {
            doctor.writes as ArrayList<Write>
        }
        list.add(write)
        doctor.writes = list
        hospitalList.postValue(h)
    }


}