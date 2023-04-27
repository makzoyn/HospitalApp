package com.example.hospitalapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.hospitalapp.data.Client
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
    fun newClient(writeID: UUID, client: Client) {
        val h = hospitalList.value ?: return
        val hospital = h.find { it.doctors?.find { it.writes?.find { it.id == writeID } !=null } !=null } ?: return
        val doctor = hospital.doctors?.find { it.writes?.find { it.id == writeID } !=null } ?: return
        val write = doctor.writes?.find { it.id == writeID }  ?: return

        val list: ArrayList<Client> = if ((write.clients?.isEmpty() ?: true) == true)
            ArrayList()
        else {
            write.clients as ArrayList<Client>
        }
        list.add(client)
        write.clients = list
        hospitalList.postValue(h)
    }


    fun deleteWrite(doctorID: UUID, write: Write){
        val h = hospitalList.value ?: return
        val hospital = h.find { it.doctors?.find { it.id == doctorID } != null } ?: return
        val doctor = hospital.doctors?.find { it.id == doctorID }
        if (doctor!!.writes?.isEmpty() == true) return
        val list = doctor.writes as ArrayList<Write>
        list.remove(write)
        doctor.writes = list
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



}