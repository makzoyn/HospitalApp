package com.example.hospitalapp.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.hospitalapp.R
import com.example.hospitalapp.data.Write
import com.example.hospitalapp.databinding.FragmentWriteBinding
import com.example.hospitalapp.ui.viewmodels.WriteViewModel
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.UUID

const val WRITE_TAG = "WriteFragment"

class WriteFragment private constructor() : Fragment() {

    private var _binding: FragmentWriteBinding? = null
    private val binding get() = _binding!!

    companion object {
        private var write: Write? = null
        private lateinit var doctorID: UUID
        fun newInstance(doctorID: UUID, write: Write? = null): WriteFragment {
            this.write = write
            this.doctorID = doctorID
            return WriteFragment()
        }
    }

    private lateinit var viewModel: WriteViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWriteBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fun changeTimeByHalfHour(timeString: String): String {
            val dtf = DateTimeFormatter.ofPattern("HH:mm")
            val time = LocalTime.parse(timeString, dtf)
            val newTime = time.plusMinutes(30)
            val newTimeString = newTime.format(dtf)
            return newTimeString
        }
        binding.apply {
            setTimeBtn.setOnClickListener {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { /*timePicker: TimePicker?*/_, hour1: Int, minute1: Int ->
                        val timeString = StringBuilder()
                        if (hour1 < 10) {
                            timeString.append("0")
                        }
                        timeString.append(hour1)
                        timeString.append(":")
                        if (minute1 < 10) {
                            timeString.append("0")
                        }
                        timeString.append(minute1)
                        tvTime.text = timeString.toString()
                    },
                    hour,
                    minute,
                    false
                )
                timePickerDialog.show()
            }
            setDateBtn.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { /*datePicker: DatePicker?*/_, year1: Int, month1: Int, day1: Int ->
                        val monthBuff = month1 + 1
                        tvDate.text = "$day1.$monthBuff.$year1"
                    },
                    year,
                    month,
                    day
                )
                datePickerDialog.show()
            }
        }
        if (write != null) {
            binding.apply {
                tvTime.text = write!!.time
                tvDate.text = write!!.date
                enabledCheck.isChecked = write!!.enable
            }
        }
        viewModel = ViewModelProvider(this).get(WriteViewModel::class.java)
        binding.btnSetWrite.setOnClickListener {
            val times = mutableListOf<String>()
            var timeElement = binding.tvTime.text.toString()
            for (i in 0..10) {
                times.add(timeElement)
                timeElement = changeTimeByHalfHour(timeElement)
            }
            if (TextUtils.isEmpty(binding.tvDate.text)) {
                Toast.makeText(requireContext(), "Date empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                if (write == null) {
                    for (timeElement in times) {
                        write = Write()
                        write?.apply {
                            time = timeElement
                            date = binding.tvDate.text.toString()
                            enable = binding.enabledCheck.isChecked
                        }
                        viewModel.newWrite(doctorID!!, write!!)
                    }

                } else {
                    write?.apply {
                        time = binding.tvTime.text.toString()
                        date = binding.tvDate.text.toString()
                        enable = binding.enabledCheck.isChecked
                    }
                    viewModel.editWrite(doctorID!!, write!!)
                }
                Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

        }


    }
}