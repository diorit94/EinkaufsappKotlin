package com.codefrog.dioritbajrami.einkaufsappkotlin.Activities

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toolbar
import com.codefrog.dioritbajrami.einkaufsappkotlin.*
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EmpfehlungsAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EventListAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EventModel
import com.github.clans.fab.FloatingActionMenu
import com.google.firebase.auth.FirebaseAuth


class EventsListActivity : AppCompatActivity() {


    var arrayEvent = ArrayList<EventModel>()
    var eventAdapter: EventListAdapter?=null
    var recyclerView: RecyclerView?=null

    var geburtstagButton : com.github.clans.fab.FloatingActionButton?=null
    var eventEssenButton : com.github.clans.fab.FloatingActionButton?=null
    var andereEssenButton : com.github.clans.fab.FloatingActionButton?=null
    var fabMenu : com.github.clans.fab.FloatingActionMenu?=null

    var alerts = Alerts(this)

    var shadowView: View?=null

    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_list)


        var toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.toolbarID)
        toolbar.title = "Events"
        toolbar.setTitleTextColor(applicationContext.getResources().getColor(R.color.white))

        recyclerView = findViewById(R.id.recyclerEventList)
        recyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        eventAdapter = EventListAdapter(this,arrayEvent)
        recyclerView!!.adapter = eventAdapter

        val firebaseClient = FirebaseClient()

        firebaseClient.getEventData(arrayEvent, eventAdapter!!)


        initialization()
        OnClickListeners()

    }

    fun initialization() {
        geburtstagButton = findViewById(R.id.geburtstagEventID)
        andereEssenButton = findViewById(R.id.andereEventID)
        eventEssenButton = findViewById(R.id.essenEventID)

        fabMenu = findViewById(R.id.floatingactionmenuID)
        fabMenu!!.setClosedOnTouchOutside(true)

        if(mAuth.currentUser!!.uid != getString(R.string.admin_id)){
            fabMenu!!.visibility = View.GONE
        }

    }

    fun OnClickListeners(){
        geburtstagButton!!.setOnClickListener {
            setTheme(R.style.GeburtstagStyle)
            alerts.startEventAlert(fabMenu!!, "Geburtstag")
        }
        andereEssenButton!!.setOnClickListener {
            setTheme(R.style.andereStyle)
            alerts.startEventAlert(fabMenu!!, "Andere")
        }
        eventEssenButton!!.setOnClickListener {
            setTheme(R.style.essenStyle)
            alerts.startEventAlert(fabMenu!!, "Event Essen")
        }
    }

}
