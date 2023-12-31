package com.example.deliveryapp.Dialog

import android.app.Activity
import android.app.AlertDialog
import com.example.deliveryapp.R

class Dialogs(private val activity: Activity) {
    lateinit var dialog: AlertDialog


    fun iniciarDialog(){
        val builder = AlertDialog.Builder(activity)
        val layoutInflater = activity.layoutInflater
        builder.setView(layoutInflater.inflate(R.layout.dialog_carregamento,null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()

    }

 fun liberarDialog(){
     dialog.dismiss()
 }


}