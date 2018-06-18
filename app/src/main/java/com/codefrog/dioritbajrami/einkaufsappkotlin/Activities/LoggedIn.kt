package com.codefrog.dioritbajrami.einkaufsappkotlin.Activities

import android.animation.Animator
import android.app.Activity
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.RelativeLayout
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.*
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.CostompagerAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.MainFragments.IT_Fragment
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.MainFragments.Person_Fragment
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.MainFragments.Verwaltung_Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_logged_in.*
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EinkaufsItemAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import java.util.*
import android.content.ComponentName
import android.content.Context.ACTIVITY_SERVICE
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface


class LoggedIn : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var database = FirebaseDatabase.getInstance()
    var firRef = database.reference
    var pagerAdapter: CostompagerAdapter? = null
    var addItem: FloatingActionButton? = null

    var isNetwork: Boolean?=null

    var adapter: EinkaufsItemAdapter? = null

    var resultArray = ArrayList<EInkaufsItem>()
    var firebaseClient = FirebaseClient(resultArray)

    var relativeLayout: RelativeLayout?=null
    var contentLayout: RelativeLayout?=null
    var layoutMain: RelativeLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        //contentLayout = findViewById(R.id.contentLayoutID)
        layoutMain = findViewById<RelativeLayout>(R.id.lyoutMain)
        contentLayout = findViewById<RelativeLayout>(R.id.layoutContent)
        relativeLayout = findViewById<RelativeLayout>(R.id.circularBackGroundAnimationID)

        mAuth = FirebaseAuth.getInstance()

        addItem = findViewById(R.id.addItemFab)

        addItem!!.setOnClickListener {
            val alerts = Alerts(this)
            alerts.startAlert("")
            relativeLayout!!.visibility = View.GONE
        }

        addFragments()

        checkIfItEgzists()
    }

    //If it is empty start the Thread for the Animation
    fun startThread(){
        isNetwork = InternetConnection(this).isNetworkAvailable()

        val startTimer = Timer()
        startTimer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                        //viewMenu()
                        if(isNetwork!!){
                            var cr = CircularReveal()
                            cr.getCircularReveal(relativeLayout!!, contentLayout!!, layoutMain!!)
                        }
                }
            }
        }, 200)
    }

    //Check if the Database for Artikel is empty.
    fun checkIfItEgzists(){
        firRef.child("Artikel")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(!dataSnapshot.exists()){
                            startThread()
                        }
                    }

                    override fun onCancelled(p0: DatabaseError?) {
                    }
                })
    }

    fun addFragments() {
        pagerAdapter = CostompagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragments(IT_Fragment(), "IT")
        pagerAdapter!!.addFragments(Verwaltung_Fragment(), "Verwaltung")
        pagerAdapter!!.addFragments(Person_Fragment(), "Person")

        //adding pageradapter to viewpager
        costomViewPager!!.adapter = pagerAdapter
        //Now setting up viewpager with tablayout
        costomTabLayout.setupWithViewPager(costomViewPager)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    fun showSnackBar(title: String, anzahl: Long, userID: String, verwalter: String) {
        val snackBar = Snackbar
                .make(layoutContent!!, "$title wurde gelöscht!", Snackbar.LENGTH_LONG)
                .setAction("Rückgängig machen") {
                    val fireClient = FirebaseClient()
                    fireClient.saveFirebaseData(title, anzahl, userID, verwalter)
                }

        snackBar.show()
    }


    override fun onStart() {

        super.onStart()
        var isNetwork = InternetConnection(this).isNetworkAvailable()

        if (!isNetwork!!) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Kein Internet oder es besteht ein Netzwerk Problem. Bitte Internet anmachen und auf OK drücken. ")
            builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                recreate()
            })
            builder.show()
        }
    }

}
