package com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EinkaufsItemAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.LoggedIn
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem

import com.codefrog.dioritbajrami.einkaufsappkotlin.R

class Verwaltung_Fragment : Fragment() {


    var recyclerView: RecyclerView?=null

    var itemArray = ArrayList<EInkaufsItem>()
    var itemAdapter: EinkaufsItemAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_verwaltung_, container, false)

        recyclerView = view.findViewById(R.id.VerwaltungListView)
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

        itemAdapter = EinkaufsItemAdapter(context!!,itemArray)

        recyclerView!!.adapter = itemAdapter

        val firebaseClient = FirebaseClient(itemArray,itemAdapter!!)
        firebaseClient.getFirebaseData("Verwaltung")

        return view
    }
}
