// HomeUiState.kt
package com.example.deliveryapp.viewmodel

import com.example.deliveryapp.model.Produto // Certifique-se de importar sua classe Produto, o caminho pode ser diferente dependendo de onde Produto está.

/**
 * Representa os diferentes estados da UI da HomeTela.
 */
sealed class HomeUiState {
    /** Indica que os dados estão sendo carregados. */
    object Loading : HomeUiState()

    /** Indica que os dados foram carregados com sucesso. */
    data class Success(val produtos: List<Produto>) : HomeUiState()

    /** Indica que ocorreu um erro durante o carregamento dos dados. */
    data class Error(val message: String) : HomeUiState()

    /** Indica que não há produtos a serem exibidos (ex: pesquisa sem resultados). */
    object Empty : HomeUiState()
}