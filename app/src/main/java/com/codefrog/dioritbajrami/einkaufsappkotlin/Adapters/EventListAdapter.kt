package com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.EventItemActivity
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.EventsListActivity
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EventModel
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import kotlin.coroutines.experimental.coroutineContext
import android.view.View.OnLongClickListener
import android.widget.RelativeLayout
import com.codefrog.dioritbajrami.einkaufsappkotlin.Alerts
import com.github.clans.fab.FloatingActionButton
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import java.text.SimpleDateFormat
import java.util.*


class EventListAdapter(var context: Context, var arrayEventList: ArrayList<EventModel>) : RecyclerView.Adapter<EventListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.event_item_row, parent, false)
        return EventListAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayEventList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        var event = arrayEventList[position]

        holder!!.eventName.text = event.name

        var formatter = SimpleDateFormat("dd. MMMM yyyy");
        var dateString = formatter.format(Date(event.date));

        holder!!.eventDate.text = dateString
        holder!!.eventTime.text = event.uhrzeit

        val colorBlue = holder!!.cardID.getContext().getResources().getColor(R.color.fabBlue)
        val colorYellow = holder!!.cardID.getContext().getResources().getColor(R.color.fabYellow)
        val colorRed = holder!!.cardID.getContext().getResources().getColor(R.color.fabRed)

        if (event.type == "Geburtstag") {
            holder!!.cardID.setCardBackgroundColor(colorBlue)
            holder.logoIcon.setImageResource(R.drawable.ic_cake_black_24dp)
        } else if (event.type == "Event Essen") {
            holder!!.cardID.setCardBackgroundColor(colorRed)
            holder.logoIcon.setImageResource(R.drawable.ic_local_dining_black_24dp)

        } else if (event.type == "Andere") {
            holder!!.cardID.setCardBackgroundColor(colorYellow)
            holder.logoIcon.setImageResource(R.drawable.ic_bookmark_border_black_24dp)
        }

        holder.cardID.setOnClickListener {
            var i = Intent(context, EventItemActivity::class.java)
            i.putExtra("FirID", event.firebaseID)
            i.putExtra("Type", event.type)
            i.putExtra("Title", event.name)
            context.startActivity(i)
        }


        holder.cardID.setOnLongClickListener(OnLongClickListener {

            showDialog(event, position)

            true
        })


    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val eventName = itemView.findViewById<TextView>(R.id.event_name_id)
        val eventDate = itemView.findViewById<TextView>(R.id.event_date_id)
        val eventTime = itemView.findViewById<TextView>(R.id.event_time_id)
        val logoIcon = itemView.findViewById<ImageView>(R.id.imageViewID)

        val cardID = itemView.findViewById<CardView>(R.id.cardViewID)
    }

    fun removeItem(position: Int) {
        arrayEventList.removeAt(position)
        notifyItemRemoved(position)
    }

    var fclient = FirebaseClient()

    fun showDialog(model: EventModel, position: Int) {
        if ((context as EventsListActivity).mAuth.currentUser!!.uid != "eIeqKuxSsxZekufpxEy4jmik8DA3") {
            return
        }
        var alerts = Alerts(context)

        val option = arrayOf("Bearbeiten", "Löschen")
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_selectable_list_item, option)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Aktion auswählen")
        builder.setAdapter(adapter, DialogInterface.OnClickListener { dialogInterface, i ->
            if (i == 0) {
                alerts.updateEvent(model)
            } else {
                startAlertEinkaufAbschliessen(model,position)
            }

        })
        val a = builder.create()
        a.show()
    }

    fun startAlertEinkaufAbschliessen(model:EventModel, position: Int) {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setMessage(model.name+" löschen?")
        builder.setPositiveButton("Ja", DialogInterface.OnClickListener { dialog, id ->
            fclient.deleteFirebase("Event", model.firebaseID)
            removeItem(position)
        })
        builder.setNegativeButton("Nein", DialogInterface.OnClickListener { dialog, id ->
            // User cancelled the dialog
        })
        builder.show()
    }




}