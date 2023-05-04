package com.example.hospitalapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.hospitalapp.data.Client
import com.example.hospitalapp.repository.HospitalRepository
import java.util.UUID

class ClientViewModel : ViewModel() {
    fun newClient(writeID: UUID, client: Client) =
        HospitalRepository.get().newClient(writeID, client)

    fun deleteClient(writeID: UUID) =
        HospitalRepository.get().deleteClient(writeID)

    fun editClient(writeID: UUID, client: Client)=
        HospitalRepository.get().editClient(writeID, client)
}