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
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlin.collections.ArrayList


class LoggedIn : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var database = FirebaseDatabase.getInstance()
    var firRef = database.reference
    var pagerAdapter: CostompagerAdapter? = null
    var addItem: FloatingActionButton? = null

    var isNetwork: Boolean? = null

    var adapter: EinkaufsItemAdapter? = null

    var resultArray = ArrayList<EInkaufsItem>()

    var relativeLayout: RelativeLayout? = null
    var contentLayout: RelativeLayout? = null
    var layoutMain: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        //contentLayout = findViewById(R.id.contentLayoutID)
        layoutMain = findViewById(R.id.lyoutMain)
        contentLayout = findViewById(R.id.layoutContent)
        relativeLayout = findViewById(R.id.circularBackGroundAnimationID)

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
    fun startThread() {
        isNetwork = InternetConnection(this).isNetworkAvailable()

        val startTimer = Timer()
        startTimer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    //viewMenu()
                    if (isNetwork!!) {
                        var cr = CircularReveal()
                        cr.getCircularReveal(relativeLayout!!, contentLayout!!, layoutMain!!)
                    }
                }
            }
        }, 200)
    }

    //Check if the Database for Artikel is empty.
    fun checkIfItEgzists() {
        firRef.child("Artikel")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (!dataSnapshot.exists()) {
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
        costomTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                addItem!!.show()
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    fun showSnackBar(title: String, anzahl: Long, userID: String, verwalter: String, type: String) {
        val snackBar = Snackbar
                .make(layoutContent!!, "" + anzahl + "x $title, $type wurde gelöscht!", Snackbar.LENGTH_LONG)
                .setAction("Rückgängig machen") {
                    val fireClient = FirebaseClient()
                    fireClient.saveFirebaseData(title, anzahl, userID, verwalter, type)
                }

        snackBar.show()
    }


    override fun onStart() {

        super.onStart()
        var isNetwork = InternetConnection(this).isNetworkAvailable()

        if (!isNetwork!!) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.internet_network_problem)
            builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                recreate()
            })
            builder.show()
        }
    }


    //Hide floating button if you scroll on the bottom
    fun hideFloatingButton(recycler: RecyclerView, layoutManager: LinearLayoutManager) {
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {

        })
    }


}
