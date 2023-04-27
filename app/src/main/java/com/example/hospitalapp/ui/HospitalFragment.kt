package com.example.hospitalapp.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalapp.R
import com.example.hospitalapp.data.Hospital
import com.example.hospitalapp.databinding.FragmentHospitalBinding
import com.example.hospitalapp.ui.viewmodels.HospitalViewModel
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.UUID

const val HOSPITAL_TAG = "HospitalFragment"
const val HOSPITAL_TITLE = "Hospital"

class HospitalFragment : Fragment() {

    private lateinit var viewModel: HospitalViewModel
    private var _binding : FragmentHospitalBinding?= null
    val binding
        get() = _binding!!

    private var adapter: HospitalListAdapter = HospitalListAdapter(emptyList())

    companion object {
        fun newInstance() = HospitalFragment()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHospitalBinding.inflate(inflater, container, false)
        binding.rvHospital.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HospitalViewModel::class.java)
        viewModel.hospitalList.observe(viewLifecycleOwner){
            adapter=HospitalListAdapter(it)
            binding.rvHospital.adapter = adapter
        }
        callbacks?.setTitle(HOSPITAL_TITLE)
    }

    private inner class HospitalHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
        lateinit var hospital: Hospital

        fun bind(hospital: Hospital){
            this.hospital = hospital
            itemView.findViewById<TextView>(R.id.tvHospitalName).text = hospital.name
        }

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick (v: View?){
            callbacks?.showDoctorFragment(hospital.id)
        }
    }

    private inner class HospitalListAdapter(private val items: List<Hospital>)
        : RecyclerView.Adapter<HospitalHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalHolder {
            val view = layoutInflater.inflate(R.layout.element_hospital_list, parent, false)
            return HospitalHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: HospitalHolder, position: Int) {
            holder.bind(items[position])
        }
    }
    interface Callbacks{
        fun setTitle(_title: String)
        fun showDoctorFragment(hospitalID : UUID)
    }

    var callbacks : Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }

}