package com.example.deliveryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deliveryapp.data.CarrinhoRepository // Importe seu CarrinhoRepository
import com.example.deliveryapp.model.Produto // Importe sua classe Produto
import kotlinx.coroutines.launch

class DetalhesProdutosViewModel(private val carrinhoRepository: CarrinhoRepository) : ViewModel() {

    private val _uiState = MutableLiveData<DetalhesProdutosUiState>()
    val uiState: LiveData<DetalhesProdutosUiState> = _uiState

    // Propriedade para guardar o produto que está sendo exibido na tela
    private var currentProduct: Produto? = null

    /**
     * Carrega os detalhes do produto e os expõe via UiState.
     * @param produto Os detalhes do produto recebidos via Intent.
     */
    fun loadProductDetails(produto: Produto) {
        currentProduct = produto
        _uiState.value = DetalhesProdutosUiState.ProductLoaded(produto)
        // Se houvesse alguma operação assíncrona para buscar detalhes adicionais
        // do produto (ex: de uma API), ela seria iniciada aqui,
        // com o estado _uiState.value = DetalhesProdutosUiState.Loading
        // antes da chamada, e ProductLoaded ou Error após.
    }

    /**
     * Adiciona o produto atualmente carregado ao carrinho.
     */
    // Dentro de DetalhesProdutosViewModel.kt

    fun adicionarAoCarrinho() {
        currentProduct?.let { produto ->
            // Inicia uma coroutine para chamar o repositório
            viewModelScope.launch {
                try {
                    // >>> CORRIGIDO AQUI: Removemos 'val sucesso =' e a verificação 'if (sucesso)' <<<
                    carrinhoRepository.adicionarProdutoAoCarrinho(produto) // Apenas chama a função

                    // Assumimos sucesso, pois a função do Repository não retorna falha explícita.
                    // A lógica de sucesso é disparada diretamente.
                    _uiState.value = DetalhesProdutosUiState.ProductAdded(
                        addedProduct = produto,
                        message = "Produto adicionado: ${produto.nome} - Preço: R$ ${String.format("%.2f", produto.preco)}"
                    )
                } catch (e: Exception) {
                    // Lidar com exceções de rede ou outras que o repositório possa lançar
                    _uiState.value = DetalhesProdutosUiState.Error("Erro ao adicionar ao carrinho: ${e.message}")
                }
            }
        } ?: run {
            _uiState.value = DetalhesProdutosUiState.Error("Nenhum produto para adicionar ao carrinho.")
        }
    }


}