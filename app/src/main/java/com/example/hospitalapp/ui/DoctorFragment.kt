package com.example.hospitalapp.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hospitalapp.R
import com.example.hospitalapp.data.Doctor
import com.example.hospitalapp.data.Hospital
import com.example.hospitalapp.data.Write
import com.example.hospitalapp.databinding.FragmentDoctorBinding
import com.example.hospitalapp.ui.viewmodels.DoctorViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.UUID

const val DOCTOR_TAG = "DoctorFragment"

class DoctorFragment : Fragment() {
    private var _binding: FragmentDoctorBinding? = null
    private val binding get() = _binding!!

    companion object {
        private lateinit var hospitalID: UUID

        fun newInstance(hospitalID: UUID): DoctorFragment {
            this.hospitalID = hospitalID
            return DoctorFragment()
        }

        val getHospitalID
            get() = hospitalID
    }

    private lateinit var viewModel: DoctorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoctorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DoctorViewModel::class.java)
        viewModel.hospital.observe(viewLifecycleOwner) {
            updateUI(it)
            callbacks?.setTitle(it?.name ?: "")
        }
        viewModel.setHospital(hospitalID)
    }

    var tabPosition = 0

    private fun updateUI(hospital: Hospital) {

        binding.tabDoctor.clearOnTabSelectedListeners()
        binding.tabDoctor.removeAllTabs()

        for (i in 0 until (hospital?.doctors?.size ?: 0)) {
            binding.tabDoctor.addTab(binding.tabDoctor.newTab().apply {
                text = i.toString()
            })
        }

        val adapter = DoctorPageAdapter(requireActivity(), hospital!!)
        binding.vpDoctor.adapter = adapter
        TabLayoutMediator(binding.tabDoctor, binding.vpDoctor, true, true) { tab, pos ->
            tab.text = hospital?.doctors?.get(pos)?.name
        }.attach()


        binding.tabDoctor.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabPosition = tab?.position!!
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tabPosition = tab?.position!!
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tabPosition = tab?.position!!
            }

        })
        binding.faAddWriteBtn.visibility =
            if ((hospital?.doctors?.size ?: 0) == 0)
                View.GONE
            else {
                binding.faAddWriteBtn.setOnClickListener {
                    callbacks?.showWrite(hospital?.doctors!!.get(tabPosition).id)
                }
                View.VISIBLE
            }
        val tabLayout = binding.tabDoctor
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            val tabView = tab?.view
            tabView?.setOnLongClickListener {
                val doctor = hospital.doctors?.get(tab.position)
                if (doctor != null) {
                    showTabOptionsDialog(doctor)
                }
                true
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun showEditDialog(doctorID: UUID, doctor: Doctor){
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.name_input, null)
        builder.setView(dialogView)
        val nameInput = dialogView.findViewById(R.id.editName) as EditText
        val tvInfo = dialogView.findViewById(R.id.tvInfo) as TextView
        builder.setTitle("Укажите значение")
        nameInput.setText(doctor.name)
        tvInfo.text = getString(R.string.input_doctor)
        builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
            doctor.apply { name = nameInput.text.toString() }
            viewModel.editDoctor(doctorID, doctor)
            binding.vpDoctor.adapter?.notifyDataSetChanged()
        }
        builder.setNegativeButton(getString(R.string.cancel), null)
        val alert = builder.create()
        alert.show()
    }
    private fun showDeleteDialog(hospitalID: UUID, doctor: Doctor){
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setMessage("Вы хотите удалить специальность?")
        builder.setPositiveButton("Подтверждаю") {_, _ ->
            viewModel.deleteDoctor(hospitalID, doctor)
        }
        builder.setNegativeButton("Отмена", null)
        val alert = builder.create()
        alert.show()
    }


   private fun showTabOptionsDialog(doctor: Doctor) {
        val options = arrayOf("Изменить", "Удалить")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Выберите опцию")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    showEditDialog(doctor.id, doctor)
                }
                1 -> {
                    showDeleteDialog(hospitalID, doctor)
                }
            }
        }
        val dialog = builder.create()
        dialog.show()
    }


    private inner class DoctorPageAdapter(fa: FragmentActivity, private val hospital: Hospital) :
        FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return (hospital.doctors?.size ?: 0)
        }

        override fun createFragment(position: Int): Fragment {
            return DoctorListFragment(hospital.doctors?.get(position)!!)
        }
    }

    interface Callbacks {
        fun setTitle(_title: String)
        fun showWrite(doctorID: UUID, _write: Write? = null)
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }
}