package com.example.deliveryapp.Cadastro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.ActivityLogoInicialBinding
import com.example.deliveryapp.databinding.LogoinicialBinding

class Logo_Inicial : AppCompatActivity() {
    lateinit var binding: LogoinicialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LogoinicialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())
        val delayMillis: Long = 3000 // Tempo em milissegundos (3 segundos)

        handler.postDelayed({
            // Intent para iniciar a próxima atividade (tela de login)
            val intent = Intent(this, Form_login::class.java)
            startActivity(intent)
            finish() // Encerra a atividade atual após iniciar a próxima
        }, delayMillis)





    }
}