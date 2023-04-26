package com.example.hospitalapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.hospitalapp.data.Write
import com.example.hospitalapp.repository.HospitalRepository
import java.util.UUID

class WriteViewModel : ViewModel() {
    fun newWrite(doctorID: UUID, write: Write) =
        HospitalRepository.get().newWrite(doctorID, write)
}