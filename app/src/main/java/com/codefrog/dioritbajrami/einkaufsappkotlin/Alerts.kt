package com.codefrog.dioritbajrami.einkaufsappkotlin

import android.app.Dialog
import android.content.Context
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.add_alert_dialog.*

class Alerts{

    var context: Context?=null
    var mAuth = FirebaseAuth.getInstance()

    var database = FirebaseDatabase.getInstance()
    var firRef = database.reference
    var empfehlungLista = ArrayList<String>()

    var fireClient = FirebaseClient()


    constructor(context: Context){
        this.context = context
    }


    fun startAlert() {
        var adapter = ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item)
        fireClient.getFirebaseEmpfehlungen1(adapter)

        var d = Dialog(context)
        d.setTitle("Einkaufen")
        d.setContentView(R.layout.add_alert_dialog)

        var name = d.findViewById<AutoCompleteTextView>(R.id.nameEditText)
        var ammount = d.findViewById<EditText>(R.id.ammountEditText)

        name.setAdapter(adapter)

        var radio_person_btn = d.findViewById<RadioButton>(R.id.radio_person)

        /*radio_person_btn.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (radio_person_btn.isChecked()) {
                personName.visibility = View.VISIBLE
            } else if (!radio_person_btn.isChecked()) {
                personName.visibility = View.GONE
            }
        })*/

        d.saveBtn.setOnClickListener {

            var currentUserID = mAuth!!.currentUser!!.uid

            var radio_itbtn = d.findViewById<RadioButton>(R.id.radio_it)
            var radio_verwaltung_btn = d.findViewById<RadioButton>(R.id.radio_verwaltung)

            if (!radio_itbtn.isChecked() && !radio_verwaltung_btn.isChecked() && !radio_person_btn.isChecked() || name.getText().toString().isEmpty() || ammount.getText().toString().isEmpty()){
                Toast.makeText(context, "Bitte alle Felder füllen!", Toast.LENGTH_SHORT).show()
            }
            else if(radio_itbtn.isChecked){
                fireClient.saveFirebaseData(name.text.toString(),ammount.text.toString().toLong(), currentUserID, "IT")
                d.dismiss()
            }
            else if(radio_verwaltung_btn.isChecked){
                fireClient.saveFirebaseData(name.text.toString(),ammount.text.toString().toLong(), currentUserID, "Verwaltung")
                d.dismiss()
            }
            else if(radio_person_btn.isChecked){
                var usermAuth = FirebaseAuth.getInstance().currentUser!!.displayName
                fireClient.saveFirebaseData(name.text.toString(),ammount.text.toString().toLong(), currentUserID, "(Person)$usermAuth")
                d.dismiss()
            }

            fireClient.checkIfEmpfehlungExists(name.text.toString())
        }

        d.show()

    }


}