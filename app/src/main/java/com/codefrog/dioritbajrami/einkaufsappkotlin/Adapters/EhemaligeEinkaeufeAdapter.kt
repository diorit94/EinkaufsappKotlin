package com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EhemaligeEinkaeufe
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import kotlinx.android.synthetic.main.ehemalige_einkaeufe_row.view.*
import android.os.Bundle
import android.R.attr.key
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.EhemaligeEinkaufsListe
import android.R.attr.key
import android.app.FragmentManager
import com.codefrog.dioritbajrami.einkaufsappkotlin.Fragments.ehemaligItemFragment
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EinkaufsItemWrapper


class EhemaligeEinkaeufeAdapter(var context: Context, var arrayList: ArrayList<EhemaligeEinkaeufe>) : BaseAdapter(){


    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        var view: View

        var ehemaligeEinkaufe = arrayList[position]

        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflator.inflate(R.layout.ehemalige_einkaeufe_row, null)


        view.row_dateID.text = ehemaligeEinkaufe.date

        Toast.makeText(context,ehemaligeEinkaufe.key, Toast.LENGTH_SHORT).show()

        view.setOnClickListener {
            var cx = (context as com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.EhemaligeEinkaeufe)
            var bundle = Bundle()

            //var wrapper = EinkaufsItemWrapper(ehemaligeEinkaufe.einkaufsArrayList)

            bundle.putString("name_key",ehemaligeEinkaufe.date)
            bundle.putString("key_key",ehemaligeEinkaufe.key)
            //bundle.putSerializable("obj",wrapper)

            cx.itemListFragment.arguments = bundle

            cx.changeFragment()
        }

        return view
    }

    override fun getItem(p0: Int): Any {
        return arrayList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }

}