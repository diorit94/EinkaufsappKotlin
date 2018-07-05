package com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import at.markushi.ui.CircleButton
import com.codefrog.dioritbajrami.einkaufsappkotlin.*
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.Empfehlung_activity
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.LoggedIn
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.MainActivity
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EhemaligeEinkaeufe
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Logger
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.main_list_row.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainMenuAdapter(var context: Context, var menuArray: ArrayList<MenuItem>) : RecyclerView.Adapter<MainMenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.main_list_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return menuArray.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        var menuItems = menuArray[position]

        holder!!.tittleRow.text = menuItems.name
        holder!!.itemResource.setImageResource(menuItems.image)

        var adminUID = "eIeqKuxSsxZekufpxEy4jmik8DA3"
        var currentUserUID = (context as MainActivity).mAuth!!.currentUser!!.uid

        if (position == 3) {
            if (currentUserUID != adminUID) {
                holder.itemResource.setColor(Color.GRAY)
            }
        }


        holder.itemResource.setOnClickListener {
            if (context is MainActivity) {

                if (position == 0) {
                    (context as MainActivity).changeIntent(1)
                } else if (position == 1) {
                    val alerts = Alerts(context)
                    alerts.startAlert("")
                } else if (position == 3) {
                    if (currentUserUID == adminUID) {
                        (context as MainActivity).changeIntent(2)
                    } else {
                        Toast.makeText(context, "Du hast leider keine Administrator Rechte", Toast.LENGTH_SHORT).show()
                    }
                } else if (position == 4) {
                    (context as MainActivity).changeIntent(3)
                } else if (position == 2) {
                    (context as MainActivity).changeIntent(4)
                } else if (position == 5) {
                    (context as MainActivity).changeIntent(5)
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tittleRow = itemView.findViewById(R.id.tittleRowID) as TextView
        val itemResource = itemView.findViewById(R.id.circleButtonId) as CircleButton
    }

    /*override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {

        val item = menuArray[position]
        val itemView: View

        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        itemView = inflator.inflate(R.layout.main_list_row, null)

        itemView.tittleRowID.text = item.name
        itemView.circleButtonId.setImageResource(item.image)

        var adminUID = "eIeqKuxSsxZekufpxEy4jmik8DA3"
        var currentUserUID = (context as MainActivity).mAuth!!.currentUser!!.uid

        if(position == 2){
            if(currentUserUID != adminUID){
                itemView.circleButtonId.setColor(Color.GRAY)
            }
        } else if(position == 4){
            if(currentUserUID != adminUID){
                itemView.circleButtonId.setColor(Color.GRAY)
            }
        }

        itemView.circleButtonId.setOnClickListener {

            if (context is MainActivity) {

                if (position == 0) {
                    (context as MainActivity).changeIntent(1)
                } else if (position == 1) {
                    val alerts = Alerts(context)
                    alerts.startAlert()
                } else if (position == 2) {
                    if(currentUserUID == adminUID){
                        (context as MainActivity).changeIntent(2)
                    }else {
                        Toast.makeText(context, "Du hast leider keine Administrator Rechte", Toast.LENGTH_SHORT).show()
                    }
                } else if (position == 3) {
                    (context as MainActivity).changeIntent(3)
                } else if (position == 4) {
                    if(currentUserUID == adminUID){
                        (context as MainActivity).changeIntent(4)
                    }else {
                        Toast.makeText(context, "Du hast leider keine Administrator Rechte", Toast.LENGTH_SHORT).show()
                    }
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
    }*/


}