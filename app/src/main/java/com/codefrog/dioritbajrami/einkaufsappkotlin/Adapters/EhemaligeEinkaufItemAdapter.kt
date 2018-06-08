package com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import kotlinx.android.synthetic.main.ehemalige_einkaufs_item_row.view.*

class EhemaligeEinkaufItemAdapter(var context: Context, var einkaufsArray: ArrayList<EInkaufsItem>) : BaseAdapter() {

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val view:View

        var einkaufsItem = einkaufsArray[position]

        val inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflator.inflate(R.layout.ehemalige_einkaufs_item_row, null)

        view.anzahlID.text = einkaufsItem.anzahl.toString()
        view.ehemalige_text_id.text = einkaufsItem.name

        return view
    }

    override fun getItem(p0: Int): Any {
        return einkaufsArray[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return einkaufsArray.size
    }

}