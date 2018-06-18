package com.codefrog.dioritbajrami.einkaufsappkotlin.Activities

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EinkaufsItemAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.MenuLoginFragments.LoginFragment
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.MenuLoginFragments.MenuFragment
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import com.codefrog.dioritbajrami.einkaufsappkotlin.RecyclerItemTouchHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.DialogInterface
import android.net.ConnectivityManager
import com.codefrog.dioritbajrami.einkaufsappkotlin.InternetConnection


class MainActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()!!

    var fragmentManager = supportFragmentManager
    var fragmentTransaction = fragmentManager.beginTransaction()
    var loginFragment = LoginFragment()
    var menuFragment = MenuFragment()
    var mAuth: FirebaseAuth? = null

    var database = FirebaseDatabase.getInstance()
    var firRef = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            fragmentTransaction.replace(R.id.content_frame, menuFragment)
        } else {
            fragmentTransaction.replace(R.id.content_frame, loginFragment)
        }
        fragmentTransaction.commit()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_logout -> {
                if (auth.currentUser != null) {
                    auth!!.signOut()

                    getSupportFragmentManager().beginTransaction().remove(menuFragment).commit();
                    var fragmentTransaction = fragmentManager!!.beginTransaction()
                    fragmentTransaction.replace(R.id.content_frame, loginFragment).commit()
                } else {
                    Toast.makeText(this, "Du bist schon abgemeldet", Toast.LENGTH_SHORT).show()
                }

            }
        }
        return true
    }


    fun changeIntent(choosenIntent: Int) {
        if (choosenIntent == 1) {
            var intent = Intent(this, LoggedIn::class.java)
            startActivity(intent)
        } else if (choosenIntent == 2) {
            var intent = Intent(this, EinkaufStarten::class.java)
            startActivity(intent)
        } else if (choosenIntent == 3) {
            val i = Intent(this, com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.EhemaligeEinkaeufe::class.java)
            startActivity(i)
        } else if (choosenIntent == 4) {
            val i = Intent(this, Empfehlung_activity::class.java)
            startActivity(i)
        }
    }


    override fun onStart() {
        super.onStart()

        var isNetwork = InternetConnection(this).isNetworkAvailable()

        if (!isNetwork) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Kein Internet oder es besteht ein Netzwerk Problem. Bitte Internet anmachen und auf OK drücken. ")
            builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                recreate()
            })
            builder.show()
        }
    }



}