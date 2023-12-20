package com.example.deliveryapp.view

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.ActivityPerfilUsuarioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView


class Perfil_Usuario : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    val UsuarioId = FirebaseAuth.getInstance().currentUser!!.uid
    lateinit var binding: ActivityPerfilUsuarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val perfilfoto = binding.perfilFoto
        val nomeLogado = binding.NomeConta
        val emailLogado = binding.EmailUsER
        val bteditarPerfil = binding.editarPerfil

        recuperarFotoUser(perfilfoto, this)

    }


    override fun onStart() {
        super.onStart()


      }


    fun recuperarFotoUser(perfilFoto : CircleImageView,context: Context ) {
        val documentReference = db.collection("fotos Usuarios").document(UsuarioId)
        documentReference.addSnapshotListener { documento, _ ->
            if (documento != null){
                Glide.with(context).load(documento.getString("foto")).placeholder(R.color.preto).into(perfilFoto)
                val nome = FirebaseAuth.getInstance().currentUser?.displayName
                val email = FirebaseAuth.getInstance().currentUser?.email
                binding.NomeConta.text = nome
                binding.EmailUsER.text = email


            }


        }



    }



}


