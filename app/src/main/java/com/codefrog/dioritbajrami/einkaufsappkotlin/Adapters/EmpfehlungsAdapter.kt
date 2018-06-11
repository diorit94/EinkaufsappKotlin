package com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.Empfehlungen
import com.codefrog.dioritbajrami.einkaufsappkotlin.R

class EmpfehlungsAdapter(val empfehlungsArray: ArrayList<Empfehlungen>) : RecyclerView.Adapter<EmpfehlungsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.empfehlung_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return empfehlungsArray.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val empfehlung = empfehlungsArray[position]

        holder!!.textView.text = empfehlung.name

    }


    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val textView = itemView.findViewById(R.id.empfehlungTextID) as TextView
        val viewForeground = itemView.findViewById(R.id.view_foreground) as RelativeLayout
        val viewBackground = itemView.findViewById(R.id.view_background) as RelativeLayout

    }
}
