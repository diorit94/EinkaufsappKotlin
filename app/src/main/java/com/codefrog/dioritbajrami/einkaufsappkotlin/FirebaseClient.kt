package com.codefrog.dioritbajrami.einkaufsappkotlin

import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EmpfehlungsAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.Empfehlungen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseClient{

    var database = FirebaseDatabase.getInstance()
    var firRef = database.reference

    var artikelArray = ArrayList<EInkaufsItem>()
    var empfehlungArray = ArrayList<Empfehlungen>()
    var arrayAdapter: BaseAdapter?=null
    var empfehlungsAdapter: EmpfehlungsAdapter?=null


    constructor(artikelArray: ArrayList<EInkaufsItem>, arrayAdapter: BaseAdapter){
        this.artikelArray = artikelArray
        this.arrayAdapter = arrayAdapter
    }

    constructor(){

    }

    constructor(empfehlungenArray: ArrayList<Empfehlungen>, adapter: EmpfehlungsAdapter){
        this.empfehlungArray = empfehlungenArray
        this.empfehlungsAdapter = adapter
    }

    fun saveFirebaseData(title: String, anzahl: Long,userID:String, verwalter: String){
        var key = firRef.push().key
        firRef.child("Artikel").child(key).setValue(EInkaufsItem(title,anzahl,verwalter,key, userID, false))
    }

    fun saveEmpfehlung(title: String){
        var key = firRef.push().key
        firRef.child("Empfehlung").child(key).setValue(Empfehlungen(title,key))
    }

    //Load data from Firebase
    fun getFirebaseData(verwaltung: String) {
        firRef.child("Artikel")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        if(!dataSnapshot.exists()){
                            return
                        }

                        artikelArray.clear()

                        var td = dataSnapshot.value as HashMap<String, Any>

                        for(key in td.keys){

                            var post = td[key] as HashMap<String, Any>

                            //IF you want to save only Verwaltungs Data
                            var verwalterData = post["verwaltung"].toString()
                            if(verwalterData.contains(verwaltung)) {
                                artikelArray.add(EInkaufsItem(
                                        post["name"] as String,
                                        post["anzahl"] as Long,
                                        post["verwaltung"] as String,
                                        post["firebaseID"] as String,
                                        post["userID"] as String,
                                        post["bought"] as Boolean))
                            } else if(verwaltung == "All" ){
                                artikelArray.add(EInkaufsItem(
                                        post["name"] as String,
                                        post["anzahl"] as Long,
                                        post["verwaltung"] as String,
                                        post["firebaseID"] as String,
                                        post["userID"] as String,
                                        post["bought"] as Boolean))

                            }
                        }
                        arrayAdapter!!.notifyDataSetChanged()
                    }

                    override fun onCancelled(p0: DatabaseError?) {
                    }
                })
    }

    fun getFirebaseEmpfehlungen(){
        firRef.child("Empfehlung").addValueEventListener(object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                if(!dataSnapshot!!.exists()){
                    return
                }

                var td = dataSnapshot!!.value as HashMap<String, Any>

                for(key in td.keys){
                    var empfehlung = td[key] as HashMap<String, Any>

                    empfehlungArray.add(Empfehlungen(
                            empfehlung["name"] as String,
                            empfehlung["firebaseID"] as String
                    ))

                }
                empfehlungsAdapter!!.notifyDataSetChanged()


            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

    fun getFirebaseEmpfehlungen1(adapter: ArrayAdapter<String>){
        firRef.child("Empfehlung").addValueEventListener(object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                var td = dataSnapshot!!.value as HashMap<String, Any>

                for(key in td.keys){
                    var empfehlung = td[key] as HashMap<String, Any>
                    adapter.add(empfehlung["name"] as String)
                }

            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

    fun checkIfEmpfehlungExists(title: String){

        firRef.child("Empfehlung").orderByChild("name").equalTo(title).addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    if (!dataSnapshot!!.exists()) {
                        saveEmpfehlung(title)
                    }
                }


            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

}