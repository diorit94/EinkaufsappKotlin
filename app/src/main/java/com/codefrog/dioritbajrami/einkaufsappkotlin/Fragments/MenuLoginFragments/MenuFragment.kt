package com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.MenuLoginFragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.view.menu.MenuAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.LinearLayout
import android.widget.ListView
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.MainMenuAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.MenuItem

import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.*


class MenuFragment : Fragment() {

    var menuArray = ArrayList<MenuItem>()

    var menuAdapter: MainMenuAdapter?=null

    var menuListView: RecyclerView?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        menuArray.clear()

        menuAdapter = MainMenuAdapter(context!!, menuArray)
        menuListView = view.findViewById(R.id.menuRecyclerView)

        menuListView!!.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        menuListView!!.adapter = menuAdapter

        getMenuItems()

        return view
    }

    fun getMenuItems(){
        menuArray.add(MenuItem("Einkaufsliste", R.mipmap.ic_add_shopping_cart_white_24dp))
        menuArray.add(MenuItem("Artikel hinzu", R.mipmap.ic_playlist_add_white_24dp))
        menuArray.add(MenuItem("Wunschliste", R.mipmap.outline_star_border_white_24))
        menuArray.add(MenuItem("Einkauf starten", R.mipmap.ic_shopping_cart_white_24dp))
        menuArray.add(MenuItem("Ehemalige Einkäufe", R.mipmap.ic_calendar_check_white_24dp))
    }

}
