package com.codefrog.dioritbajrami.einkaufsappkotlin.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.LoginFragment
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.MenuFragment
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


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
            fragmentTransaction.add(R.id.content_frame, menuFragment)
        } else {
            fragmentTransaction.add(R.id.content_frame, loginFragment)
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
        if (choosenIntent == 1){
            var intent = Intent(this, LoggedIn::class.java)
            startActivity(intent)
        } else if(choosenIntent == 2){
            var intent = Intent(this, EinkaufStarten::class.java)
            startActivity(intent)
        }
    }

}