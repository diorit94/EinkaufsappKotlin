package com.codefrog.dioritbajrami.einkaufsappkotlin.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EinkaufStartenAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import com.google.firebase.database.FirebaseDatabase

class EinkaufStarten : AppCompatActivity() {

    var einkaufArray = ArrayList<EInkaufsItem>()
    var listView: ListView?=null

    var database = FirebaseDatabase.getInstance()
    var firRef = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_einkauf_starten)

        val startenAdapter = EinkaufStartenAdapter(this,einkaufArray)
        listView = findViewById(R.id.einkaufStartenID)
        listView!!.adapter = startenAdapter

        val firebaseClient = FirebaseClient(einkaufArray, startenAdapter)
        firebaseClient.getFirebaseData("All")

    }

}
