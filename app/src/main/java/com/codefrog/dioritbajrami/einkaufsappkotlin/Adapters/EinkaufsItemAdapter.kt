package com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.LoggedIn
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import kotlinx.android.synthetic.main.einkauf_item_row.view.*

class EinkaufsItemAdapter: BaseAdapter{

    var einkaufsArray = ArrayList<EInkaufsItem>()
    var context: Context?=null

    constructor(context: Context, einkaufsArray: ArrayList<EInkaufsItem>){
        this.context = context
        this.einkaufsArray = einkaufsArray
    }

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        var itemView: View
        var einkaufsItem = einkaufsArray[position]

        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        itemView = inflator.inflate(R.layout.einkauf_item_row, null)

        itemView.artikelNameID.text = einkaufsItem.name
        itemView.artikelZahlID.text = einkaufsItem.anzahl.toString()

        if(einkaufsItem.verwaltung == "Verwaltung"){
            itemView.verwaltungID.text = "Verwaltung"
        }else if(einkaufsItem.verwaltung == "IT"){
            itemView.verwaltungID.text = "IT"
        }else if(einkaufsItem.verwaltung.contains("(Person)")){
            itemView.verwaltungID.text = einkaufsItem.verwaltung
        }


        var getContextCurrentUser = (context as LoggedIn).mAuth!!.currentUser!!.uid

        if(getContextCurrentUser == einkaufsItem.userID || getContextCurrentUser == "eIeqKuxSsxZekufpxEy4jmik8DA3"){
            itemView.deleteArtikelID.visibility = View.VISIBLE
        }else {
            itemView.deleteArtikelID.visibility = View.GONE
        }


        itemView.deleteArtikelID.setOnClickListener {
            if (context is LoggedIn) {
                if ((context as LoggedIn).mAuth!!.currentUser!!.uid == einkaufsItem.userID) {
                    (context as LoggedIn).firRef.child("Artikel").child(einkaufsItem.firebaseID).removeValue()
                } else if((context as LoggedIn).mAuth!!.currentUser!!.uid == "eIeqKuxSsxZekufpxEy4jmik8DA3"){
                    (context as LoggedIn).firRef.child("Artikel").child(einkaufsItem.firebaseID).removeValue()
                } else {
                    Toast.makeText(context, "Kannst es nicht löschen da du es selber nicht hinzugefügt hast", Toast.LENGTH_SHORT).show()
                }

                //SHOW THE SNACKBAR TO RETURN DELETION
                (context as LoggedIn).showSnackBar(einkaufsItem.name,einkaufsItem.anzahl, einkaufsItem.userID,einkaufsItem.verwaltung)

            } else {
                Toast.makeText(context, "Context Failure", Toast.LENGTH_SHORT).show()
            }
        }

        return itemView
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