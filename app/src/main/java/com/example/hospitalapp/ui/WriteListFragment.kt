package com.example.hospitalapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hospitalapp.data.Write
import com.example.hospitalapp.databinding.FragmentWriteListBinding
import com.example.hospitalapp.ui.viewmodels.WriteListViewModel
import java.util.UUID


const val WRITE_LIST_TAG = "WriteListFragment"
class WriteListFragment: Fragment() {
    private var _binding: FragmentWriteListBinding? = null
    private val binding get() = _binding!!

    companion object {
        private lateinit var writeID: UUID
        private lateinit var write: Write
        fun newInstance(writeID: UUID, write: Write?): WriteListFragment {
            this.writeID = writeID
            this.write = write!!
            return WriteListFragment()
        }

        val getWriteID
            get() = writeID
    }
    private lateinit var viewModel: WriteListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWriteListBinding.inflate(inflater, container, false)

        binding.apply {
            faAddClientBtn.setOnClickListener {
                callbacks?.showClient(writeID)
            }
        }
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.rvClients.layoutManager = LinearLayoutManager(context,
//            LinearLayoutManager.VERTICAL, false)
//        binding.rvClients.adapter = WriteListAdapter(emptyList())
//        viewModel = ViewModelProvider(this).get(WriteListViewModel::class.java)
//    }
//
//
//    private inner class WriteListHolder(view: View) : RecyclerView.ViewHolder(view),
//        View.OnClickListener {
//        lateinit var client: Client
//        fun bind(client: Client) {
//            this.client = client
//            val firstName = client.firstName
//            val reason = client.reason
//            itemView.findViewById<TextView>(R.id.tvFCS).text = firstName
//            itemView.findViewById<TextView>(R.id.tvReasonTitle).text = reason
//
//        }
//
//        init {
//            itemView.setOnClickListener(this)
//        }
//
//        override fun onClick(v: View?) {
//            //callbacks?.showClient(writeID, client)
//        }
//    }
//
//    private inner class WriteListAdapter(private val items: List<Client>) :
//        RecyclerView.Adapter<WriteListFragment.WriteListHolder>() {
//        override fun onCreateViewHolder(
//            parent: ViewGroup,
//            viewType: Int
//        ): WriteListFragment.WriteListHolder {
//            val view = layoutInflater.inflate(
//                R.layout.client_list_element, parent, false)
//            return WriteListHolder(view)
//        }
//
//        override fun getItemCount(): Int = items.size
//
//        override fun onBindViewHolder(holder: WriteListFragment.WriteListHolder, position: Int) {
//            holder.bind(items[position])
//        }
//    }
    interface Callbacks {
        fun showClient(writeID: UUID)
    }

    var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        callbacks = null
        super.onDetach()
    }

}