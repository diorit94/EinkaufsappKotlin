package com.codefrog.dioritbajrami.einkaufsappkotlin

import android.animation.Animator
import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_logged_in.*

class CircularReveal(){

    fun getCircularReveal(relativeLayout: RelativeLayout,contentLayout: RelativeLayout, layoutMain: RelativeLayout){
        val x = contentLayout!!.right
        val y = contentLayout!!.bottom

        var startRadius: Int = 0
        var endRadius: Int = Math.hypot(layoutMain!!.width.toDouble(), layoutMain!!.height.toDouble()).toInt()

        var anim: Animator = ViewAnimationUtils.createCircularReveal(relativeLayout, x, y, startRadius.toFloat(), endRadius.toFloat())

        relativeLayout!!.visibility = View.VISIBLE
        anim.duration = 1000
        anim.start()
    }

}