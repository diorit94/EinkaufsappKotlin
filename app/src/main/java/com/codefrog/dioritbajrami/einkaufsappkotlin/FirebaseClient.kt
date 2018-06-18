package com.codefrog.dioritbajrami.einkaufsappkotlin

import android.app.AlertDialog
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EmpfehlungsAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.Empfehlungen
import android.content.DialogInterface
import android.util.Log
import android.widget.RelativeLayout
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.LoggedIn
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EinkaufsItemAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EhemaligeEinkaufItemAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EhemaligeEinkaeufe
import com.google.firebase.database.*


class FirebaseClient {

    var database = FirebaseDatabase.getInstance()
    var firRef = database.reference

    var artikelArray = ArrayList<EInkaufsItem>()

    var empfehlungArray = ArrayList<Empfehlungen>()
    var arrayAdapter: EinkaufsItemAdapter? = null
    var empfehlungsAdapter: EmpfehlungsAdapter? = null
    var shopAdapter: BaseAdapter? = null

    var cons: Boolean? = null


    constructor(artikelArray: ArrayList<EInkaufsItem>, arrayAdapter: EinkaufsItemAdapter) {
        this.artikelArray = artikelArray
        this.arrayAdapter = arrayAdapter
        cons = true
    }

    constructor(empfehlungenArray: ArrayList<Empfehlungen>, adapter: EmpfehlungsAdapter) {
        this.empfehlungArray = empfehlungenArray
        this.empfehlungsAdapter = adapter
    }

    constructor(einkaufsArrayShop: ArrayList<EInkaufsItem>, baseadapter: BaseAdapter) {
        this.artikelArray = einkaufsArrayShop
        this.shopAdapter = baseadapter
        cons = false
    }

    constructor(baseadapter: BaseAdapter) {
        this.shopAdapter = baseadapter
    }


    constructor(einkaufsArrayShop: ArrayList<EInkaufsItem>) {
        this.artikelArray = einkaufsArrayShop
    }


    constructor() {

    }


    var context: Context? = null

    constructor(context: Context) {
        this.context = context
    }

    fun saveFirebaseData(title: String, anzahl: Long, userID: String, verwalter: String) {
        val key = firRef.push().key

        if (verwalter.contains("Person")) {
            firRef.child("Artikel").child(key).setValue(EInkaufsItem(title, anzahl, verwalter, key, userID, false))
            return
        }
        firRef.child("Artikel").orderByChild("name").equalTo(title).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                for (fireDataSnapshot in dataSnapshot!!.children) {

                    val verwaltung = fireDataSnapshot.child("verwaltung").getValue(String::class.java)
                    val zahl = fireDataSnapshot.child("anzahl").getValue(Int::class.java)!!

                    if (fireDataSnapshot.exists() && verwaltung == verwalter) {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Es existiert bereits $zahl $title auf der $verwalter liste, noch $anzahl hinzufügen?")
                        builder.setPositiveButton("Ja", DialogInterface.OnClickListener { dialog, id ->
                            val beforeammount = fireDataSnapshot.child("anzahl").getValue(Int::class.java)!!
                            fireDataSnapshot.ref.child("anzahl").setValue(beforeammount + anzahl)
                        })
                        builder.setNegativeButton("Nein", DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog
                        })
                        builder.show()

                        return
                    }
                }
                firRef.child("Artikel").child(key).setValue(EInkaufsItem(title, anzahl, verwalter, key, userID, false))
            }


            override fun onCancelled(p0: DatabaseError?) {

            }
        })


    }


    //Load data from Firebase
    fun getFirebaseData(verwaltung: String) {

        firRef.child("Artikel")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        artikelArray.clear()

                        if (!dataSnapshot.exists()) {
                            return
                        }


                        val td = dataSnapshot.value as HashMap<String, Any>
                        var counterTrue: Boolean? = null

                        for (key in td.keys) {

                            val post = td[key] as HashMap<String, Any>

                            //IF you want to save only Verwaltungs Data
                            val verwalterData = post["verwaltung"].toString()
                            if (verwalterData.contains(verwaltung)) {
                                artikelArray.add(EInkaufsItem(
                                        post["name"] as String,
                                        post["anzahl"] as Long,
                                        post["verwaltung"] as String,
                                        post["firebaseID"] as String,
                                        post["userID"] as String,
                                        post["bought"] as Boolean))

                            } else if (verwaltung == "All") {
                                artikelArray.add(EInkaufsItem(
                                        post["name"] as String,
                                        post["anzahl"] as Long,
                                        post["verwaltung"] as String,
                                        post["firebaseID"] as String,
                                        post["userID"] as String,
                                        post["bought"] as Boolean))
                            }
                        }


                        if (cons == false) {
                            shopAdapter!!.notifyDataSetChanged()
                        } else if (cons == true) {
                            arrayAdapter!!.notifyDataSetChanged()
                        }

                    }

                    override fun onCancelled(p0: DatabaseError?) {
                    }
                })
    }


    fun saveEmpfehlung(title: String) {
        val key = firRef.push().key
        firRef.child("Empfehlung").child(key).setValue(Empfehlungen(title, key, 1))
    }

    fun getFirebaseEmpfehlungen() {
        firRef.child("Empfehlung").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                if (!dataSnapshot!!.exists()) {
                    return
                }

                empfehlungArray.clear()

                val td = dataSnapshot.value as HashMap<String, Any>

                for (key in td.keys) {
                    val empfehlung = td[key] as HashMap<String, Any>


                    val counter = empfehlung["counter"] as Long

                    if (counter >= 3) {
                        empfehlungArray.add(Empfehlungen(
                                empfehlung["name"] as String,
                                empfehlung["firebaseID"] as String,
                                empfehlung["counter"] as Long
                        ))
                    }


                }
                empfehlungsAdapter!!.notifyDataSetChanged()


            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

    fun getFirebaseEmpfehlungen1(adapter: ArrayAdapter<String>) {
        firRef.child("Empfehlung").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                if (!dataSnapshot!!.exists()) {
                    return
                }

                val td = dataSnapshot.value as HashMap<String, Any>

                for (key in td.keys) {
                    val empfehlung = td[key] as HashMap<String, Any>
                    var counter = empfehlung["counter"] as Long

                    if (counter >= 3) {
                        adapter.add(empfehlung["name"] as String)
                    }

                }

            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

    fun checkIfEmpfehlungExists(title: String) {

        firRef.child("Empfehlung").orderByChild("name").equalTo(title).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if (!dataSnapshot!!.exists()) {
                    saveEmpfehlung(title)
                } else {
                    for (fireDataSnapshot in dataSnapshot!!.children) {
                        var counter = fireDataSnapshot.child("counter").getValue(Long::class.java)
                        fireDataSnapshot.ref.child("counter").setValue(counter!!.plus(1))
                    }
                }
            }


            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

    fun saveEhemaligeEinkaeufe(currentDate: String) {
        firRef.child("Artikel").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var einkaufsArrayShop = HashMap<String, EInkaufsItem>()
                einkaufsArrayShop.clear()

                if (!dataSnapshot.exists()) {
                    return
                }

                val td = dataSnapshot.value as HashMap<String, Any>

                for (key in td.keys) {
                    val post = td[key] as HashMap<String, Any>
                    var key = firRef.push().key

                    einkaufsArrayShop.put(key, EInkaufsItem(
                            post["name"] as String,
                            post["anzahl"] as Long,
                            post["verwaltung"] as String,
                            post["firebaseID"] as String,
                            post["userID"] as String,
                            post["bought"] as Boolean))
                }

                var childKey = firRef.push().key
                firRef.child("Ehemalige Einkaeufe").child(childKey).setValue(EhemaligeEinkaeufe(currentDate, childKey, einkaufsArrayShop))

                //THEN REMOVE THE DATA FROM THE ARTICEL
                firRef.child("Artikel").removeValue()

            }

            override fun onCancelled(p0: DatabaseError?) {
            }
        })
    }

    fun getEhemaligeEinkaeufe(einkaufsArrayShop: ArrayList<EhemaligeEinkaeufe>) {
        val query: Query = firRef.child("Ehemalige Einkaeufe")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                einkaufsArrayShop.clear()

                if (!dataSnapshot.exists()) {
                    return
                }

                val td = dataSnapshot.value as HashMap<String, Any>

                for (key in td.keys) {
                    val post = td[key] as HashMap<String, Any>

                    einkaufsArrayShop.add(EhemaligeEinkaeufe(
                            post["date"] as String,
                            post["key"] as String
                    ))
                }
                shopAdapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError?) {
            }
        })
    }

    fun loadEhemaligItemData(key: String, arrayItemList: ArrayList<EInkaufsItem>, adapter: EhemaligeEinkaufItemAdapter) {
        firRef.child("Ehemalige Einkaeufe").child(key).child("einkaufsMap").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (!dataSnapshot.exists()) {
                    return
                }
                arrayItemList.clear()


                val td = dataSnapshot.value as HashMap<String, Any>

                for (key in td.keys) {

                    val post = td[key] as HashMap<String, Any>

                    arrayItemList.add(EInkaufsItem(
                            post["name"] as String,
                            post["anzahl"] as Long,
                            post["verwaltung"] as String,
                            post["firebaseID"] as String,
                            post["userID"] as String,
                            post["bought"] as Boolean))
                }
                adapter.notifyDataSetChanged()
            }


            override fun onCancelled(p0: DatabaseError?) {
            }
        })
    }

    fun deleteFirebase(mainBranch: String, key: String) {
        val query = firRef.child(mainBranch).orderByChild("firebaseID").equalTo(key)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                for (firesnapshot in dataSnapshot!!.children) {
                    firesnapshot.ref.removeValue()
                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }
}