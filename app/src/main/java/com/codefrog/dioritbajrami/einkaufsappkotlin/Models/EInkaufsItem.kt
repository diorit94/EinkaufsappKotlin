package com.codefrog.dioritbajrami.einkaufsappkotlin.Models

import java.io.Serializable

data class EInkaufsItem(var name: String, var anzahl: Long, var verwaltung: String, var firebaseID: String, var userID: String, var bought: Boolean){
}