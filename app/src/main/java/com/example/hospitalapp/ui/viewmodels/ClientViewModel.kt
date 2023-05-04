package com.example.hospitalapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.hospitalapp.data.Client
import com.example.hospitalapp.repository.HospitalRepository
import java.util.UUID

class ClientViewModel : ViewModel() {
    fun newClient(writeID: UUID, client: Client) =
        HospitalRepository.get().newClient(writeID, client)
}