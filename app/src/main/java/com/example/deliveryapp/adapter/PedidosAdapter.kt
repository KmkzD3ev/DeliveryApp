package com.example.deliveryapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.Payment.Pedidos
import com.example.deliveryapp.databinding.PedidosItemBinding
import com.example.deliveryapp.model.Produto
import com.example.deliveryapp.model.ServidorDadosPedidos

class PedidosAdapter(val context: Context,val lista_pedidos:MutableList<ServidorDadosPedidos>):
    RecyclerView.Adapter<PedidosAdapter.PedidosviewHolder>() {


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidosviewHolder {
     val item_lista = PedidosItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return PedidosviewHolder(item_lista)
    }

    override fun getItemCount() = lista_pedidos.size



    override fun onBindViewHolder(holder: PedidosviewHolder, position: Int) {
       holder.textEndereco.text = lista_pedidos.get(position).endereco
        holder.textCelular.text = lista_pedidos.get(position).celular
        holder.textStatus_pagamento.text = lista_pedidos.get(position).status_pagamentos
        holder.textEntrega_Status.text = lista_pedidos.get(position).entregastatus
        val produtosText = buildProductsText(lista_pedidos[position].listaprodutos)
        holder.textListaProduto.text = produtosText

            }

    inner class PedidosviewHolder(binding: PedidosItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val textEndereco = binding.textEndereco
        val textCelular = binding.textCelular
        val textListaProduto = binding.textListaProduto
        val textStatus_pagamento = binding.textStatuspagamento
        val textEntrega_Status = binding.textEntregaStatus



    }



    private fun buildProductsText(produtos: List<Produto>): String {
        val produtosTextList = produtos.map { "${it.nome} - R$ ${it.preco}" }
        return produtosTextList.joinToString("\n")
    }


}