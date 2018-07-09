package com.codefrog.dioritbajrami.einkaufsappkotlin

import android.app.AlertDialog
import android.content.Context
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.Empfehlungen
import android.content.DialogInterface
import android.util.Log
import android.view.View
import android.widget.*
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.EinkaufStarten
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.LoggedIn
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.*
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EhemaligeEinkaeufe
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EventModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class FirebaseClient {

    var database = FirebaseDatabase.getInstance()
    var firRef = database.reference.ref

    var artikelArray = ArrayList<EInkaufsItem>()

    var empfehlungArray = ArrayList<Empfehlungen>()
    var arrayAdapter: EinkaufsItemAdapter? = null
    var empfehlungsAdapter: EmpfehlungsAdapter? = null
    var shopAdapter: BaseAdapter? = null

    var cons: Boolean? = null
    var mAuth = FirebaseAuth.getInstance()

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

    fun saveFirebaseData(title: String, anzahl: Long, userID: String, verwalter: String, type: String) {
        val key = firRef.push().key

        if (verwalter.contains("Person")) {

            firRef.child("Artikel").orderByChild("name").equalTo(title).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    for (firesnapshot in dataSnapshot!!.children) {

                        val userid = firesnapshot.child("userID").getValue(String::class.java)!!
                        val beforeammount = firesnapshot.child("anzahl").getValue(Int::class.java)!!
                        val verwaltung = firesnapshot.child("verwaltung").getValue(String::class.java)
                        val einheit = firesnapshot.child("type").getValue(String::class.java)

                        if (einheit == type) {
                            if (firesnapshot.exists() && userid == userID && verwaltung == verwalter) {
                                val builder = AlertDialog.Builder(context)
                                builder.setMessage("Es existiert bereits " + beforeammount + "x $title, $type auf der Liste für die $verwalter, noch $anzahl hinzufügen?")
                                builder.setPositiveButton("Ja", DialogInterface.OnClickListener { dialog, id ->

                                    var actualAmmount = beforeammount + anzahl
                                    if (actualAmmount > 99) {
                                        Toast.makeText(context, R.string.anzahl99, Toast.LENGTH_LONG).show()
                                    } else {
                                        firesnapshot.ref.child("anzahl").setValue(beforeammount + anzahl)
                                        saveEinkaufsStartenFirebaseData(title, anzahl, userID, verwalter, type)
                                    }

                                })
                                builder.setNegativeButton("Nein", DialogInterface.OnClickListener { dialog, id ->
                                    // User cancelled the dialog
                                })
                                builder.show()
                                return
                            }
                        }
                    }
                    firRef.child("Artikel").child(key).setValue(EInkaufsItem(title, anzahl, verwalter, key, userID, false, type))
                    saveEinkaufsStartenFirebaseData(title, anzahl, userID, verwalter, type)


                }

                override fun onCancelled(p0: DatabaseError?) {

                }
            })
            return
        }
        firRef.child("Artikel").orderByChild("name").equalTo(title).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                for (fireDataSnapshot in dataSnapshot!!.children) {

                    val verwaltung = fireDataSnapshot.child("verwaltung").getValue(String::class.java)
                    val zahl = fireDataSnapshot.child("anzahl").getValue(Int::class.java)!!
                    val einheit = fireDataSnapshot.child("type").getValue(String::class.java)

                    if (einheit == type) {
                        if (fireDataSnapshot.exists() && verwaltung == verwalter) {
                            val builder = AlertDialog.Builder(context)
                            builder.setMessage("Es existiert bereits " + zahl + "x $title, $type auf der Liste für die $verwalter, noch $anzahl hinzufügen?")
                            builder.setPositiveButton("Ja", DialogInterface.OnClickListener { dialog, id ->

                                val beforeammount = fireDataSnapshot.child("anzahl").getValue(Int::class.java)!!
                                val userUID = fireDataSnapshot.child("userID").getValue(String::class.java)

                                var actualAmmount = beforeammount + anzahl
                                if (actualAmmount > 99) {
                                    Toast.makeText(context, R.string.anzahl99, Toast.LENGTH_LONG).show()
                                } else {
                                    fireDataSnapshot.ref.child("anzahl").setValue(actualAmmount)
                                    fireDataSnapshot.ref.child("userID").setValue(userID)
                                    saveEinkaufsStartenFirebaseData(title, anzahl, userID, verwalter, type)
                                }

                            })
                            builder.setNegativeButton("Nein", DialogInterface.OnClickListener { dialog, id ->
                                // User cancelled the dialog
                            })
                            builder.show()

                            return
                        }
                    }
                }
                firRef.child("Artikel").child(key).setValue(EInkaufsItem(title, anzahl, verwalter, key, userID, false, type))
                saveEinkaufsStartenFirebaseData(title, anzahl, userID, verwalter, type)
            }


            override fun onCancelled(p0: DatabaseError?) {

            }
        })


    }

    fun saveEinkaufsStartenFirebaseData(title: String, anzahl: Long, userID: String, verwalter: String, type: String) {
        val key = firRef.push().key
        //firRef.child("EinkaufsStarten").child(key).setValue(EInkaufsItem(title, anzahl, verwalter, key, userID, false, type))
        firRef.child("EinkaufsStarten").orderByChild("name").equalTo(title).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapShot: DataSnapshot?) {
                for(fireSnapShot in dataSnapShot!!.children){
                    val zahl = fireSnapShot.child("anzahl").getValue(Int::class.java)!!
                    val einheit = fireSnapShot.child("type").getValue(String::class.java)

                    if(einheit == type){
                        val totalAmmount = zahl + anzahl

                        fireSnapShot.ref.child("anzahl").setValue(totalAmmount)
                        return
                    }
                }
                firRef.child("EinkaufsStarten").child(key).setValue(EInkaufsItem(title, anzahl, verwalter, key, userID, false, type))
            }

            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }


        //Load data from Firebase
    fun getFirebaseData(verwaltung: String, branch: String) {
        val query = firRef.child(branch).orderByChild("name")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                artikelArray.clear()

                if (!dataSnapshot.exists()) {
                    return
                }


                val td = dataSnapshot.value as HashMap<String, Any>

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
                                post["bought"] as Boolean,
                                post["type"] as String))

                    } else if (verwaltung == "All") {
                        artikelArray.add(EInkaufsItem(
                                post["name"] as String,
                                post["anzahl"] as Long,
                                post["verwaltung"] as String,
                                post["firebaseID"] as String,
                                post["userID"] as String,
                                post["bought"] as Boolean,
                                post["type"] as String))
                    }
                    //Sorting by name
                    artikelArray.sortBy {
                        it.name
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


    fun saveEmpfehlung(title: String, counter: Long) {
        val key = firRef.push().key
        firRef.child("Empfehlung").child(key).setValue(Empfehlungen(title, key, counter))
    }

    fun getFirebaseEmpfehlungen() {
        firRef.child("Empfehlung").orderByChild("name").addValueEventListener(object : ValueEventListener {

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

                        //Sort it by name
                        empfehlungArray.sortBy {
                            it.name
                        }
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
                    saveEmpfehlung(title, 1)
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

    fun saveEhemaligeEinkaeufe(currentDate: Long) {
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
                            post["bought"] as Boolean,
                            post["type"] as String))
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
                            post["date"] as Long,
                            post["key"] as String
                    ))
                    einkaufsArrayShop.sortBy {
                        it.date
                    }
                    einkaufsArrayShop.reverse()
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
                            post["bought"] as Boolean,
                            post["type"] as String))

                    arrayItemList.sortBy {
                        it.name
                    }
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

    fun saveEvent(eventName: String, pickDate: Long, pickTime: String , pickType: String) {
        var key = firRef.push().key
        firRef.child("Event").child(key).setValue(EventModel(eventName, pickDate,pickTime,pickType,key))
    }


    fun getEventData(arrayEventList: ArrayList<EventModel>, adapter: EventListAdapter) {
        firRef.child("Event").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (!dataSnapshot.exists()) {
                    return
                }
                arrayEventList.clear()


                val td = dataSnapshot.value as HashMap<String, Any>

                for (key in td.keys) {

                    val post = td[key] as HashMap<String, Any>

                    arrayEventList.add(EventModel(
                            post["name"] as String,
                            post["date"] as Long,
                            post["uhrzeit"] as String,
                            post["type"] as String,
                            post["firebaseID"] as String))
                    arrayEventList.sortBy {
                        it.date
                    }

                }
                adapter.notifyDataSetChanged()
            }


            override fun onCancelled(p0: DatabaseError?) {
            }
        })
    }

    fun getEventItems(key: String, arrayEventList: ArrayList<EInkaufsItem>, adapter: EventListItemAdapter) {
        firRef.child("Event").child(key).child("EventEinkauf").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (!dataSnapshot.exists()) {
                    return
                }
                arrayEventList.clear()


                val td = dataSnapshot.value as HashMap<String, Any>

                for (key in td.keys) {

                    val post = td[key] as HashMap<String, Any>

                    arrayEventList.add(EInkaufsItem(
                            post["name"] as String,
                            post["anzahl"] as Long,
                            post["verwaltung"] as String,
                            post["firebaseID"] as String,
                            post["userID"] as String,
                            post["bought"] as Boolean,
                            post["type"] as String))

                    arrayEventList.sortBy {
                        it.name
                    }
                }
                adapter.notifyDataSetChanged()
            }


            override fun onCancelled(p0: DatabaseError?) {
            }
        })
    }

    fun updateEvent(eventName: String, pickDate: Long, pickTime: String, id: String){
        firRef.child("Event").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapShot: DataSnapshot) {
                for(fireSnapShot in dataSnapShot.children){
                    val keyID = fireSnapShot.child("firebaseID").getValue(String::class.java)!!

                    if(keyID == id){
                        fireSnapShot.ref.child("name").setValue(eventName)
                        fireSnapShot.ref.child("date").setValue(pickDate)
                        fireSnapShot.ref.child("uhrzeit").setValue(pickTime)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }


    fun deleteEventList(branchKey: String, key: String){
        var query = firRef.child("Event").child(branchKey).child("EventEinkauf").orderByChild("firebaseID").equalTo(key)

        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                for (firesnapshot in dataSnapshot!!.children) {
                    firesnapshot.ref.removeValue()
                }
            }

            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }


    fun deleteEinkaufsStartenItem(name:String, number: Long, type: String){
        var query = firRef.child("EinkaufsStarten").orderByChild("name").equalTo(name)

        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapShot: DataSnapshot?) {
                for(fireSnapShot in dataSnapShot!!.children){
                    val anzahl = fireSnapShot.child("anzahl").getValue(Long::class.java)!!
                    val einheit = fireSnapShot.child("type").getValue(String::class.java)!!

                    if(type == einheit){
                        val finalValue = anzahl-number
                        fireSnapShot.ref.child("anzahl").setValue(finalValue)

                        if(finalValue<=0){
                            fireSnapShot.ref.removeValue()
                        }
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }


}