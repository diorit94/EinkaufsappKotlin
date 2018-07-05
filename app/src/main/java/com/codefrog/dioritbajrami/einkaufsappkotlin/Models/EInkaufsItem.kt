package com.codefrog.dioritbajrami.einkaufsappkotlin.Models

import java.io.Serializable
import android.R.attr.rating


class EInkaufsItem{

    var name: String ?=null
    var anzahl: Long?=null
    var verwaltung: String?=null
    var firebaseID: String?=null
    var userID: String?=null
    var bought: Boolean?=null
    var Type: String?=null

    constructor(name: String, anzahl: Long, verwaltung: String, firebaseID: String, userID: String, bought: Boolean, Type: String) : super() {
        this.name = name
        this.anzahl= anzahl
        this.verwaltung = verwaltung
        this.firebaseID =firebaseID
        this.userID = userID
        this.bought = bought
        this.Type = Type
    }

}