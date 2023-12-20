package com.example.deliveryapp.Payment

import android.content.Context
import android.content.SharedPreferences
import com.example.deliveryapp.model.Produto
import com.google.common.reflect.TypeToken
import com.google.gson.Gson




class CarrinhoProdutos(context: Context) {
    private val itensCarrinho: MutableList<Produto> = mutableListOf()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("carrinho_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()


    val itens: MutableList<Produto>
        get() = recuperarItensCarrinho()


    fun adcProduto(produto: Produto) {
        val carrinhoAtual = recuperarItensCarrinho()
        carrinhoAtual.add(produto)
        salvarItensCarrinho(carrinhoAtual)

    }

    fun removerItemCarrinho(produto: Produto) {
        val carrinhoAtual = recuperarItensCarrinho()
        carrinhoAtual.remove(produto)
        salvarItensCarrinho(carrinhoAtual)
        notifyItemRemoved(produto)

    }
    private fun notifyItemRemoved(produto: Produto) {
        val index = itensCarrinho.indexOf(produto)
        if (index != -1) {
            itensCarrinho.removeAt(index)
            listener?.onItemRemoved(index)
        }
    }


    interface OnItemRemovedListener {
        fun onItemRemoved(position: Int)
    }


    private var listener: OnItemRemovedListener? = null

    fun setOnItemRemovedListener(listener: OnItemRemovedListener) {
        this.listener = listener
    }


    fun limparLista() {
        val carrinhoAtual = recuperarItensCarrinho()
        carrinhoAtual.clear()
        salvarItensCarrinho(carrinhoAtual)


    }

    private fun salvarItensCarrinho(itens: List<Produto>) {
        val editor = sharedPreferences.edit()
        val jsonItens = gson.toJson(itens)
        editor.putString("carrinho", jsonItens)
        editor.apply()

    }


    fun recuperarItensCarrinho(): MutableList<Produto> {
        val jsonItens = sharedPreferences.getString("carrinho", "")
        val itemType = object : TypeToken<List<Produto>>() {}.type
        return gson.fromJson(jsonItens, itemType) ?: mutableListOf()

    }








}