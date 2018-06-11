package com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.EinkaufStarten
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import kotlinx.android.synthetic.main.einkauf_starten_row.view.*
import android.view.animation.AlphaAnimation
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient


class EinkaufStartenAdapter(var context: Context, var einkaufStartenList: ArrayList<EInkaufsItem>) : BaseAdapter(){

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val itemView: View

        val einkaufItem = einkaufStartenList[position]

        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        itemView = inflator.inflate(R.layout.einkauf_starten_row, null)

        itemView.anzahlStartID.text = einkaufItem.anzahl.toString()
        itemView.titleStartID.text = einkaufItem.name
        itemView.verwaltungStartID.text = einkaufItem.verwaltung

        if(einkaufItem.bought == true){
            itemView.checkBoxStartID.isChecked = true
            itemView.coordinatorLayoutID.setBackgroundResource(R.color.grey)
        } else if(einkaufItem.bought == false){
            itemView.checkBoxStartID.isChecked = false
        }

        itemView.setOnClickListener {
            checkBoxhChange(einkaufItem.bought, einkaufItem.firebaseID)
        }

        itemView.checkBoxStartID.setOnClickListener {
            checkBoxhChange(einkaufItem.bought, einkaufItem.firebaseID)
        }

        return itemView
    }
    var firebaseClient = FirebaseClient()

    fun checkBoxhChange(bought: Boolean, id: String){
        if(bought == false){
            (context as EinkaufStarten).firRef.child("Artikel").child(id).child("bought").setValue(true)
        }else if(bought == true){
            (context as EinkaufStarten).firRef.child("Artikel").child(id).child("bought").setValue(false)
        }
    }

    fun itemClickAnimation(view: View){
        val animation1 = AlphaAnimation(0.3f, 1.0f)
        animation1.duration = 4000
        view.startAnimation(animation1)

    }

    override fun getItem(p0: Int): Any {
        return einkaufStartenList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }


    override fun getCount(): Int {
        return einkaufStartenList.size
    }

}