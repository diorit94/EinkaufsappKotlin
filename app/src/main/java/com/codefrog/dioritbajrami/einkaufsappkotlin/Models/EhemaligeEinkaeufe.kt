package com.codefrog.dioritbajrami.einkaufsappkotlin.Models

class EhemaligeEinkaeufe{

    var date: Long ?=null
    var key: String?=null
    var einkaufsMap = HashMap<String, EInkaufsItem>()

    //TO READ DATA
    constructor(date: Long, key: String){
        this.date = date
        this.key = key
    }

    //TO SAVE DATA
    constructor(date: Long, key: String, einkaufsMap: HashMap<String, EInkaufsItem>){
        this.date = date
        this.einkaufsMap = einkaufsMap
        this.key = key
    }

}