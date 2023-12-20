package com.example.deliveryapp.Payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliveryapp.adapter.PedidosAdapter
import com.example.deliveryapp.databinding.ActivityEntregasPedidosBinding
import com.example.deliveryapp.model.ServidorDadosPedidos
import com.example.deliveryapp.model.database
import com.example.deliveryapp.view.HomeTela
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.logging.Log

class EntrEgasPedidos : AppCompatActivity() {
    lateinit var binding: ActivityEntregasPedidosBinding
    lateinit var pedidosAdapter : PedidosAdapter
    var listapedidos :MutableList<ServidorDadosPedidos> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntregasPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val entregas_Recyle = binding.entregasRecycle
        entregas_Recyle.layoutManager = LinearLayoutManager(this)
        pedidosAdapter = PedidosAdapter(this,listapedidos)
        entregas_Recyle.setHasFixedSize(true)
        entregas_Recyle.adapter= pedidosAdapter

        val database = database()
        database.obterListaPedidos(listapedidos,pedidosAdapter)


    }
    override fun onBackPressed() {
        // Navega de volta para a tela inicial (Home)
        val intent = Intent(this, HomeTela::class.java)
        startActivity(intent)
        finish() // Opcional: encerra esta atividade (EntregasPedidos)
    }
}