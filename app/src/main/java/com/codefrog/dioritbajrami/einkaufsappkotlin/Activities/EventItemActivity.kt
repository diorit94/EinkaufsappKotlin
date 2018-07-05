package com.codefrog.dioritbajrami.einkaufsappkotlin.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EventListItemAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Alerts
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EventItemActivity : AppCompatActivity() {

    var recyclerView : RecyclerView?=null
    var eventArtikelArray = ArrayList<EInkaufsItem>()
    var eventAdapter: EventListItemAdapter?=null

    var database = FirebaseDatabase.getInstance()

    var floatingButton: FloatingActionButton?=null

    var firebaseIDKey :String ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent

        firebaseIDKey = intent.getStringExtra("FirID")
        val TypeEvent = intent.getStringExtra("Type")
        val Title = intent.getStringExtra("Title")

        if(TypeEvent == "Geburtstag"){
            setTheme(R.style.GeburtstagStyle)
        }else if (TypeEvent == "Event Essen"){
            setTheme(R.style.essenStyle)
        }else if(TypeEvent == "Andere"){
            setTheme(R.style.andereStyle)
        }

        setContentView(R.layout.activity_event_item)

        title = Title

        recyclerView = findViewById(R.id.event_list_recycler_view)
        recyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        eventAdapter = EventListItemAdapter(this, eventArtikelArray)
        recyclerView!!.adapter = eventAdapter



        var firebaseClient= FirebaseClient(this)
        firebaseClient.getEventItems(firebaseIDKey!!, eventArtikelArray, eventAdapter!!)

        floatingButton = findViewById(R.id.addEventItemFabID)
        floatingButton!!.setOnClickListener {
            var alerts = Alerts(this)
            alerts.startEventItemAlert(firebaseIDKey!!)
        }
    }


    fun getData(){
        eventArtikelArray.add(EInkaufsItem("Pizza",3,"Diorit Bajrami","FIRID","UserID",false,"null"))
        eventArtikelArray.add(EInkaufsItem("Pizza",3,"Alex","FIRID","UserID",false,"null"))
        eventArtikelArray.add(EInkaufsItem("Pizza",3,"Anhal","FIRID","UserID",false,"null"))
        eventArtikelArray.add(EInkaufsItem("Pizza",3,"Tiroid","FIRID","UserID",false,"null"))
    }
}
