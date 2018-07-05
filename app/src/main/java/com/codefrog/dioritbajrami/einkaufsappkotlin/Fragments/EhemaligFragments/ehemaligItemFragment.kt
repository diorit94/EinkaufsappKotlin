package com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.EhemaligFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import android.widget.ListView
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EhemaligeEinkaufItemAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EhemaligeEinkaeufe
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EinkaufsItemWrapper
import java.text.SimpleDateFormat
import java.util.*


class ehemaligItemFragment : Fragment() {

    var listView: ListView?=null
    var adapter: EhemaligeEinkaufItemAdapter?=null
    var arrayEhemaligItemList = ArrayList<EInkaufsItem>()
    var titleString : String?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View
        view = inflater.inflate(R.layout.fragment_ehemalig_item, container, false)

        listView = view.findViewById(R.id.ehemaligeItemListViewID)

        val bundle = arguments
        if (bundle != null) {

            val keyString = bundle.getString("key_key")
            titleString = bundle.getString("name_key")

            //GET THE ARRAY
            //var wrap = bundle.getSerializable("obj") as EinkaufsItemWrapper


            adapter = EhemaligeEinkaufItemAdapter(context!!,arrayEhemaligItemList)
            listView!!.adapter = adapter


            //LOAD THE DATA
            val firebaseClient = FirebaseClient()
            firebaseClient.loadEhemaligItemData(keyString, arrayEhemaligItemList, adapter!!)
        }

        activity!!.setTitle(titleString)

        return view
    }

}
