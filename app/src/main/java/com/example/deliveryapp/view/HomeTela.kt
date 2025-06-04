package com.example.deliveryapp.view

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deliveryapp.Cadastro.Form_login
import com.example.deliveryapp.Dialog.CarrinhoDialog
import com.example.deliveryapp.Payment.CarrinhoProdutos
import com.example.deliveryapp.Payment.EntrEgasPedidos
import com.example.deliveryapp.R
import com.example.deliveryapp.RecyclerItemClickListener.RecyclerItemClickListener
import com.example.deliveryapp.adapter.AdapterProdutos
import com.example.deliveryapp.databinding.ActivityHomeTelaBinding
import com.example.deliveryapp.model.Produto
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// >>> NOVOS IMPORTS PARA O MVVM <<<
import androidx.lifecycle.ViewModelProvider
import com.example.deliveryapp.data.ProdutoRepository // Certifique-se de que este caminho está correto
import com.example.deliveryapp.viewmodel.HomeUiState // Certifique-se de que este caminho está correto
import com.example.deliveryapp.viewmodel.HomeViewModel // Certifique-se de que este caminho está correto
import com.example.deliveryapp.viewmodel.HomeViewModelFactory // Certifique-se de que este caminho está correto
// <<< FIM DOS NOVOS IMPORTS >>>

class HomeTela : AppCompatActivity(), RecyclerView.OnItemTouchListener {

    private lateinit var adapterProdutos: AdapterProdutos
    private val produtoList: MutableList<Produto> = mutableListOf()
    // val produtosEncontrados: MutableList<Produto> = mutableListOf() // >>> PODE SER REMOVIDO OU REAVALIADO, POIS O ViewModel GERENCIA A LISTA AGORA <<<
    private lateinit var recyclerView: RecyclerView
    private lateinit var carrinhoproduto: CarrinhoProdutos
    lateinit var binding: ActivityHomeTelaBinding
    private var db = FirebaseFirestore.getInstance() // Mantenha o db se precisar para outras operações que não sejam do ViewModel
    private lateinit var searchView: androidx.appcompat.widget.SearchView

    // >>> NOVO: Instância do ViewModel <<<
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHomeTelaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // >>> NOVO: Inicialização do ViewModel <<<
        val firestoreDb = FirebaseFirestore.getInstance()
        val repository = ProdutoRepository(firestoreDb)
        val factory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        // <<< FIM DA INICIALIZAÇÃO DO ViewModel >>>

        // >>> NOVO: Observador do estado da UI do ViewModel <<<
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is HomeUiState.Loading -> {
                    // Mostrar um spinner de carregamento ou ProgressBar
                    binding.progressBar.visibility = View.VISIBLE //
                    binding.recycleProdutos.visibility = View.GONE
                    binding.txtStatus.visibility = View.GONE
                }
                is HomeUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recycleProdutos.visibility = View.VISIBLE
                    binding.txtStatus.visibility = View.GONE
                    produtoList.clear()
                    produtoList.addAll(state.produtos) //
                    adapterProdutos.notifyDataSetChanged()
                }
                is HomeUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recycleProdutos.visibility = View.GONE
                    binding.txtStatus.visibility = View.VISIBLE
                    binding.txtStatus.text = state.message //
                    Log.e(TAG, "Erro na UI: ${state.message}")
                }
                is HomeUiState.Empty -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recycleProdutos.visibility = View.GONE
                    binding.txtStatus.visibility = View.VISIBLE
                    binding.txtStatus.text = "Nenhum produto encontrado." //
                }
            }
        }
        // <<< FIM DO OBSERVADOR DO ViewModel >>>

        binding.navView.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_dashboard -> {
                        exibirCarrinhoDialog()
                        return true
                    }
                    R.id.navigation_home -> { // Ícone de Pessoa (Perfil) -> Ir para Perfil_Usuario
                        val goPerfil = Intent(this@HomeTela, Perfil_Usuario::class.java)
                        startActivity(goPerfil)
                        return true
                    }
                    R.id.navigation_notifications -> { // Ícone de Pedidos -> Ir para EntrEgasPedidos
                        val entregas = Intent(this@HomeTela, EntrEgasPedidos::class.java)
                        startActivity(entregas)
                        return true
                    }
                }
                return false
            }
        })

        carrinhoproduto = CarrinhoProdutos(this)
        val fab = binding.fab
        fab.setOnClickListener {
            exibirCarrinhoDialog()
        }

        recyclerView = binding.recycleProdutos
        adapterProdutos = AdapterProdutos(this, produtoList)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapterProdutos

        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onItemClick(view: View?, position: Int) {
                        // Lógica para clique no item do produto (se houver)
                    }

                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        // TODO("Not yet implemented") // Remover ou implementar
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        // Lógica para o clique longo
                    }
                }
            )
        )

        // >>> REMOVIDO: Lógica antiga de carregamento de produtos do Firestore.
        //     Agora o ViewModel é responsável por isso (chamado no init do ViewModel).
        /*
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
        */
        // <<< FIM DA REMOÇÃO >>>


        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val nomeUsuario = user?.displayName
        binding.NameUser.text = "Bem vindo , $nomeUsuario"

        binding.root.post { // Usa post para garantir que o layout já foi desenhado
            hideKeyboard(binding.root)
        }

        searchView = binding.pesquisahome
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchView.setBackgroundColor(ContextCompat.getColor(this, R.color.branco))
            } else {
                searchView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
            }
        }
        setupSearchView()
    }


    private fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @SuppressLint("ResourceAsColor")
    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.pesquisarProdutos(it) // >>> MODIFICADO: Chama o ViewModel <<<
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    Log.d("Pesquisa", "Texto da Pesquisa: $newText")
                    viewModel.pesquisarProdutos(newText) // >>> MODIFICADO: Chama o ViewModel <<<
                } else {
                    Log.d("Pesquisa", "Texto da Pesquisa vazio. Exibindo todos os produtos.")
                    viewModel.carregarTodosProdutos() // >>> MODIFICADO: Chama o ViewModel para recarregar todos <<<
                }
                // >>> REMOVIDA: Lógica antiga de busca do Firestore e atualização do adapter
                /*
                db.collection("Produtos")
                    .whereEqualTo("tag", newText)
                    .get()
                    .addOnSuccessListener { result ->
                        produtosEncontrados.clear()
                        for (document in result) {
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
                */
                // <<< FIM DA REMOÇÃO DA LÓGICA ANTIGA DE BUSCA <<<
                return true
            }
        })
    }

    private fun exibirCarrinhoDialog() {
        val carrinhoDialog = CarrinhoDialog(this, carrinhoproduto.itens, carrinhoproduto)
        carrinhoDialog.show()
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        // No need to implement unless you have custom touch handling
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        // No need to implement unless you have custom touch handling
    }
}