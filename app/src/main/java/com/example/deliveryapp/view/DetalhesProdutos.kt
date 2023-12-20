package com.example.deliveryapp.view
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.deliveryapp.Dialog.CarrinhoDialog
import com.example.deliveryapp.Payment.CarrinhoProdutos
import com.example.deliveryapp.adapter.AdapterProdutos
import com.example.deliveryapp.databinding.ActivityDetalhesProdutosBinding
import com.example.deliveryapp.model.Produto

import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

class DetalhesProdutos : AppCompatActivity() {
    lateinit var binding: ActivityDetalhesProdutosBinding
    lateinit var carrinhoproduto: CarrinhoProdutos
    lateinit var adapter: AdapterProdutos
    var quantidade = 1



    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)
         carrinhoproduto = CarrinhoProdutos(this)


        val foto = intent.extras?.getString("foto")
            val nome = intent.extras?.getString("nome")
            val descricao = intent.extras?.getString("descricao")
            val preco = intent.extras?.getDouble("preco")
        var newPreco = preco

        val listaProdutos = carrinhoproduto.recuperarItensCarrinho()
        adapter = AdapterProdutos(this, listaProdutos)

       //
            Glide.with(this).load(foto).into(binding.detlahesfoto)
            binding.dtNome.text = nome
            binding.dtDescricao.text = descricao
            val precoFormatado = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(preco)
            binding.dtPreco.text = precoFormatado

            binding.AdcCar.setOnClickListener {
                val produto = Produto(foto, nome, preco, null) // O campo de foto está vazio
                carrinhoproduto.adcProduto(produto)
               var  carrinhoDialog = CarrinhoDialog(this, carrinhoproduto.itens, carrinhoproduto)
                 exibirMensagemProdutoAdicionado()

            }


         binding





        fun removerProduto(produto: Produto) {
            // Remove o produto da lista
            listaProdutos.remove(produto)

            // Atualiza o adapter após a remoção do produto
            adapter.atualizarLista(listaProdutos)
        }





    }
    private fun exibirMensagemProdutoAdicionado() {
        if (carrinhoproduto.itens.isNotEmpty()) {
            val firstProduct = carrinhoproduto.itens.last()
            val snackbarMessage = "Produto adicionado: ${firstProduct.nome} - Preço: R$ ${firstProduct.preco}"
            val snackbar = Snackbar.make(binding.root, snackbarMessage, Snackbar.LENGTH_INDEFINITE)
            snackbar.setTextColor(Color.BLACK)
            snackbar.setBackgroundTint(Color.GRAY)
            snackbar.setAction("Ok") { snackbar.dismiss() }
            snackbar.show()
        }
    }



    private fun exibirMensagemProdutoRemovido() {
        if (carrinhoproduto.itens.isNotEmpty()) {
            val firstProduct = carrinhoproduto.itens.last()
            val snackbarMessage =
                "Produto removido : ${firstProduct.nome} - Preço: R$ ${firstProduct.preco}"
            val snackbar = Snackbar.make(binding.root, snackbarMessage, Snackbar.LENGTH_INDEFINITE)
            snackbar.setTextColor(Color.BLACK)
            snackbar.setBackgroundTint(Color.YELLOW)
            snackbar.setAction("Ok") { snackbar.dismiss() }
            snackbar.show()

        }
    }
}
