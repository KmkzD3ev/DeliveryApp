package com.example.deliveryapp.Dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.ImageView
import com.example.deliveryapp.R

class iNICIALdIALOG(private val activity: Activity) {
    lateinit var dialog: AlertDialog

    @SuppressLint("MissingInflatedId")
    fun exibirDialogImagem(){
        val builder = AlertDialog.Builder(activity)
        val inflater = LayoutInflater.from(activity)
        val dialogView = inflater.inflate(R.layout.logoinicial, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.inicialDilaog)

        // Aqui você pode configurar a imagem que deseja exibir no imageView
        // Por exemplo:
         imageView.setImageResource(R.drawable.logoatualizada___copia)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    fun fecharDialog(){
        dialog.dismiss()
    }
}







