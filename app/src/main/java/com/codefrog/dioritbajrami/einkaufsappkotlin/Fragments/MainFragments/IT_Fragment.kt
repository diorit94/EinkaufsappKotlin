package com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.MainFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.LoggedIn
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EinkaufsItemAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem

import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import com.codefrog.dioritbajrami.einkaufsappkotlin.RecyclerItemTouchHelper
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_logged_in.*

class IT_Fragment : Fragment()  {

    var recyclerView: RecyclerView?=null

    var itemArray = ArrayList<EInkaufsItem>()
    var itemAdapter: EinkaufsItemAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_it_, container, false)

        recyclerView = view.findViewById(R.id.ITListView)
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

        itemAdapter = EinkaufsItemAdapter(context!!,itemArray)

        recyclerView!!.adapter = itemAdapter


        val firebaseClient = FirebaseClient(itemArray,itemAdapter!!)
        firebaseClient.getFirebaseData("IT")
        //firebaseClient.refreshData()

        (activity as LoggedIn).hideFloatingButton(recyclerView!!)

        return view
    }

}
