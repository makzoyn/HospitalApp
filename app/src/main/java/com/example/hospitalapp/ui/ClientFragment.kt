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
import android.widget.TextView
import android.widget.Toast
import com.example.hospitalapp.R
import com.example.hospitalapp.data.Client
import com.example.hospitalapp.databinding.FragmentClientBinding
import com.example.hospitalapp.ui.viewmodels.ClientViewModel
import java.util.UUID

const val CLIENT_TAG = "ClientFragment"

class ClientFragment : Fragment() {
    private var _binding: FragmentClientBinding? = null
    private val binding get() = _binding!!

    companion object {
        private lateinit var writeID: UUID
        private var client: Client? = null
        fun newInstance(writeID: UUID, client: Client? = null): ClientFragment {
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
        viewModel = ViewModelProvider(this).get(ClientViewModel::class.java)
        if (client == null) {
            client = Client()
            binding.setClientBtn.setOnClickListener {
                if(binding.etFirstName.text.toString() == "") {
                    binding.etFirstName.error = "Введите имя"
                }
                else if(binding.etMiddleName.text.toString() == ""){
                    binding.etMiddleName.error = "Введите фамилию"
                }
                else if(binding.etLastName.text.toString() == ""){
                    binding.etLastName.error = "Введите отчество"
                }
                else if(binding.etReason.text.toString() == ""){
                    binding.etReason.error = "Введите причину обращения"
                }
                else {
                    client?.apply {
                        firstName = binding.etFirstName.text.toString()
                        middleName = binding.etMiddleName.text.toString()
                        lastName = binding.etLastName.text.toString()
                        reason = binding.etReason.text.toString()
                        haveACard = binding.cardCheck.isChecked
                    }
                    viewModel.newClient(writeID, client!!)
                    Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT)
                        .show()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        else if(client?.description==""){
            binding.apply {
                vsFirstName.showPrevious()
                vsMiddleName.showPrevious()
                vsLastName.showPrevious()
                vsReason.showPrevious()
                tvFirstName.text = client!!.firstName
                tvMiddleName.text = client!!.middleName
                tvLastName.text = client!!.lastName
                tvReason.text = client!!.reason
                cardCheck.isChecked = client!!.haveACard
                cardCheck.isClickable = false
                setClientBtn.visibility = View.GONE
                vsDescription.visibility = View.VISIBLE
                setDescriptionBtn.visibility = View.VISIBLE
                deleteClientBtn.visibility = View.VISIBLE
                setDescriptionBtn.setOnClickListener {
                    if(binding.etDescription.text.toString() != "") {
                        client?.description = binding.etDescription.text.toString()
                        Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                    else {
                        binding.etDescription.error = "Введите описание"
                    }
                }
                deleteClientBtn.setOnClickListener {
                    viewModel.deleteClient(writeID)
                    Toast.makeText(requireContext(), "Successfully deleted!", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        else{
            binding.apply {
                vsFirstName.showPrevious()
                vsMiddleName.showPrevious()
                vsLastName.showPrevious()
                vsReason.showPrevious()
                vsDescription.visibility = View.VISIBLE
                vsDescription.showPrevious()
                tvFirstName.text = client!!.firstName
                tvMiddleName.text = client!!.middleName
                tvLastName.text = client!!.lastName
                tvReason.text = client!!.reason
                tvDescription.text = client!!.description
                cardCheck.isChecked = client!!.haveACard
                cardCheck.isClickable = false
                setClientBtn.visibility = View.GONE
                deleteClientBtn.visibility = View.VISIBLE
                editClientBtn.visibility = View.VISIBLE
                deleteClientBtn.setOnClickListener {
                    viewModel.deleteClient(writeID)
                    Toast.makeText(requireContext(), "Successfully deleted!", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                editClientBtn.setOnClickListener {
                    vsFirstName.showNext()
                    vsMiddleName.showNext()
                    vsLastName.showNext()
                    vsReason.showNext()
                    vsDescription.visibility = View.VISIBLE
                    vsDescription.showNext()
                    etFirstName.setText(client!!.firstName)
                    etMiddleName.setText(client!!.middleName)
                    etLastName.setText(client!!.lastName)
                    etReason.setText(client!!.reason)
                    etDescription.setText(client!!.description)
                    cardCheck.isChecked = client!!.haveACard
                    cardCheck.isClickable = true
                    setClientBtn.visibility = View.VISIBLE
                    deleteClientBtn.visibility = View.GONE
                    editClientBtn.visibility = View.GONE
                }
                binding.setClientBtn.setOnClickListener {
                    if(binding.etFirstName.text.toString() == "") {
                        binding.etFirstName.error = "Введите имя"
                    }
                    else if(binding.etMiddleName.text.toString() == ""){
                        binding.etMiddleName.error = "Введите фамилию"
                    }
                    else if(binding.etLastName.text.toString() == ""){
                        binding.etLastName.error = "Введите отчество"
                    }
                    else if(binding.etReason.text.toString() == ""){
                        binding.etReason.error = "Введите причину обращения"
                    }
                    else {
                        client?.apply {
                            firstName = binding.etFirstName.text.toString()
                            middleName = binding.etMiddleName.text.toString()
                            lastName = binding.etLastName.text.toString()
                            reason = binding.etReason.text.toString()
                            haveACard = binding.cardCheck.isChecked
                            description = binding.etDescription.text.toString()
                        }
                        viewModel.newClient(writeID, client!!)
                        Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT)
                            .show()
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        }
    }
}