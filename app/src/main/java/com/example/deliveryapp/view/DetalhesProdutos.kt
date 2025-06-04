package com.example.deliveryapp.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.deliveryapp.Dialog.CarrinhoDialog
import com.example.deliveryapp.Payment.CarrinhoProdutos // Ainda precisamos de CarrinhoProdutos para CarrinhoDialog
import com.example.deliveryapp.adapter.AdapterProdutos
import com.example.deliveryapp.databinding.ActivityDetalhesProdutosBinding
import com.example.deliveryapp.model.Produto

import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

// >>> NOVOS IMPORTS PARA O MVVM <<<
import androidx.lifecycle.ViewModelProvider
import com.example.deliveryapp.data.CarrinhoRepository // Importe o novo CarrinhoRepository
import com.example.deliveryapp.viewmodel.DetalhesProdutosUiState // Importe o UiState específico
import com.example.deliveryapp.viewmodel.DetalhesProdutosViewModel // Importe o ViewModel específico
import com.example.deliveryapp.viewmodel.DetalhesProdutosViewModelFactory // Importe a Factory específica
// <<< FIM DOS NOVOS IMPORTS >>>


class DetalhesProdutos : AppCompatActivity() {
    lateinit var binding: ActivityDetalhesProdutosBinding
    // lateinit var carrinhoproduto: CarrinhoProdutos // >>> REMOVIDO: Será gerenciado pelo Repository/ViewModel ou passado para o Dialog <<<
    lateinit var adapter: AdapterProdutos // >>> REMOVIDO/REAVALIADO: Este adapter não parece ser usado diretamente nesta Activity para RecyclerView <<<
    var quantidade = 1 // >>> REMOVIDO/REAVALIADO: Esta variável não é usada no seu código atual, e a lógica de quantidade deveria estar no ViewModel/Carrinho <<<

    // >>> NOVO: Instância do ViewModel <<<
    private lateinit var viewModel: DetalhesProdutosViewModel

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // >>> NOVO: Inicialização do ViewModel <<<
        // O CarrinhoRepository precisa de Context, então passamos 'this' (a Activity)
        val carrinhoRepository = CarrinhoRepository(this)
        val factory = DetalhesProdutosViewModelFactory(this) // A Factory precisa do Context para criar o Repository
        viewModel = ViewModelProvider(this, factory)[DetalhesProdutosViewModel::class.java]
        // <<< FIM DA INICIALIZAÇÃO DO ViewModel >>>

        // Recupera os detalhes do produto da Intent
        val foto = intent.extras?.getString("foto")
        val nome = intent.extras?.getString("nome")
        val descricao = intent.extras?.getString("descricao")
        val preco = intent.extras?.getDouble("preco")

        // Cria o objeto Produto para passar ao ViewModel
        val produtoCarregado = if (foto != null && nome != null && preco != null) {
            Produto(foto, nome, preco, descricao)
        } else {
            // Se os dados essenciais estiverem faltando, pode ser um erro
            // Ou você pode usar um produto padrão/nulo e tratar isso no ViewModel/UI
            Produto(null, "Produto Desconhecido", 0.0, "Dados incompletos")
        }

        // >>> NOVO: Carrega os detalhes do produto no ViewModel <<<
        viewModel.loadProductDetails(produtoCarregado)
        // <<< FIM DA CHAMADA PARA O ViewModel >>>

        // >>> NOVO: Observador do estado da UI do ViewModel <<<
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is DetalhesProdutosUiState.Loading -> {
                    // Opcional: Mostrar um ProgressBar se o carregamento fosse assíncrono aqui
                    // binding.progressBarDetalhes.visibility = View.VISIBLE
                }
                is DetalhesProdutosUiState.ProductLoaded -> {
                    // binding.progressBarDetalhes.visibility = View.GONE
                    val produto = state.produto
                    // Atualiza a UI com os detalhes do produto
                    Glide.with(this).load(produto.foto).into(binding.detlahesfoto)
                    binding.dtNome.text = produto.nome
                    binding.dtDescricao.text = produto.descricao
                    val precoFormatado = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(produto.preco)
                    binding.dtPreco.text = precoFormatado
                }
                is DetalhesProdutosUiState.ProductAdded -> {
                    // binding.progressBarDetalhes.visibility = View.GONE
                    exibirMensagemProdutoAdicionado(state.addedProduct) // Chama a função local para o Snackbar
                    // Opcional: Você pode querer abrir o diálogo do carrinho aqui, mas é geralmente feito na HomeTela
                    // var carrinhoDialog = CarrinhoDialog(this, viewModel.getCarrinhoItens(), carrinhoProdutos) // Opcional: pegar itens do ViewModel
                }
                is DetalhesProdutosUiState.Error -> {
                    // binding.progressBarDetalhes.visibility = View.GONE
                    // Exibir Snackbar de erro
                    val snackbar = Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG)
                    snackbar.setTextColor(Color.WHITE)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()
                }
            }
        }
        // <<< FIM DO OBSERVADOR DO ViewModel >>>

        // >>> REMOVIDO/REAVALIADO: CarrinhoProdutos e Adapter Produtos serão obtidos do Repository se necessário <<<
        // carrinhoproduto = CarrinhoProdutos(this) // Removido ou reavaliado
        // val listaProdutos = carrinhoproduto.recuperarItensCarrinho() // Removido
        // adapter = AdapterProdutos(this, listaProdutos) // Removido ou reavaliado

        // O listener do botão "Adicionar ao Carrinho" agora chama o ViewModel
        binding.AdcCar.setOnClickListener {
            // >>> MODIFICADO: Delega a ação para o ViewModel <<<
            viewModel.adicionarAoCarrinho()
        }

        // A função removerProduto estava no onCreate, não estava sendo usada e não é uma função membro da classe.
        // Ela deveria estar no ViewModel ou em um Repository se for uma ação do carrinho.
        /*
        fun removerProduto(produto: Produto) {
            // Remove o produto da lista
            listaProdutos.remove(produto)
            // Atualiza o adapter após a remoção do produto
            adapter.atualizarLista(listaProdutos)
        }
        */
        // <<< FIM DA REMOÇÃO/REAVALIAÇÃO DA FUNÇÃO LOCAL REMOVERPRODUTO >>>
    }

    // >>> MODIFICADO: Função exibirMensagemProdutoAdicionado para receber o produto diretamente <<<
    private fun exibirMensagemProdutoAdicionado(produtoAdicionado: Produto) {
        val snackbarMessage = "Produto adicionado: ${produtoAdicionado.nome} - Preço: R$ ${String.format("%.2f", produtoAdicionado.preco)}"
        val snackbar = Snackbar.make(binding.root, snackbarMessage, Snackbar.LENGTH_INDEFINITE)
        snackbar.setTextColor(Color.BLACK)
        snackbar.setBackgroundTint(Color.GRAY)
        snackbar.setAction("Ok") { snackbar.dismiss() }
        snackbar.show()
    }
    // <<< FIM DA MODIFICAÇÃO >>>


    // >>> REMOVIDO: exibirMensagemProdutoRemovido se não for mais usada diretamente aqui <<<
    // Se a lógica de remoção for implementada e precisar de um Snackbar aqui,
    // o ViewModel emitiria um estado para isso.
    /*
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
    */
    // <<< FIM DA REMOÇÃO/REAVALIAÇÃO >>>
}