// DetalhesProdutosUiState.kt
package com.example.deliveryapp.viewmodel // ou onde seus ViewModels estão

import com.example.deliveryapp.model.Produto // Certifique-se de importar sua classe Produto

/**
 * Representa os diferentes estados da UI da tela de Detalhes do Produto.
 */
sealed class DetalhesProdutosUiState {
    /** Indica o estado inicial ou que o produto está sendo carregado. */
    object Loading : DetalhesProdutosUiState()

    /** Indica que os detalhes do produto foram carregados e estão prontos para exibição. */
    data class ProductLoaded(val produto: Produto) : DetalhesProdutosUiState()

    /** Indica que um produto foi adicionado ao carrinho com sucesso. */
    data class ProductAdded(val addedProduct: Produto, val message: String) : DetalhesProdutosUiState()

    /** Indica que ocorreu um erro. */
    data class Error(val message: String) : DetalhesProdutosUiState()
}