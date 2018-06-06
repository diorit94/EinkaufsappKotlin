package com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.codefrog.dioritbajrami.einkaufsappkotlin.R

class EhemaligeEinkaufsListe : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_ehemalige_einkaufs_liste, container, false)

        return view
    }
}
