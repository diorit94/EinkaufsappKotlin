package com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.LoggedIn
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import kotlinx.android.synthetic.main.einkauf_item_row.view.*

class EinkaufsItemAdapter(val context: Context,val einkaufsArray: ArrayList<EInkaufsItem>) : RecyclerView.Adapter<EinkaufsItemAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.einkauf_item_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return einkaufsArray.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val einkaufsItem = einkaufsArray[position]

        holder!!.artikelName.text = einkaufsItem.name
        holder.artikelZahl.text = einkaufsItem.anzahl.toString()

        if(einkaufsItem.verwaltung == "Verwaltung"){
            holder.artikelVerwaltung.text = "Verwaltung"
        }else if(einkaufsItem.verwaltung == "IT"){
            holder.artikelVerwaltung.text = "IT"
        }else if(einkaufsItem.verwaltung.contains("(Person)")){
            holder.artikelVerwaltung.text = einkaufsItem.verwaltung
        }


        val getContextCurrentUser = (context as LoggedIn).mAuth!!.currentUser!!.uid

        if(getContextCurrentUser == einkaufsItem.userID || getContextCurrentUser == "eIeqKuxSsxZekufpxEy4jmik8DA3"){
            holder.artikelButton.visibility = View.VISIBLE
        }else {
            holder.artikelButton.visibility = View.GONE
        }

        holder.artikelButton.setOnClickListener {
                if ((context).mAuth!!.currentUser!!.uid == einkaufsItem.userID) {
                    (context).firRef.child("Artikel").child(einkaufsItem.firebaseID).removeValue()
                } else if((context).mAuth!!.currentUser!!.uid == "eIeqKuxSsxZekufpxEy4jmik8DA3"){
                    (context).firRef.child("Artikel").child(einkaufsItem.firebaseID).removeValue()
                } else {
                    Toast.makeText(context, "Kannst es nicht löschen da du es selber nicht hinzugefügt hast", Toast.LENGTH_SHORT).show()
                }

                //SHOW THE SNACKBAR TO RETURN DELETION
                (context).showSnackBar(einkaufsItem.name,einkaufsItem.anzahl, einkaufsItem.userID,einkaufsItem.verwaltung)


        }


    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val artikelName = itemView.findViewById<TextView>(R.id.artikelNameID)
        val artikelZahl = itemView.findViewById<TextView>(R.id.artikelZahlID)
        val artikelVerwaltung = itemView.findViewById<TextView>(R.id.verwaltungID)
        val artikelButton = itemView.findViewById<Button>(R.id.deleteArtikelID)
    }

}