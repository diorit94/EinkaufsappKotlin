package com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.*
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EhemaligeEinkaeufe
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.Empfehlungen
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.main_list_row.view.*

class MainMenuAdapter : BaseAdapter {

    var menuArray = ArrayList<MenuItem>()
    var context: Context? = null


    constructor(context: Context, menuArray: ArrayList<MenuItem>) {
        this.context = context
        this.menuArray = menuArray
    }

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {

        var item = menuArray[position]
        var itemView: View

        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        itemView = inflator.inflate(R.layout.main_list_row, null)

        itemView.tittleRowID.text = item.name
        itemView.circleButtonId.setImageResource(item.image)

        itemView.circleButtonId.setOnClickListener {

            if (context is MainActivity) {

                if (position == 0) {
                    (context as MainActivity).changeIntent(1)
                } else if (position == 1) {
                    var alerts = Alerts(context!!)
                    alerts.startAlert()
                } else if (position == 2) {
                    (context as MainActivity).changeIntent(2)
                }else if (position == 3) {
                    //(context as MainActivity).firRef.child("Artikel").removeValue()
                    var key = (context as MainActivity).firRef.push().key
                    var arrayList = ArrayList<EInkaufsItem>()

                    arrayList.add(EInkaufsItem("Pizza",1,"verwaltung", "FIRID","UID",false))
                    arrayList.add(EInkaufsItem("Pizza",1,"verwaltung", "FIRID","UID",false))
                    arrayList.add(EInkaufsItem("Pizza",1,"verwaltung", "FIRID","UID",false))

                    (context as MainActivity).firRef.child("Ehemalige Einkaeufe").child(key).setValue(EhemaligeEinkaeufe("08.05.2018",arrayList))
                }else if (position == 4) {
                    var i = Intent(context, Empfehlung_activity::class.java)
                    context!!.startActivity(i)
                }
            }


        }

        return itemView
    }

    override fun getItem(p0: Int): Any {
        return menuArray[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return menuArray.size
    }


}