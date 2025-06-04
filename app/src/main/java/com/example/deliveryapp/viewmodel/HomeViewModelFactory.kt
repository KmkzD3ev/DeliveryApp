package com.example.deliveryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.deliveryapp.data.ProdutoRepository // Certifique-se de importar seu ProdutoRepository, o caminho pode ser diferente

/**
 * Factory para criar uma instância de HomeViewModel com ProdutoRepository.
 * Age como um Context Api espalhando dados onde forem nescessarios
 */
class HomeViewModelFactory(private val repository: ProdutoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica se a classe do ViewModel solicitada é HomeViewModel
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            // Retorna uma nova instância de HomeViewModel, passando o repositório
            @Suppress("UNCHECKED_CAST") // Suprime o aviso de cast não verificado, pois sabemos que o tipo é seguro aqui
            return HomeViewModel(repository) as T
        }
        // Lança uma exceção se a classe do ViewModel for desconhecida
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}