package com.example.hospitalapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.example.hospitalapp.data.Write
import com.example.hospitalapp.repository.HospitalRepository
import com.example.hospitalapp.ui.DOCTOR_TAG
import com.example.hospitalapp.ui.DoctorFragment
import com.example.hospitalapp.ui.HOSPITAL_TAG
import com.example.hospitalapp.ui.HospitalFragment
import java.util.UUID

class MainActivity : AppCompatActivity(),
    HospitalFragment.Callbacks,
    DoctorFragment.Callbacks{
    private var myNewFaculty: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragment, HospitalFragment.newInstance(), HOSPITAL_TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        myNewFaculty = menu?.findItem(R.id.myNewHospital)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.myNewHospital -> {
                /*val myHospitalFragment = supportFragmentManager.findFragmentByTag(HOSPITAL_TAG)
                val myDoctorFragment = supportFragmentManager.findFragmentByTag(DOCTOR_TAG)
                if(myHospitalFragment != null) {
                    showNameInputDialog(0);
                }
                else if(myDoctorFragment != null)
                    showNameInputDialog(1)
                else {
                    showTimeInputDialog()
                }*/
                val myFragment = supportFragmentManager.findFragmentByTag(DOCTOR_TAG)
                if(myFragment == null ){
                    showNameInputDialog(0);
                }
                else {
                    showNameInputDialog(1)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showNameInputDialog(index: Int = -1) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.name_input, null)
        builder.setView(dialogView)
        val nameInput = dialogView.findViewById(R.id.editName) as EditText
        val tvInfo = dialogView.findViewById(R.id.tvInfo) as TextView
        builder.setTitle("Укажите значение")
        when (index) {
            0 -> {
                tvInfo.text = getString(R.string.input_hospital)
                builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
                    val s = nameInput.text.toString()
                    if (s.isNotBlank()) {
                        HospitalRepository.get().newHospital(s)
                    }
                }
            }
            1 -> {
                tvInfo.text = getString(R.string.input_doctor)
                builder.setPositiveButton(getString(R.string.commit)) { _, _ ->
                    val s = nameInput.text.toString()
                    if (s.isNotBlank()) {
                        HospitalRepository.get().newDoctor(DoctorFragment.getHospitalID, s)
                    }
                }
            }
        }
        builder.setNegativeButton(getString(R.string.cancel), null)
        val alert = builder.create()
        alert.show()
    }

//    private fun showTimeInputDialog() {
//        val builder = AlertDialog.Builder(this)
//        builder.setCancelable(true)
//        val dialogView = LayoutInflater.from(this).inflate(R.layout.time_input, null)
//        builder.setView(dialogView)
//        val time = dialogView.findViewById(R.id.tpTime) as TimePicker
//        builder.setTitle("Укажите время")
//        builder.setNegativeButton(getString(R.string.cancel), null)
//        val alert = builder.create()
//        alert.show()
//    }

    override fun setTitle(_title: String) {
        title = _title
    }


    override fun showDoctorFragment(hospitalID: UUID) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragment, DoctorFragment.newInstance(hospitalID), DOCTOR_TAG)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }


}