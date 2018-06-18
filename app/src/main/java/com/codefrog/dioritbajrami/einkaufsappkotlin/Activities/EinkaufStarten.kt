package com.codefrog.dioritbajrami.einkaufsappkotlin.Activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.drawable.RippleDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EinkaufStartenAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.CircularReveal
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class EinkaufStarten : AppCompatActivity() {

    var einkaufArray = ArrayList<EInkaufsItem>()
    var listView: ListView? = null

    var database = FirebaseDatabase.getInstance()
    var firRef = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_einkauf_starten)

        val startenAdapter = EinkaufStartenAdapter(this, einkaufArray)
        listView = findViewById(R.id.einkaufStartenID)
        listView!!.adapter = startenAdapter
        listView!!.isStackFromBottom = false
        val firebaseClient = FirebaseClient(einkaufArray, startenAdapter)
        firebaseClient.getFirebaseData("All")


        val abschliessButton = findViewById(R.id.buttonEinkaufAbschliessenID) as Button

        abschliessButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Einkauf Abschliessen?")
            builder.setPositiveButton("Ja", DialogInterface.OnClickListener { dialog, id ->
                val sdf = SimpleDateFormat("d/M/yyyy")
                val currentDate = sdf.format(Date())

                val fireClient = FirebaseClient()
                fireClient.saveEhemaligeEinkaeufe(currentDate)
                finish()
            })
            builder.setNegativeButton("Nein", DialogInterface.OnClickListener { dialog, id ->
                // User cancelled the dialog
            })
            builder.show()



        }


        /*var relativeLayout = findViewById<RelativeLayout>(R.id.layoutCG)
        var contentLayout = findViewById<RelativeLayout>(R.id.layoutC)
        var layoutMain = findViewById<RelativeLayout>(R.id.layoutM)

        val startTimer = Timer()
        startTimer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if(einkaufArray.size == 0){
                        var cr = CircularReveal()
                        cr.getCircularReveal(relativeLayout, layoutMain,contentLayout)
                    }
                }
            }
        }, 500)*/
    }
}