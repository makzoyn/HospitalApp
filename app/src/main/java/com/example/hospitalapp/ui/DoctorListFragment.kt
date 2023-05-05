package com.example.hospitalapp.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hospitalapp.R
import com.example.hospitalapp.data.Client
import com.example.hospitalapp.data.Doctor
import com.example.hospitalapp.data.Write
import com.example.hospitalapp.databinding.FragmentDoctorListBinding
import com.example.hospitalapp.ui.viewmodels.DoctorListViewModel
import java.util.Calendar
import java.util.GregorianCalendar
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
        View.OnClickListener, View.OnLongClickListener {
        lateinit var write: Write

        @SuppressLint("NotifyDataSetChanged")
        fun bind(write: Write) {
            this.write = write
            val time = write.time
            val date = write.date
            itemView.findViewById<ConstraintLayout>(R.id.csButtons).visibility=View.GONE
            itemView.findViewById<TextView>(R.id.tvElementTime).text = time
            itemView.findViewById<TextView>(R.id.tvElementDate).text = date
            if(!write.enable) {
                itemView.setOnClickListener(null)
                itemView.setBackgroundColor(Color.LTGRAY)
                itemView.findViewById<ImageButton>(R.id.deleteWriteBtn).setBackgroundColor(Color.LTGRAY)
                itemView.findViewById<ImageButton>(R.id.editWriteBtn).setBackgroundColor(Color.LTGRAY)
            }
            else if(write.client!=null){
                itemView.setBackgroundColor(Color.CYAN)
                itemView.findViewById<ImageButton>(R.id.deleteWriteBtn).setBackgroundColor(Color.CYAN)
                itemView.findViewById<ImageButton>(R.id.editWriteBtn).setBackgroundColor(Color.CYAN)
            }
            itemView.findViewById<ImageButton>(R.id.deleteWriteBtn).setOnClickListener {
                showDeleteDialog(write)
            }
            itemView.findViewById<ImageButton>(R.id.editWriteBtn).setOnClickListener {
                callbacks?.showWrite(doctor.id, write)
            }
            val originalList = doctor.writes
            binding.chooseDateBtn.setOnClickListener {
                val year = (binding.dtpCalendar.year).toString()
                val month = ((binding.dtpCalendar.month)+1).toString()
                val day = binding.dtpCalendar.dayOfMonth.toString()
                val chooseDate = "$day.$month.$year"
                val refreshedList = refreshList(originalList, chooseDate)
                binding.rvDoctorList.adapter = DoctorListAdapter(refreshedList!!)
            }

        }
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }
        override fun onClick(v: View?) {
            callbacks?.showClientFragment(write.id, write.client)
        }

        override fun onLongClick(v: View?): Boolean {
            val cl = itemView.findViewById<ConstraintLayout>(R.id.csButtons)
            cl.visibility = View.VISIBLE
            lastItemView?.findViewById<ConstraintLayout>(R.id.csButtons)?.visibility=View.GONE
            lastItemView = if(lastItemView == itemView) null else itemView
            return true
        }


    }
    private fun showDeleteDialog(write: Write){
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setMessage("Удалить прием на Дату: ${write.date} \n в ${write.time} часов из списка?")
        builder.setPositiveButton("Подтверждаю") {_, _ ->
            viewModel.deleteWrite(doctor.id,write)
        }
        builder.setNegativeButton("Отмена", null)
        val alert = builder.create()
        alert.show()
    }
    private fun refreshList(
        originalList: MutableList<Write>?,
        chooseDate: String
    ): MutableList<Write>?{
        val refreshedList = originalList?.filter {
            it.date == chooseDate
        }
        return refreshedList as MutableList<Write>?
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
        fun showWrite(doctorID: UUID, _write: Write?)
        fun showClientFragment(writeID: UUID, _client: Client?)
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