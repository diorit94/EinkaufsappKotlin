package com.codefrog.dioritbajrami.einkaufsappkotlin.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.EhemaligeEinkaufsListe
import com.codefrog.dioritbajrami.einkaufsappkotlin.R

class EhemaligeEinkaeufe : AppCompatActivity() {

    var fragmentManager = supportFragmentManager
    var fragmentTransaction = fragmentManager.beginTransaction()

    var einkaufListeFragment = EhemaligeEinkaufsListe()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ehemalige_einkaeufe)

        fragmentTransaction.add(R.id.ehemalige_einkaeufe_frame, einkaufListeFragment)
    }
}
