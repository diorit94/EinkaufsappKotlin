package com.codefrog.dioritbajrami.einkaufsappkotlin.Models

import java.io.Serializable

class EinkaufsItemWrapper: Serializable{

    val serialVersionID = 1L
    var einkaufsArrayShop = ArrayList<EInkaufsItem>()

    constructor(einkaufsArrayShop: ArrayList<EInkaufsItem>){
        this.einkaufsArrayShop = einkaufsArrayShop
    }

    fun getItemDetails(): ArrayList<EInkaufsItem>{
        return einkaufsArrayShop
    }

}