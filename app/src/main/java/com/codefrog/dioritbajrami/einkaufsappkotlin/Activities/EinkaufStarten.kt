package com.codefrog.dioritbajrami.einkaufsappkotlin.Activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EinkaufStartenAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList
import android.view.animation.AnimationUtils
import android.widget.*
import com.codefrog.dioritbajrami.einkaufsappkotlin.CircularReveal
import kotlinx.android.synthetic.main.activity_einkauf_starten.*


class EinkaufStarten : AppCompatActivity() {

    var einkaufArray = ArrayList<EInkaufsItem>()
    var listView: ListView? = null

    var database = FirebaseDatabase.getInstance()
    var firRef = database.reference

    var abschliessButton: Button? = null
    var abbrechButton: Button? = null

    var relativeLayout: RelativeLayout ?= null
    var layoutContent: RelativeLayout ?= null
    var layoutCg: RelativeLayout?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_einkauf_starten)

        title = this.getString(R.string.Einkauf_starten)

        relativeLayout = findViewById(R.id.layoutM)
        layoutContent = findViewById(R.id.layoutC)
        layoutCg = findViewById(R.id.layoutCG)

        //progressDialog = findViewById(R.id.progressBarDialogID)

        val startenAdapter = EinkaufStartenAdapter(this, einkaufArray)
        listView = findViewById(R.id.einkaufStartenID)
        listView!!.adapter = startenAdapter
        listView!!.isStackFromBottom = false
        val firebaseClient = FirebaseClient(einkaufArray, startenAdapter)

        firebaseClient.getFirebaseData("All", "EinkaufsStarten")

        abschliessButton = findViewById(R.id.buttonEinkaufAbschliessenID)
        abbrechButton = findViewById(R.id.buttonEinkaufAbbrechenID)

        abschliessButton!!.setOnClickListener {
            areAllTrue(einkaufArray)
        }

        abbrechButton!!.setOnClickListener {
            startAlertEinkaufAbschliessen("Einkauf abbrechen?", true)
        }
        //loopthroughFB()
        checkIfEmpty()

    }


    //Look in array if all are true
    fun areAllTrue(array: ArrayList<EInkaufsItem>) {
        for (b in array)
            if (!b.bought!!) {
                startAlertEinkaufAbschliessen(applicationContext.getString(R.string.nicht_alles_gekauft),false)
                return
            }
        startAlertEinkaufAbschliessen("Einkauf abschließen?", false)
    }

    //Look branch in database if all are true
    /*fun loopthroughFB(){
        firRef.child("Artikel").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (firesnapShot in dataSnapshot.children) {
                    val bool = firesnapShot.child("bought").getValue(Boolean::class.java)!!
                    if(!bool){
                        //NOT ALL ARE TRUE
                        startAlertEinkaufAbschliessen("Nicht alle Sachen wurden eingekauft, trotzdem einkauf abschliessen?")
                        return
                    }
                }
                //ALL ARE TRUE
                startAlertEinkaufAbschliessen("Einkauf abschliessen?")
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }*/

    fun checkIfEmpty() {
        firRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.hasChild("Artikel")) {
                    val slide = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_fadeout) //the above transition
                    abschliessButton!!.startAnimation(slide)
                    abbrechButton!!.startAnimation(slide)

                    abschliessButton!!.visibility = View.INVISIBLE
                    abbrechButton!!.visibility = View.INVISIBLE

                    var cReveal = CircularReveal()
                    cReveal.getCircularReveal(layoutCg!!, layoutContent!!,relativeLayout!!)

                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

    fun returnToBoughtToFalse(branch: String){
        firRef.child(branch).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                for(fireSnapShot in dataSnapshot!!.children){
                    fireSnapShot.ref.child("bought").setValue(false)
                }
            }

            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun startAlertEinkaufAbschliessen(saveText: String, abbrechen: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(saveText)
        builder.setPositiveButton("Ja", DialogInterface.OnClickListener { dialog, id ->
            val currentTime = System.currentTimeMillis()

            if(!abbrechen){
                val fireClient = FirebaseClient()
                fireClient.saveEhemaligeEinkaeufe(currentTime)
                firRef.child("EinkaufsStarten").removeValue()
                Toast.makeText(this, "Eingekauft!", Toast.LENGTH_SHORT).show()
            } else {
                returnToBoughtToFalse("Artikel")
                returnToBoughtToFalse("EinkaufsStarten")
            }
            finish()
        })
        builder.setNegativeButton("Nein", DialogInterface.OnClickListener { dialog, id ->
            // User cancelled the dialog
        })
        builder.show()
    }


}