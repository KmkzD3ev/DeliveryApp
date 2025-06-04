package com.example.deliveryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // Importante para as Coroutines
import com.example.deliveryapp.data.ProdutoRepository // Certifique-se de importar seu ProdutoRepository
import com.example.deliveryapp.model.Produto // Importe sua classe Produto
import kotlinx.coroutines.launch // Importe para lançar coroutines

class HomeViewModel(private val repository: ProdutoRepository) : ViewModel() {

    // MutableLiveData para permitir que o ViewModel atualize o estado
    private val _uiState = MutableLiveData<HomeUiState>()

    // LiveData para que a UI possa observar as mudanças no estado
    val uiState: LiveData<HomeUiState> = _uiState

    init {
        // Carrega os produtos assim que o ViewModel é criado
        carregarTodosProdutos()
    }

    /**
     * Carrega todos os produtos usando o ProdutoRepository e atualiza o estado da UI.
     */
    fun carregarTodosProdutos() {
        _uiState.value = HomeUiState.Loading // Define o estado como carregando
        viewModelScope.launch { // Inicia uma coroutine no escopo do ViewModel
            try {
                val produtos = repository.getTodosProdutos() 
                if (produtos.isNotEmpty()) {
                    _uiState.value = HomeUiState.Success(produtos) // Define o estado como sucesso com os produtos
                } else {
                    _uiState.value = HomeUiState.Empty // Define o estado como vazio se não houver produtos
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Erro ao carregar produtos: ${e.message}") // Define o estado como erro
            }
        }
    }

    /**
     * Pesquisa produtos usando o ProdutoRepository e atualiza o estado da UI.
     * @param query O termo de busca.
     */
    fun pesquisarProdutos(query: String) {
        // Opcional: Se a query for vazia, pode ser interessante recarregar todos os produtos
        if (query.isBlank()) {
            carregarTodosProdutos()
            return
        }

        _uiState.value = HomeUiState.Loading // Define o estado como carregando durante a pesquisa
        viewModelScope.launch { // Inicia uma coroutine no escopo do ViewModel
            try {
                val produtos = repository.pesquisarProdutos(query) // Chama a função suspend do Repository para pesquisa
                if (produtos.isNotEmpty()) {
                    _uiState.value = HomeUiState.Success(produtos) // Define o estado como sucesso com os produtos encontrados
                } else {
                    _uiState.value = HomeUiState.Empty // Define o estado como vazio se não houver produtos para a pesquisa
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Erro ao pesquisar produtos: ${e.message}") // Define o estado como erro
            }
        }
    }
}