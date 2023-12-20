package com.example.deliveryapp.view


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.Cadastro.Form_login
import com.example.deliveryapp.Dialog.CarrinhoDialog
import com.example.deliveryapp.Dialog.Dialogs
import com.example.deliveryapp.Dialog.iNICIALdIALOG
import com.example.deliveryapp.Payment.CarrinhoProdutos
import com.example.deliveryapp.Payment.EntrEgasPedidos
import com.example.deliveryapp.Payment.Pedidos
import com.example.deliveryapp.R
import com.example.deliveryapp.RecyclerItemClickListener.RecyclerItemClickListener
import com.example.deliveryapp.adapter.AdapterProdutos
import com.example.deliveryapp.databinding.ActivityHomeTelaBinding
import com.example.deliveryapp.model.Produto
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeTela : AppCompatActivity(), RecyclerView.OnItemTouchListener {

    private lateinit var adapterProdutos: AdapterProdutos
    private val produtoList: MutableList<Produto> = mutableListOf()
    val produtosEncontrados: MutableList<Produto> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var carrinhoproduto: CarrinhoProdutos
    lateinit var binding: ActivityHomeTelaBinding
    private var db = FirebaseFirestore.getInstance()
    private lateinit var searchView: androidx.appcompat.widget.SearchView



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHomeTelaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)





        val menuPed = findViewById<ImageView>(R.id.MenuPed)
        menuPed.setOnClickListener {
            val popup = PopupMenu(this, it)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.menu_telahome , popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.perfil -> {
                        val goPerfil = Intent(this, Perfil_Usuario::class.java)
                        startActivity(goPerfil)
                        finish()
                        true
                    }

                    R.id.sairMenu -> {
                        FirebaseAuth.getInstance().signOut()
                        val BackLogin = Intent(this, Form_login::class.java)
                        startActivity(BackLogin)
                        finish()
                        true
                    }
                    R.id.verpedidosMenu -> {
                        val entregas = Intent(this, EntrEgasPedidos::class.java)
                        startActivity(entregas)
                        finish()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }








        carrinhoproduto = CarrinhoProdutos(this)
        val fab = binding.fab
        fab.setOnClickListener {
            exibirCarrinhoDialog()
        }


        recyclerView = binding.recycleProdutos
        adapterProdutos = AdapterProdutos(this, produtoList)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapterProdutos

        val recyclerView = binding.recycleProdutos

        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onItemClick(view: View?, position: Int) {


                    }

                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        // Lógica para o clique longo
                    }
                }
            )
        )




        db.collection("Produtos").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "Documento: ${document.id} => ${document.data}")
                    val foto = document.getString("foto")
                    val nome = document.getString("nome")
                    val precoString = document.getString("preco")
                    val descricao : String? = document.getString("descricao")

                    if (foto != null && nome != null && precoString != null) {
                        val precoPattern = "([0-9]+[,.][0-9]{1,2})".toRegex()
                        val matchResult = precoPattern.find(precoString)
                        val preco = matchResult?.value?.replace(",", ".")?.toDoubleOrNull()

                        if (preco != null) {
                            val produto = Produto(foto, nome, preco,descricao )
                            produtoList.add(produto)
                            Log.d(TAG, "Produto adicionado: $produto")
                        } else {
                            Log.d(
                                TAG,
                                "O valor do campo 'preco' não pôde ser convertido para um número."
                            )
                        }
                    } else {
                        Log.d(TAG, "Os dados obtidos do documento estão incompletos.")
                    }
                }

                Log.d(TAG, "Lista de produtos: $produtoList")
                adapterProdutos.notifyDataSetChanged()
            }




                val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val nomeUsuario = user?.displayName


        binding.NameUser.text = "Bem vindo , $nomeUsuario"
        searchView = findViewById(R.id.pesquisahome)

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchView.setBackgroundColor(ContextCompat.getColor(this, R.color.branco))
            } else {
                searchView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
            }
        }

       setupSearchView()
    }





    @SuppressLint("ResourceAsColor")
    private fun setupSearchView() {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Lógica ao enviar a consulta de pesquisa (se necessário)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Verifica se o texto não está vazio
                if (!newText.isNullOrEmpty()) {

                    Log.d("Pesquisa", "Texto da Pesquisa: $newText")

                    db.collection("Produtos")
                        .whereEqualTo("tag", newText)
                        .get()
                        .addOnSuccessListener { result ->
                            produtosEncontrados.clear()
                            for (document in result) {
                                // Lógica para processar os resultados da pesquisa
                                val nome = document.getString("nome")
                                if (nome != null) {
                                    val preco = document.getString("preco")
                                    val descricao = document.getString("descricao")
                                    val foto = document.getString("foto")
                                    Log.d("Pesquisa", "Produto encontrado - Nome: $nome, Preço: $preco, Descrição: $descricao")

                                    if (preco != null) {
                                        val precoDouble = preco.toDoubleOrNull()
                                        if (precoDouble != null && foto != null) {
                                            val produtoEncontrado = Produto(foto, nome, precoDouble, descricao)
                                            produtosEncontrados.add(produtoEncontrado)
                                        } else {
                                            Log.e("Pesquisa", "Erro ao converter o preço para Double ou a URL da imagem está vazia.")
                                        }
                                    } else {
                                        Log.e("Pesquisa", "Campo 'preco' não encontrado ou está vazio.")
                                    }

                                    adapterProdutos.atualizarLista(produtosEncontrados)
                                    adapterProdutos.notifyDataSetChanged()
                                } else {
                                    Log.e("Pesquisa", "Nome do produto não encontrado no documento.")
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Pesquisa", "Falha na pesquisa: $exception")
                        }

                } else {

                    Log.d("Pesquisa", "Texto da Pesquisa vazio. Exibindo todos os produtos.")
                    // Lógica para lidar com o caso em que o texto da pesquisa está vazio
                    // Por exemplo, exibir todos os produtos novamente
                }

                return true
            }
        })
    }




    private fun exibirCarrinhoDialog() {
        val carrinhoDialog = CarrinhoDialog(this, carrinhoproduto.itens, carrinhoproduto)
        carrinhoDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_telahome,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.perfil->{
                val goPerfil = Intent (this,Perfil_Usuario::class.java)
                startActivity(goPerfil)
                finish()


            }
          
         R.id.sairMenu->{
             FirebaseAuth.getInstance().signOut()
             val BackLogin = Intent (this,Form_login::class.java)
             startActivity(BackLogin)
             finish()

         }

            R.id.verpedidosMenu->{
                val entregas = Intent(this,EntrEgasPedidos::class.java)
                startActivity(entregas)
                finish()





            }




        }
                return super.onOptionsItemSelected(item)

    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        TODO("Not yet implemented")
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        TODO("Not yet implemented")
    }
}