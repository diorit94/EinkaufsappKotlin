package com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EhemaligeEinkaeufe
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import kotlinx.android.synthetic.main.ehemalige_einkaeufe_row.view.*
import android.os.Bundle
import java.text.SimpleDateFormat
import java.util.*


class EhemaligeEinkaeufeAdapter(var context: Context, var arrayList: ArrayList<EhemaligeEinkaeufe>) : BaseAdapter(){


    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        var view: View

        var ehemaligeEinkaufe = arrayList[position]

        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflator.inflate(R.layout.ehemalige_einkaeufe_row, null)


        var formatter = SimpleDateFormat("dd. MMMM yyyy");
        var dateString = formatter.format(Date(ehemaligeEinkaufe.date!!));

        view.row_dateID.text = dateString

        view.setOnClickListener {
            var cx = (context as com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.EhemaligeEinkaeufe)
            var bundle = Bundle()

            //var wrapper = EinkaufsItemWrapper(ehemaligeEinkaufe.einkaufsArrayList)

            bundle.putString("name_key", dateString)
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