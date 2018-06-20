package com.codefrog.dioritbajrami.einkaufsappkotlin.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters.EmpfehlungsAdapter
import com.codefrog.dioritbajrami.einkaufsappkotlin.FirebaseClient
import com.codefrog.dioritbajrami.einkaufsappkotlin.Models.Empfehlungen
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Toast
import com.codefrog.dioritbajrami.einkaufsappkotlin.RecyclerItemTouchHelper
import com.google.firebase.auth.FirebaseAuth


class Empfehlung_activity : AppCompatActivity(), RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    var recyclerView: RecyclerView? = null
    var empfehlungsArray = ArrayList<Empfehlungen>()
    var adapter: EmpfehlungsAdapter? = null
    var mAuth: FirebaseAuth? = null
    var cordinatorLayout: CoordinatorLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empfehlung_activity)

        cordinatorLayout = findViewById(R.id.cordinatorLayoutEmpfehlungen)

        recyclerView = findViewById(R.id.empgehlungListViewID)
        recyclerView!!.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        adapter = EmpfehlungsAdapter(empfehlungsArray, this)
        recyclerView!!.adapter = adapter

        val itemTouchHelperCallback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        var firebaseClient = FirebaseClient(empfehlungsArray, adapter!!)
        firebaseClient.getFirebaseEmpfehlungen()

        mAuth = FirebaseAuth.getInstance()

    }

    /*fun getData(){
        empfehlungsArray.add(Empfehlungen("Diorit"))
        empfehlungsArray.add(Empfehlungen("Granit"))
        empfehlungsArray.add(Empfehlungen("Festina"))
        empfehlungsArray.add(Empfehlungen("Atifete"))

    }*/

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        val firebaseClient = FirebaseClient()

        if (viewHolder is EmpfehlungsAdapter.ViewHolder) {
            var name = empfehlungsArray.get(viewHolder.adapterPosition).name
            var counter: Long = empfehlungsArray.get(viewHolder.adapterPosition).counter

            var key: String = empfehlungsArray.get(viewHolder.adapterPosition).firebaseID

            if (mAuth!!.currentUser!!.uid != "eIeqKuxSsxZekufpxEy4jmik8DA3") {
                Toast.makeText(this, "Nur der Administrator kan Artikel aus dieser Liste löschen.", Toast.LENGTH_LONG).show()
            } else {
                firebaseClient.deleteFirebase("Empfehlung", key)
                adapter!!.removeItem(viewHolder.getAdapterPosition())
                showSnackBar(name, counter)

            }

        }

    }

    fun showSnackBar(name: String, counter: Long) {
        val snackBar = Snackbar
                .make(cordinatorLayout!!, "$name wurde gelöscht!", Snackbar.LENGTH_LONG)
                .setAction("Rückgängig machen") {
                    var fClienT = FirebaseClient()
                    fClienT.saveEmpfehlung(name, counter)
                }

        snackBar.show()
    }

}
