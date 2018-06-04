package com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.view.menu.MenuAdapter
import android.view.*
import android.widget.ListView
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.MainMenuAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.MenuItem

import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.*


class MenuFragment : Fragment() {

    var menuArray = ArrayList<MenuItem>()

    var menuAdapter: MainMenuAdapter?=null

    var menuListView: ListView?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        menuArray.clear()


        menuAdapter = MainMenuAdapter(context!!, menuArray)

        menuListView = view.findViewById(R.id.menuListView)
        menuListView!!.adapter = menuAdapter
        menuListView!!.divider = null

        getMenuItems()

        return view
    }

    fun getMenuItems(){
        menuArray.add(MenuItem("Einkaufsliste", R.mipmap.ic_add_shopping_cart_white_24dp))
        menuArray.add(MenuItem("Artikel Hinzu", R.mipmap.ic_playlist_add_white_24dp))
        menuArray.add(MenuItem("Einkauf Starten", R.mipmap.ic_shopping_cart_white_24dp))
        menuArray.add(MenuItem("Einkauf abschließen", R.mipmap.baseline_outlined_flag_white_24))
        menuArray.add(MenuItem("Empfehlungen", R.mipmap.ic_account_box_white_24dp))
    }

}
