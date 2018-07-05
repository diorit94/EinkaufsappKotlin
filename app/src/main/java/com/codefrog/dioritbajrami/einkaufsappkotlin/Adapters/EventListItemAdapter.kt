package com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.EventItemActivity
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EventModel
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import com.google.firebase.auth.FirebaseAuth

class EventListItemAdapter(var context: Context, var arrayEventList: ArrayList<EInkaufsItem>): RecyclerView.Adapter<EventListItemAdapter.ViewHolder>(){

    var firebaseClient = FirebaseClient()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.event_item_list_row, parent, false)
        return EventListItemAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayEventList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        var event = arrayEventList[position]

        holder!!.eventArtikelName.text = event.name + " / " + event.Type
        holder.eventArtikelZahl.text = event.anzahl.toString() + "x"
        holder.eventPersonName.text = event.verwaltung

        holder.itemCardView.setOnClickListener {
            if(event.userID == firebaseClient.mAuth.currentUser!!.uid || firebaseClient.mAuth.currentUser!!.uid == context.getString(R.string.admin_id)){
                deleteDialog(event.name!!, event.firebaseID!!,position)
            }
        }
    }


    //Remove item from Array
    fun removeItem(position: Int){
        arrayEventList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun deleteDialog(eventName: String,firebaseID: String, position: Int){
        val builder = android.app.AlertDialog.Builder(context)
        builder.setMessage(eventName + " sicher Löschen?")
        builder.setPositiveButton("Ja", DialogInterface.OnClickListener { dialog, id ->
            firebaseClient.deleteEventList((context as EventItemActivity).firebaseIDKey!!, firebaseID)
            removeItem(position)
        })
        builder.setNegativeButton("Nein", DialogInterface.OnClickListener { dialog, id ->
            // User cancelled the dialog
        })
        builder.show()

    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val eventArtikelName = itemView.findViewById<TextView>(R.id.event_item_title_id)
        val eventArtikelZahl = itemView.findViewById<TextView>(R.id.event_item_anzahl_id)
        val eventPersonName= itemView.findViewById<TextView>(R.id.event_item_personName_id)
        val itemCardView = itemView.findViewById<CardView>(R.id.itemCardID)

    }

}