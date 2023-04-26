package com.example.hospitalapp.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalapp.R
import com.example.hospitalapp.data.Doctor
import com.example.hospitalapp.data.Write
import com.example.hospitalapp.databinding.FragmentDoctorListBinding
import com.example.hospitalapp.ui.viewmodels.DoctorListViewModel
import java.util.UUID

class DoctorListFragment(private val doctor: Doctor) : Fragment() {

    private var _binding: FragmentDoctorListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DoctorListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoctorListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvDoctorList.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)
        binding.rvDoctorList.adapter = DoctorListAdapter(doctor.writes?: emptyList())
        viewModel = ViewModelProvider(this).get(DoctorListViewModel::class.java)
    }

    private var lastItemView : View? = null

    private inner class DoctorHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        lateinit var write: Write

        fun bind(write: Write) {
            this.write = write
            val time = write.time
            val date = write.date
            val enable = write.enable
            itemView.findViewById<TextView>(R.id.tvElementTime).text = time
            itemView.findViewById<TextView>(R.id.tvElementDate).text = date
            if(!write.enable) {
                itemView.setOnClickListener(null)
                itemView.setBackgroundColor(Color.LTGRAY)
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }

    }
    private inner class DoctorListAdapter(private val items: List<Write>) :
        RecyclerView.Adapter<DoctorHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): DoctorHolder {
            val view = layoutInflater.inflate(
                R.layout.element_doctorlist_list, parent, false)
            return DoctorHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: DoctorHolder, position: Int) {
            holder.bind(items[position])
        }
    }
    interface Callbacks{
        fun showWrite(hospitalID: UUID, _write: Write?)
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