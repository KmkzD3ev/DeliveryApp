// DetalhesProdutosViewModelFactory.kt
package com.example.deliveryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.deliveryapp.data.CarrinhoRepository // Importe seu CarrinhoRepository
import android.content.Context // Precisamos do Context para instanciar CarrinhoRepository

/**
 * Factory para criar uma instância de DetalhesProdutosViewModel com CarrinhoRepository.
 * Esta factory recebe o Context para poder instanciar o CarrinhoRepository.
 */
class DetalhesProdutosViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalhesProdutosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetalhesProdutosViewModel(CarrinhoRepository(context)) as T // Passa o CarrinhoRepository instanciado
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}