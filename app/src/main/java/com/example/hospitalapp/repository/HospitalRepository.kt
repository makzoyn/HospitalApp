package com.example.hospitalapp.repository


import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.lifecycle.MutableLiveData
import com.example.hospitalapp.HospitalAppApplication
import com.example.hospitalapp.data.Client
import com.example.hospitalapp.data.Doctor
import com.example.hospitalapp.data.Hospital
import com.example.hospitalapp.data.Write
import com.google.gson.Gson
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
        val list: MutableList<Hospital> = hospitalList.value?.toMutableList() ?: mutableListOf()
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

    fun newClient(writeID: UUID, client: Client) {
        val h = hospitalList.value ?: return
        val hospital =
            h.find { it.doctors?.find { it.writes?.find { it.id == writeID } != null } != null }
                ?: return
        val doctor =
            hospital.doctors?.find { it.writes?.find { it.id == writeID } != null } ?: return
        val write = doctor.writes?.find { it.id == writeID } ?: return
        write.client = client
        hospitalList.postValue(h)
    }

    fun deleteDoctor(hospitalID: UUID, doctor: Doctor) {
        val h = hospitalList.value ?: return
        val hospital = h.find { it.id == hospitalID }
        if(hospital?.doctors?.isEmpty()==true) return
        val list = hospital?.doctors as ArrayList<Doctor>
        list.remove(doctor)
        hospital.doctors = list
        hospitalList.postValue(h)
    }
    fun deleteWrite(doctorID: UUID, write: Write) {
        val h = hospitalList.value ?: return
        val hospital = h.find { it.doctors?.find { it.id == doctorID } != null } ?: return
        val doctor = hospital.doctors?.find { it.id == doctorID }
        if (doctor!!.writes?.isEmpty() == true) return
        val list = doctor.writes as ArrayList<Write>
        list.remove(write)
        doctor.writes = list
        hospitalList.postValue(h)
    }

    fun deleteClient(writeID : UUID) {
        val h = hospitalList.value ?: return
        val hospital = h.find { it.doctors?.find { it.writes?.find { it.id == writeID } != null } != null } ?: return
        val doctor = hospital.doctors?.find { it.writes?.find { it.id == writeID } != null} ?: return
        val write = doctor.writes?.find { it.id == writeID }
        if(write!!.client == null) return
        write.client = null
        hospitalList.postValue(h)
    }

    fun deleteHospital(hospitalID: UUID) {
        val h =  hospitalList.value ?: return
        val hospital = h.find { it.id == hospitalID } ?: return
        val list = h as ArrayList<Hospital>
        list.remove(hospital)
        hospitalList.value = list
        hospitalList.postValue(h)
    }

    fun editHospital(hospitalID: UUID, hospital: Hospital) {
        val h = hospitalList.value ?: return
        val _hospital = h.find { it.id == hospitalID } ?: return
        val list = h as ArrayList<Hospital>
        val i = list.indexOf(_hospital)
        list.remove(_hospital)
        list.add(i, hospital)
    }
    fun editDoctor(doctorID: UUID, doctor: Doctor) {
        val h = hospitalList.value ?: return
        val hospital = h.find { it.doctors?.find { it.id == doctorID } !=null } ?: return
        val _doctor = hospital.doctors?.find{ it.id == doctorID}
        val list = hospital.doctors as ArrayList<Doctor>
        val i = list.indexOf(_doctor)
        list.remove(_doctor)
        list.add(i, doctor)
    }

    fun editClient(writeID: UUID, client: Client){
        val h = hospitalList.value ?: return
        val hospital = h.find { it.doctors?.find { it.writes?.find { it.id == writeID } != null } != null } ?: return
        val doctor = hospital.doctors?.find { it.writes?.find { it.id == writeID } != null} ?: return
        val write = doctor.writes?.find { it.id == writeID }
        write?.client = client
        hospitalList.postValue(h)
    }

    fun editWrite(doctorID: UUID, write: Write) {
        val h = hospitalList.value ?: return
        val hospital = h.find { it.doctors?.find { it.id == doctorID } != null } ?: return
        val doctor = hospital.doctors?.find { it.id == doctorID } ?: return
        val _write = doctor.writes?.find { it.id == write.id }
        if (_write == null) {
            newWrite(doctorID, write)
            return
        }
        val list = doctor.writes as ArrayList<Write>
        val i = list.indexOf(_write)
        list.remove(_write)
        list.add(i, write)
        doctor.writes = list
        hospitalList.postValue(h)

    }

    fun saveHospital() {
        if (hospitalList.value == null) return
        val h = hospitalList.value!!
        val s = Gson().toJson(h)
        val context = HospitalAppApplication.applicationContext()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit {
            putString("hospital", s)
            apply()
        }
    }


    fun loadHospital() {
        val context = HospitalAppApplication.applicationContext()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val s = sharedPreferences.getString("hospital", null)
        if (s.isNullOrBlank()) return
        val h = Gson().fromJson(s, Array<Hospital>::class.java).toList()
        hospitalList.postValue(h)
    }


}

