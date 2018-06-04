package com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EinkaufsItemAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.LoggedIn
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem

import com.codefrog.dioritbajrami.einkaufsappkotlin.R

class Verwaltung_Fragment : Fragment() {


    var verwaltungListView: ListView?=null

    var itemArray = ArrayList<EInkaufsItem>()
    var itemAdapter: EinkaufsItemAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_verwaltung_, container, false)
        verwaltungListView = view.findViewById(R.id.VerwaltungListView)

        itemAdapter = EinkaufsItemAdapter(context!!,itemArray)
        verwaltungListView!!.adapter = itemAdapter

        var firebaseClient = FirebaseClient(itemArray,itemAdapter!!)
        firebaseClient.getFirebaseData("Verwaltung")

        val activity = activity as LoggedIn?
        var currentUserUID = activity!!.mAuth!!.currentUser!!.uid

        return view
    }
}
