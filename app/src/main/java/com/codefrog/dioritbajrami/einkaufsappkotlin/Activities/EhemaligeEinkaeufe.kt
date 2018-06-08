package com.codefrog.dioritbajrami.einkaufsappkotlin.Activities

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.EhemaligFragments.EhemaligeEinkaufsListe
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.EhemaligFragments.ehemaligItemFragment
import com.codefrog.dioritbajrami.einkaufsappkotlin.R


class EhemaligeEinkaeufe : AppCompatActivity() {

    var fragmentManager = supportFragmentManager
    var fragmentTransaction = fragmentManager.beginTransaction()

    var itemListFragment = ehemaligItemFragment()
    var einkaufListeFragment = EhemaligeEinkaufsListe()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ehemalige_einkaeufe)

        fragmentTransaction.add(R.id.ehemalige_einkaeufe_frame, einkaufListeFragment)
        fragmentTransaction.commit()

    }

    fun changeFragment(){
        val fragmentTransaction1 = fragmentManager.beginTransaction()
        fragmentTransaction1.replace(R.id.ehemalige_einkaeufe_frame, itemListFragment).commit()
        fragmentTransaction1.addToBackStack(null)
    }


    override fun onBackPressed() {
        if(fragmentManager.backStackEntryCount>0){
            val fm = getSupportFragmentManager()
            fm.popBackStack()
        }else {
            super.onBackPressed()
        }
    }

}
