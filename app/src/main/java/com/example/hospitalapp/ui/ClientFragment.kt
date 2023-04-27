package com.example.hospitalapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.hospitalapp.R
import com.example.hospitalapp.data.Client
import com.example.hospitalapp.data.Write
import com.example.hospitalapp.databinding.FragmentClientBinding
import com.example.hospitalapp.databinding.FragmentWriteBinding
import com.example.hospitalapp.repository.HospitalRepository
import com.example.hospitalapp.ui.viewmodels.ClientViewModel
import com.example.hospitalapp.ui.viewmodels.WriteViewModel
import java.util.Calendar
import java.util.UUID

const val CLIENT_TAG = "ClientFragment"
class ClientFragment : Fragment() {
    private var _binding: FragmentClientBinding? = null
    private val binding get() = _binding!!
    companion object {
        private lateinit var writeID: UUID
        private lateinit var client: Client
        fun newInstance(writeID: UUID, client: Client): ClientFragment {
            this.writeID = writeID
            this.client = client
            return ClientFragment()
        }
    }

    private lateinit var viewModel: ClientViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setClientBtn.setOnClickListener{
            if(client == null) {
                client = Client()
                client?.apply{
                    firstName = binding.etFirstName.text.toString()
                    middleName = binding.etMiddleName.text.toString()
                    lastName = binding.etLastName.text.toString()
                    reason = binding.etReason.text.toString()
                    haveACard = binding.cardCheck.isChecked
                }
                viewModel.newClient(writeID, client)
            }
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }


}