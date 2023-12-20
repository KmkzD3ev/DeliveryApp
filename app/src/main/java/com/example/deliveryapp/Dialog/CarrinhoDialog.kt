package com.example.deliveryapp.Dialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliveryapp.Payment.CarrinhoProdutos
import com.example.deliveryapp.Payment.Pedidos
import com.example.deliveryapp.R
import com.example.deliveryapp.adapter.AdapterProdutos

import com.example.deliveryapp.databinding.ListaDialogShowBinding
import com.example.deliveryapp.model.Produto

class CarrinhoDialog(context: Context, private val produtos: List<Produto>, private val carrinhoProdutos: CarrinhoProdutos) : Dialog(context) {


    lateinit var binding: ListaDialogShowBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListaDialogShowBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Configuração do RecyclerView
        val layoutManager = LinearLayoutManager(context)
        binding.rvCarrinho.layoutManager = layoutManager
        val adapter = AdapterProdutos(context, carrinhoProdutos.itens)

        adapter.setLayoutResourceId(R.layout.items_car)

        // Use o mesmo adaptador que você usa em outras partes
        binding.rvCarrinho.adapter = adapter

        binding.btdialogPagar.setOnClickListener{
            val intent = Intent(context, Pedidos::class.java)
            context.startActivity(intent)
            fecharDialog()
        }

    }

    fun exibirMensagemProdutoAdicionado() {
        if (produtos.isNotEmpty()) {
            val firstProduct = produtos[0]
            val snackbarMessage =
                "Produto adicionado: ${firstProduct.nome} - Preço: R$ ${firstProduct.preco}"
            val snackbar = Snackbar.make(binding.root, snackbarMessage, Snackbar.LENGTH_INDEFINITE)

            snackbar.setAction("Fechar") { snackbar.dismiss() }
            snackbar.show()
        }
    }

    fun atualizarListaProdutos(novaListaProdutos: List<Produto>) {
        // Atualiza a lista na Dialog com a versão mais recente
        val adapter = AdapterProdutos(context, novaListaProdutos)
        adapter.atualizarLista(novaListaProdutos)
        binding.rvCarrinho.adapter = adapter
    }


    fun fecharDialog() {
        dismiss() // Isso fechará a dialog quando chamado
    }
    fun limparListaProdutos() {
        carrinhoProdutos.limparLista()
        atualizarListaProdutos(emptyList()) // Atualiza a lista na Dialog com uma lista vazia
        exibirMensagemListaLimpa() // Exibe uma mensagem para indicar que a lista foi limpa
    }

    private fun exibirMensagemListaLimpa() {
        val mensagem = "Lista de produtos limpa"
        val snackbar = Snackbar.make(binding.root, mensagem, Snackbar.LENGTH_SHORT)
        snackbar.setTextColor(Color.WHITE)
        snackbar.setBackgroundTint(Color.BLACK)
        snackbar.show()
    }



}




