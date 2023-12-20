package com.example.deliveryapp.Payment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deliveryapp.Dialog.CarrinhoDialog
import com.example.deliveryapp.adapter.AdapterProdutos
import com.example.deliveryapp.databinding.ActivityPedidos2Binding
import com.example.deliveryapp.model.Produto
import com.example.deliveryapp.view.HomeTela
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.io.Serializable

class Pedidos() : AppCompatActivity() {


    lateinit var binding: ActivityPedidos2Binding
    lateinit var carrinhoDialog: CarrinhoDialog
    lateinit var carrinhoProdutos: CarrinhoProdutos
    lateinit var adapter: AdapterProdutos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidos2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val nomeUsuario = user?.displayName


        carrinhoProdutos = CarrinhoProdutos(this)
        val listaProdutos = carrinhoProdutos.recuperarItensCarrinho()

        adapter = AdapterProdutos(this, listaProdutos)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)


        /*binding.limparPedido.setOnClickListener {
            if (listaProdutos.isNotEmpty()) {

                for (item in listaProdutos) {
                    carrinhoProdutos.removerItemCarrinho(item)
                    adapter.notifyDataSetChanged() // Notify the adapter for each removal
                }
                val primeiroProduto = listaProdutos[0]
                val produto = Produto(primeiroProduto.foto, primeiroProduto.nome, primeiroProduto.preco, null)
                carrinhoProdutos.limparLista()

                Log.d("Pedidos", "Produto removido: $produto")
                Log.d("Pedidos", "Itens no carrinho após limpar: ${carrinhoProdutos.itens}")

                // Remove items one by one
                for (item in listaProdutos) {
                    carrinhoProdutos.removerItemCarrinho(item)
                    adapter.notifyDataSetChanged() // Notify the adapter for each removal
                }

                // Clear the list (optional if you want an empty list)
                listaProdutos.clear()

                mensagemRemoveItens()

            } else {
                // Trate o caso em que a lista de produtos está vazia
            }
        }

       */ binding.finalizarPedido.setOnClickListener {
            if (listaProdutos.isEmpty()) {
                Toast.makeText(this, "Lista de pedidos vazia!", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("Pedidos", "Lista de Pedidos: $listaProdutos")
                val intent = Intent(this, TelaPagamento::class.java)
                intent.putExtra("nomeUsuario", nomeUsuario)
                intent.putExtra("listaPedidos", listaProdutos as Serializable)
                intent.putExtra("nome", listaProdutos[0].nome) // Exemplo: usando o nome do primeiro produto
                intent.putExtra("preco", listaProdutos[0].preco) // Exemplo: usando o preço do primeiro produto
                startActivity(intent)
                carrinhoProdutos.limparLista()
                finish()
            }
        }


        binding.limparPedido.setOnClickListener {
            if (listaProdutos.isNotEmpty()) {
                val produtoRemovido = listaProdutos.removeAt(0)
                adapter.atualizarLista(listaProdutos)
                Toast.makeText(this, "produto removido${produtoRemovido}", Toast.LENGTH_SHORT)
                    .show()
                carrinhoProdutos.limparLista()
                adapter.atualizarLista(listaProdutos)

                if (carrinhoProdutos.itens.isEmpty() && listaProdutos.isEmpty()) {
                    val intent = Intent(this, HomeTela::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Carrinho de compras Vazio ", Toast.LENGTH_SHORT).show()


                }

            }
        }



            fun limparListaPedidos() {
                if (listaProdutos.isNotEmpty()) {
                    for (item in listaProdutos) {
                        carrinhoProdutos.removerItemCarrinho(item)
                    }
                    adapter.notifyDataSetChanged() // Notifica o adapter após remover todos os itens

                    listaProdutos.clear() // Limpa a lista de produtos
                    carrinhoDialog.atualizarListaProdutos(listaProdutos)
                } else {
                    // Trate o caso em que a lista de produtos está vazia
                }
            }


        }

        fun mensagemRemoveItens() {
            val mensagem = "Limpando lista de produtos"
            val snackbar = Snackbar.make(binding.root, mensagem, Snackbar.LENGTH_LONG)
            snackbar.setTextColor(Color.WHITE)
            snackbar.setBackgroundTint(Color.BLACK)
            snackbar.setAction("Ok") { snackbar.dismiss() }
            snackbar.show()
        }


    }



