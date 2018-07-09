package com.codefrog.dioritbajrami.einkaufsappkotlin

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.*
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.LoggedIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.add_alert_dialog.*
import android.widget.ArrayAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EventModel
import com.github.clans.fab.FloatingActionMenu
import kotlinx.android.synthetic.main.alert_event.*
import java.util.*
import android.widget.DatePicker
import android.widget.TimePicker
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.res.Resources
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import kotlinx.android.synthetic.main.add_alert_event.*
import java.text.SimpleDateFormat




class Alerts(val context: Context) {

    var mAuth = FirebaseAuth.getInstance()

    var database = FirebaseDatabase.getInstance()
    val fireClient = FirebaseClient(context)
    var firRef = database.reference.ref


    //Dropt down list Items
    var dropDownArray = arrayOf("Stück", "Kiste", "Flasche", "Dose", "Karton", "Packung", "Beutel", "Schachtel", "Palette", "Fass")

    fun startAlert(editText: String) {

        val adapter = ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item)
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

            if (!radio_itbtn.isChecked() && !radio_verwaltung_btn.isChecked() && !radio_person_btn.isChecked() || name.getText().toString().isEmpty() || ammount.getText().toString().isEmpty() || name.text.toString().startsWith(" ")) {
                Toast.makeText(context, "Bitte alle Felder füllen!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (ammount.text.toString().toInt() <= 0) {
                Toast.makeText(context, "Die Artikelanzahl muss größer als 0 sein.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (radio_itbtn.isChecked) {
                fireClient.saveFirebaseData(name.text.toString(), ammount.text.toString().toLong(), currentUserID, "IT", spinner.selectedItem.toString())

                d.dismiss()
            } else if (radio_verwaltung_btn.isChecked) {
                fireClient.saveFirebaseData(name.text.toString(), ammount.text.toString().toLong(), currentUserID, "Verwaltung", spinner.selectedItem.toString())
                d.dismiss()
            } else if (radio_person_btn.isChecked) {
                val usermAuth = FirebaseAuth.getInstance().currentUser!!.displayName
                var userAuthID = FirebaseAuth.getInstance().currentUser!!.uid

                if (userAuthID == "eIeqKuxSsxZekufpxEy4jmik8DA3") {
                    fireClient.saveFirebaseData(name.text.toString(), ammount.text.toString().toLong(), currentUserID, "(Person)Admin", spinner.selectedItem.toString())
                } else {
                    fireClient.saveFirebaseData(name.text.toString(), ammount.text.toString().toLong(), currentUserID, "(Person)$usermAuth", spinner.selectedItem.toString())
                }

                d.dismiss()
            }

            //fireClient.saveEinkaufsStartenFirebaseData(name.text.toString(), ammount.text.toString().toLong(), currentUserID, "", spinner.selectedItem.toString())
            fireClient.checkIfEmpfehlungExists(name.text.toString())

        }

        d.show()

    }

    fun startEventAlert(fab: FloatingActionMenu, type: String) {
        val d = Dialog(context)
        d.setTitle("Einkaufen")
        d.setContentView(R.layout.alert_event)

        var dpd: DatePickerDialog?=null
        var tpd: TimePickerDialog?=null

        var eventName = d.findViewById<AutoCompleteTextView>(R.id.name_event_alert_id)

        var pic: Int?=null
        var pic1: Int?=null
        var pic2: Int?=null
        var pic3: Int?=null
        var pic4: Int?=null

        d.pickDateID.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month= c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->
                var month = datePicker.month+1
                d.pickDateID.text = mDay.toString() + "."+month+"."+mYear
                pic = datePicker.year
                pic1 = datePicker.month
                pic2 = datePicker.dayOfMonth

            }, year,month,day)

            dpd!!.show()
        }

        d.pickTimeID.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            tpd = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, mHour, mMinute ->
                d.pickTimeID.text = String.format("%02d:%02d", mHour, mMinute)
                pic3 = timePicker.hour
                pic4 = timePicker.minute
            }, hour,minute, true)

            tpd!!.show()
        }

        d.event_hinzufügen_id.setOnClickListener {

            if (eventName.text.toString().isEmpty() || d.pickDateID.text.toString() == "Datum?" || d.pickTimeID.text.toString() == "Uhrzeit?" || eventName.text.toString().startsWith(" ")) {
                Toast.makeText(context, "Bitte alle Felder füllen!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val calendar = Calendar.getInstance()
            calendar.set(pic!!,pic1!!,pic2!!,pic3!!,pic4!!)
            val startTime = calendar.timeInMillis

            fireClient.saveEvent(eventName.text.toString(), startTime, d.pickTimeID.text.toString(), type)

            d.dismiss()
            fab.close(true)
        }
        d.show()
    }

    fun startEventItemAlert(key: String) {

        val adapter = ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item)
        fireClient.getFirebaseEmpfehlungen1(adapter)

        val d = Dialog(context)
        d.setTitle("Event Item")
        d.setContentView(R.layout.add_alert_event)

        var name = d.findViewById<AutoCompleteTextView>(R.id.eventNameEditText)
        val ammount = d.findViewById<EditText>(R.id.eventAmmountEditText)
        name.setAdapter(adapter)

        val spinner = d.findViewById<Spinner>(R.id.spinnerID)

        val gameKindArray = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, dropDownArray)
        gameKindArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = gameKindArray

        d.addEventItemButtonID.setOnClickListener {

            if (name.text.toString().isEmpty() || ammount.text.toString().isEmpty() || name.text.toString().startsWith(" ")) {
                Toast.makeText(context, "Bitte alle Felder füllen!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (ammount.text.toString().toInt() <= 0) {
                Toast.makeText(context, "Die Artikelanzahl kann nicht 0 sein.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val firRefKey = firRef.push().key

            if (mAuth.currentUser!!.uid == "eIeqKuxSsxZekufpxEy4jmik8DA3") {
                firRef.child("Event").child(key).child("EventEinkauf").child(firRefKey).setValue(EInkaufsItem(name.text.toString(), ammount.text.toString().toLong(), "Admin", firRefKey, mAuth.currentUser!!.uid!!, false, spinner.selectedItem.toString()))
            } else {
                firRef.child("Event").child(key).child("EventEinkauf").child(firRefKey).setValue(EInkaufsItem(name.text.toString(), ammount.text.toString().toLong(), mAuth.currentUser!!.displayName!!, firRefKey, mAuth.currentUser!!.uid!!, false, spinner.selectedItem.toString()))
            }

            fireClient.checkIfEmpfehlungExists(name.text.toString())
            d.dismiss()
        }

        d.show()
    }

    fun updateTheme(model: EventModel){
        if(model.type == "Geburtstag"){
            context.setTheme(R.style.GeburtstagStyle)
        } else if(model.type == "Event Essen"){
            context.setTheme(R.style.essenStyle)
        }else if(model.type == "Andere"){
            context.setTheme(R.style.andereStyle)
        }
    }

    fun updateEvent(model: EventModel) {


        updateTheme(model)

        val d = Dialog(context)
        d.setTitle("Einkaufen")
        d.setContentView(R.layout.alert_event)


        var eventName = d.findViewById<AutoCompleteTextView>(R.id.name_event_alert_id)

        var dpd: DatePickerDialog?=null
        var tpd: TimePickerDialog?=null

        var pic: Int?=null
        var pic1: Int?=null
        var pic2: Int?=null
        var pic3: Int?=null
        var pic4: Int?=null

        var bool1 = false
        var bool2 = false

        d.pickDateID.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month= c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->
                var month = datePicker.month+1
                d.pickDateID.text = mDay.toString() + "."+month+"."+mYear
                pic = datePicker.year
                pic1 = datePicker.month
                pic2 = datePicker.dayOfMonth
                bool1 = true

            }, year,month,day)

            dpd!!.show()
        }

        d.pickTimeID.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            tpd = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, mHour, mMinute ->
                d.pickTimeID.text = String.format("%02d:%02d", mHour, mMinute)
                pic3 = timePicker.hour
                pic4 = timePicker.minute
                bool2 = true
            }, hour,minute, true)

            tpd!!.show()
        }
        d.event_hinzufügen_id.text = "Event ändern"
        d.event_hinzufügen_id.setOnClickListener {

            if (eventName.text.toString().isEmpty() || d.pickDateID.text.toString() == "Datum?" || d.pickTimeID.text.toString() == "Uhrzeit?" || eventName.text.toString().startsWith(" ")) {
                Toast.makeText(context, "Bitte alle Felder füllen!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(bool1 == true){
                val calendar = Calendar.getInstance()
                calendar.set(pic!!,pic1!!,pic2!!)
                val startTime = calendar.timeInMillis

                fireClient.updateEvent(eventName.text.toString(), startTime, d.pickTimeID.text.toString(), model.firebaseID)
            } else {
                fireClient.updateEvent(eventName.text.toString(), model.date, d.pickTimeID.text.toString(), model.firebaseID)
            }

            d.dismiss()
        }


        eventName.setText(model.name)
        d.pickTimeID.text = model.uhrzeit

        var formatter = SimpleDateFormat("dd.MM.yyyy");
        var dateString = formatter.format(Date(model.date));

        d.pickDateID.text = dateString


        d.show()
    }

}