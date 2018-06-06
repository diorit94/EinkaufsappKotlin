package com.codefrog.dioritbajrami.einkaufsappkotlin.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.CostompagerAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Alerts
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.IT_Fragment
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.Person_Fragment
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.Verwaltung_Fragment
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_logged_in.*


class LoggedIn : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null

    var database = FirebaseDatabase.getInstance()
    var firRef = database.reference

    var pagerAdapter: CostompagerAdapter? = null

    var addItem: FloatingActionButton? = null

    var coordinatorLayout:CoordinatorLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        mAuth = FirebaseAuth.getInstance()

        addItem = findViewById(R.id.addItemFab)

        addItem!!.setOnClickListener {
            val alerts = Alerts(this)
            alerts.startAlert()
        }

        coordinatorLayout = findViewById(R.id.main_content)

        addFragments()
    }


    fun addFragments() {
        pagerAdapter = CostompagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragments(IT_Fragment(), "IT")
        pagerAdapter!!.addFragments(Verwaltung_Fragment(), "Verwaltung")
        pagerAdapter!!.addFragments(Person_Fragment(), "Person")

        //adding pageradapter to viewpager
        costomViewPager.adapter = pagerAdapter
        //Now setting up viewpager with tablayout
        costomTabLayout.setupWithViewPager(costomViewPager)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    fun showSnackBar(title: String, anzahl: Long,userID:String, verwalter: String){
        val snackBar = Snackbar
                .make(coordinatorLayout!!,"$title wurde gelöscht!", Snackbar.LENGTH_LONG)
                .setAction("Rückgängig machen"){
                    val fireClient = FirebaseClient()
                    fireClient.saveFirebaseData(title,anzahl,userID,verwalter)
                }

        snackBar.show()
    }

}
