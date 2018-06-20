package com.codefrog.dioritbajrami.einkaufsappkotlin

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.*
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.LoggedIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.add_alert_dialog.*
import android.widget.ArrayAdapter



class Alerts(val context: Context){

    var mAuth = FirebaseAuth.getInstance()

    var database = FirebaseDatabase.getInstance()

    //Dropt down list Items
    var dropDownArray = arrayOf("Stück","Kiste","Packung", "Beutel","Schachtel", "Palette")

    fun startAlert(editText: String) {
        val fireClient = FirebaseClient(context)

        val adapter = ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item)
        fireClient.getFirebaseEmpfehlungen1(adapter)

        val d = Dialog(context)
        d.setTitle("Einkaufen")
        d.setContentView(R.layout.add_alert_dialog)

        var name = d.findViewById<AutoCompleteTextView>(R.id.nameEditText)
        val ammount = d.findViewById<EditText>(R.id.ammountEditText)
        name.setAdapter(adapter)

        val radio_person_btn = d.findViewById<RadioButton>(R.id.radio_person)

        val spinner = d.findViewById<Spinner>(R.id.spinner)

        val gameKindArray = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, dropDownArray)
        gameKindArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = gameKindArray

        /*radio_person_btn.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (radio_person_btn.isChecked()) {
                personName.visibility = View.VISIBLE
            } else if (!radio_person_btn.isChecked()) {
                personName.visibility = View.GONE
            }
        })*/

        name.setText(editText)

        d.saveBtn.setOnClickListener {

            val currentUserID = mAuth!!.currentUser!!.uid

            val radio_itbtn = d.findViewById<RadioButton>(R.id.radio_it)
            val radio_verwaltung_btn = d.findViewById<RadioButton>(R.id.radio_verwaltung)

            if (!radio_itbtn.isChecked() && !radio_verwaltung_btn.isChecked() && !radio_person_btn.isChecked() || name.getText().toString().isEmpty() || ammount.getText().toString().isEmpty()){
                Toast.makeText(context, "Bitte alle Felder füllen!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if(ammount.text.toString() == "0"){
                Toast.makeText(context, "Die zahl kann nicht 0 sein", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(radio_itbtn.isChecked){
                fireClient.saveFirebaseData(name.text.toString(),ammount.text.toString().toLong(), currentUserID, "IT", spinner.selectedItem.toString())
                d.dismiss()
            }
            else if(radio_verwaltung_btn.isChecked){
                fireClient.saveFirebaseData(name.text.toString(),ammount.text.toString().toLong(), currentUserID, "Verwaltung", spinner.selectedItem.toString())
                d.dismiss()
            }
            else if(radio_person_btn.isChecked){
                val usermAuth = FirebaseAuth.getInstance().currentUser!!.displayName
                fireClient.saveFirebaseData(name.text.toString(),ammount.text.toString().toLong(), currentUserID, "(Person)$usermAuth", spinner.selectedItem.toString())
                d.dismiss()
            }


            fireClient.checkIfEmpfehlungExists(name.text.toString())

        }

        d.show()

    }


}