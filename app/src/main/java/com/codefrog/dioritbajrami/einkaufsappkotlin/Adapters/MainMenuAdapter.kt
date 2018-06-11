package com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.*
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.Empfehlung_activity
import com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.MainActivity
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EInkaufsItem
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.EhemaligeEinkaeufe
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.main_list_row.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainMenuAdapter(var context: Context, var menuArray: ArrayList<MenuItem>) : BaseAdapter() {


    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {

        val item = menuArray[position]
        val itemView: View

        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        itemView = inflator.inflate(R.layout.main_list_row, null)

        itemView.tittleRowID.text = item.name
        itemView.circleButtonId.setImageResource(item.image)

        itemView.circleButtonId.setOnClickListener {

            if (context is MainActivity) {

                if (position == 0) {
                    (context as MainActivity).changeIntent(1)
                } else if (position == 1) {
                    val alerts = Alerts(context)
                    alerts.startAlert()
                } else if (position == 2) {
                    (context as MainActivity).changeIntent(2)
                } else if (position == 3) {
                    val i = Intent(context, com.codefrog.dioritbajrami.einkaufsappkotlin.Activities.EhemaligeEinkaeufe::class.java)
                    context.startActivity(i)
                } else if (position == 4) {
                    val i = Intent(context, Empfehlung_activity::class.java)
                    context.startActivity(i)
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