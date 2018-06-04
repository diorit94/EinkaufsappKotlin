package com.codefrog.dioritbajrami.einkaufsappkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.ListView
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EmpfehlungsAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.Empfehlungen

class Empfehlung_activity : AppCompatActivity() {

    var recyclerView: RecyclerView?=null
    var empfehlungsArray = ArrayList<Empfehlungen>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empfehlung_activity)

        recyclerView = findViewById(R.id.empgehlungListViewID)

        recyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        val adapter = EmpfehlungsAdapter(empfehlungsArray)
        recyclerView!!.adapter = adapter

        var firebaseClient = FirebaseClient(empfehlungsArray, adapter)
        firebaseClient.getFirebaseEmpfehlungen()

    }

    /*fun getData(){
        empfehlungsArray.add(Empfehlungen("Diorit"))
        empfehlungsArray.add(Empfehlungen("Granit"))
        empfehlungsArray.add(Empfehlungen("Festina"))
        empfehlungsArray.add(Empfehlungen("Atifete"))

    }*/
}
