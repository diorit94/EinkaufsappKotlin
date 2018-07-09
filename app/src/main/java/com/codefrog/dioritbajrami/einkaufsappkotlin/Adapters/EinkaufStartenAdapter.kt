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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class EinkaufStartenAdapter(var context: Context, var einkaufStartenList: ArrayList<EInkaufsItem>) : BaseAdapter(){

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val itemView: View

        val einkaufItem = einkaufStartenList[position]

        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        itemView = inflator.inflate(R.layout.einkauf_starten_row, null)

        itemView.anzahlStartID.text = einkaufItem.anzahl.toString() + "x"
        itemView.titleStartID.text = einkaufItem.name + ", " + einkaufItem.Type
        //itemView.verwaltungStartID.text = einkaufItem.verwaltung

        if(einkaufItem.bought == true){
            itemView.checkBoxStartID.isChecked = true
            itemView.coordinatorLayoutID.setBackgroundResource(R.color.grey)
        } else if(einkaufItem.bought == false){
            itemView.checkBoxStartID.isChecked = false
        }

        itemView.setOnClickListener {
            checkBoxhChange(einkaufItem.bought!!, einkaufItem.firebaseID!!)
            makeTrue(einkaufItem.name!!,einkaufItem.Type!!, einkaufItem.bought!!)
        }

        itemView.checkBoxStartID.setOnClickListener {
            checkBoxhChange(einkaufItem.bought!!, einkaufItem.firebaseID!!)
            makeTrue(einkaufItem.name!!,einkaufItem.Type!!, einkaufItem.bought!!)
        }

        return itemView
    }

    fun checkBoxhChange(bought: Boolean, id: String){
        if(bought == false){
            (context as EinkaufStarten).firRef.child("EinkaufsStarten").child(id).child("bought").setValue(true)

        }else if(bought == true){
            (context as EinkaufStarten).firRef.child("EinkaufsStarten").child(id).child("bought").setValue(false)

        }
    }

    fun makeTrue(name: String, type: String, bought: Boolean){
        (context as EinkaufStarten).firRef.child("Artikel").orderByChild("name").equalTo(name).addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(dataSnapShot: DataSnapshot?) {
                for(fireSnapShot in dataSnapShot!!.children){
                    val einheit = fireSnapShot.child("type").getValue(String::class.java)

                    if(einheit == type){

                        if(!bought){
                            fireSnapShot.ref.child("bought").setValue(true)
                        }else{
                            fireSnapShot.ref.child("bought").setValue(false)
                        }

                    }

                }
            }

            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
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