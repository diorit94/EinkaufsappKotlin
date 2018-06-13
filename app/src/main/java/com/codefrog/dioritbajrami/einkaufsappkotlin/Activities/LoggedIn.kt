package com.codefrog.dioritbajrami.einkaufsappkotlin.Activities

import android.animation.Animator
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
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.CostompagerAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Alerts
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.MainFragments.IT_Fragment
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.MainFragments.Person_Fragment
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.MainFragments.Verwaltung_Fragment
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_logged_in.*
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EinkaufsItemAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.CircularReveal
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import java.util.*


class LoggedIn : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var database = FirebaseDatabase.getInstance()
    var firRef = database.reference
    var pagerAdapter: CostompagerAdapter? = null
    var addItem: FloatingActionButton? = null


    var adapter: EinkaufsItemAdapter? = null

    var resultArray = ArrayList<EInkaufsItem>()
    var firebaseClient = FirebaseClient(resultArray)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        //contentLayout = findViewById(R.id.contentLayoutID)
        var layoutMain = findViewById<RelativeLayout>(R.id.lyoutMain)
        var contentLayout = findViewById<RelativeLayout>(R.id.layoutContent)
        var relativeLayout = findViewById<RelativeLayout>(R.id.circularBackGroundAnimationID)

        mAuth = FirebaseAuth.getInstance()

        addItem = findViewById(R.id.addItemFab)

        addItem!!.setOnClickListener {
            val alerts = Alerts(this)
            alerts.startAlert()
            relativeLayout!!.visibility = View.GONE
        }

        addFragments()

        firebaseClient.getFirebaseData("All")


        val startTimer = Timer()
        startTimer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (resultArray.size == 0) {
                        //viewMenu()
                        var cr = CircularReveal()
                        cr.getCircularReveal(relativeLayout, contentLayout,layoutMain)
                    }
                }
            }
        }, 500)

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


    /*fun viewMenu() {
        val x = layoutContent!!.right
        val y = layoutContent!!.bottom

        var startRadius: Int = 0
        var endRadius: Int = Math.hypot(layoutMain!!.width.toDouble(), layoutMain!!.height.toDouble()).toInt()

        var anim: Animator = ViewAnimationUtils.createCircularReveal(relativeLayout, x, y, startRadius.toFloat(), endRadius.toFloat())

        relativeLayout!!.visibility = View.VISIBLE
        anim.duration = 1000
        anim.start()
    }*/

}
