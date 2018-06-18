package com.codefrog.dioritbajrami.einkaufsappkotlin.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EmpfehlungsAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.Empfehlungen
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import android.support.v7.widget.helper.ItemTouchHelper
import com.codefrog.dioritbajrami.einkaufsappkotlin.RecyclerItemTouchHelper



class Empfehlung_activity : AppCompatActivity(), RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    var recyclerView: RecyclerView?=null
    var empfehlungsArray = ArrayList<Empfehlungen>()
    var adapter: EmpfehlungsAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empfehlung_activity)

        recyclerView = findViewById(R.id.empgehlungListViewID)
        recyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        adapter = EmpfehlungsAdapter(empfehlungsArray, this)
        recyclerView!!.adapter = adapter

        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        var firebaseClient = FirebaseClient(empfehlungsArray, adapter!!)
        firebaseClient.getFirebaseEmpfehlungen()

    }

    /*fun getData(){
        empfehlungsArray.add(Empfehlungen("Diorit"))
        empfehlungsArray.add(Empfehlungen("Granit"))
        empfehlungsArray.add(Empfehlungen("Festina"))
        empfehlungsArray.add(Empfehlungen("Atifete"))

    }*/

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        val firebaseClient = FirebaseClient()
        if (viewHolder is EmpfehlungsAdapter.ViewHolder){
            var key : String = empfehlungsArray.get(viewHolder.adapterPosition).firebaseID
            firebaseClient.deleteFirebase("Empfehlung", key)

            adapter!!.removeItem(viewHolder.getAdapterPosition())
        }
    }

}
