package com.example.hospitalapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.hospitalapp.data.Write
import com.example.hospitalapp.repository.HospitalRepository
import java.util.UUID

class DoctorListViewModel : ViewModel() {
    fun deleteWrite(doctorID: UUID, write: Write) =
        HospitalRepository.get().deleteWrite(doctorID, write)
}