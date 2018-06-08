package com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EhemaligeEinkaeufeAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EhemaligeEinkaeufe

import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import kotlinx.android.synthetic.main.fragment_ehemalige_einkaufs_liste.view.*

class EhemaligeEinkaufsListe : Fragment() {

    var arrayList = ArrayList<EhemaligeEinkaeufe>()
    var adapter: EhemaligeEinkaeufeAdapter?=null
    var listView: ListView?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_ehemalige_einkaufs_liste, container, false)

        adapter = EhemaligeEinkaeufeAdapter(activity!!,arrayList)
        listView = view.findViewById(R.id.ehemalige_einkaeufe_ListViewID)

        listView!!.adapter = adapter

        var fClient = FirebaseClient(adapter!!)
        fClient.getEhemaligeEinkaeufe(arrayList)

        return view
    }

}
