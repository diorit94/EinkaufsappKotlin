package com.codefrog.dioritbajrami.einkaufsappkotlin.Adapters

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.support.v4.view.ViewPager
import android.view.ViewGroup
import com.codefrog.dioritbajrami.einkaufsappkotlin.R.id.textlauncher
import android.widget.TextView
import com.codefrog.dioritbajrami.einkaufsappkotlin.R
import com.codefrog.dioritbajrami.einkaufsappkotlin.R.id.titlelauncher
import com.codefrog.dioritbajrami.einkaufsappkotlin.R.layout.viewpager_item


open class ViewPagerAdapter : PagerAdapter {

    var context: Context?=null
    var title: Array<String>? = null
    var titletext: Array<String>? = null
    var inflater: LayoutInflater? = null

    constructor(context: Context, title: Array<String>, titletext: Array<String>) {
        this.context = context
        this.title = title
        this.titletext = titletext
    }

    override fun getCount(): Int {
        return title!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        // Declare Variables
        val txttitle: TextView
        val txttext: TextView

        inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = inflater!!.inflate(R.layout.viewpager_item, container,
                false)

        // Locate the TextViews in viewpager_item.xml
        txttitle = itemView.findViewById(R.id.titlelauncher)
        txttext = itemView.findViewById(R.id.textlauncher)

        // Capture position and set to the TextViews
        txttitle.text = title!![position]
        txttext.text = titletext!![position]


        // Add viewpager_item.xml to ViewPager
        (container as ViewPager).addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // Remove viewpager_item.xml from ViewPager
        (container as ViewPager).removeView(`object` as RelativeLayout)

    }

}