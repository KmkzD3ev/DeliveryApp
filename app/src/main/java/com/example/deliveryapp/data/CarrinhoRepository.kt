// CarrinhoRepository.kt
package com.example.deliveryapp.data // Assegure-se de que este é o pacote correto para seus repositórios

import android.content.Context
import com.example.deliveryapp.Payment.CarrinhoProdutos // Importe sua classe CarrinhoProdutos
import com.example.deliveryapp.model.Produto // Importe sua classe Produto

class CarrinhoRepository(private val context: Context) { // O CarrinhoProdutos precisa de Context

    private val carrinhoProdutos = CarrinhoProdutos(context) // Instancia CarrinhoProdutos aqui

    /**
     * Adiciona um produto ao carrinho.
     * @param produto O produto a ser adicionado.
     * Não retorna Boolean, pois adcProduto em CarrinhoProdutos não retorna.
     */
    fun adicionarProdutoAoCarrinho(produto: Produto) {
        carrinhoProdutos.adcProduto(produto)
        // Se houver lógica de persistência (salvar em SharedPreferences ou DB)
        // que era feita dentro de adcProduto, ela continuará sendo feita lá.
        // Se a validação de sucesso/falha for necessária aqui no Repository,
        // o método adcProduto em CarrinhoProdutos precisaria retornar um Boolean.
        // Por enquanto, presumimos que a adição é sempre "bem-sucedida" a partir da chamada.
    }

    /**
     * Remove um produto do carrinho.
     * Baseia-se na lógica vista em DetalhesProdutos onde 'listaProdutos.remove(produto)' era usado.
     * @param produto O produto a ser removido.
     * @return true se o produto foi removido com sucesso, false caso contrário.
     */
    fun removerProdutoDoCarrinho(produto: Produto): Boolean {
        // A lógica de remoção será delegada ao objeto carrinhoProdutos.
        // Assumimos que CarrinhoProdutos.itens é a lista gerenciada.
        // Se a sua classe CarrinhoProdutos tiver um método 'removerProduto(produto)',
        // idealmente você chamaria ele aqui.
        // Como o seu código anterior mostrava 'listaProdutos.remove(produto)',
        // vamos simular isso através do acesso à lista 'itens' do carrinhoProdutos.
        // Note: É mais robusto se a classe CarrinhoProdutos expuser um método remove.
        // Se houver validação interna em CarrinhoProdutos sobre remoção, ela ficaria lá.

        val wasRemoved = carrinhoProdutos.itens.remove(produto)
        // Se houver lógica de persistência (ex: atualizar SharedPreferences) após a remoção,
        // e essa lógica estiver na classe CarrinhoProdutos, certifique-se de que ela é acionada lá.
        return wasRemoved
    }

    /**
     * Recupera a lista atual de itens no carrinho.
     * @return Uma lista mutável de produtos no carrinho.
     */
    fun getItensCarrinho(): MutableList<Produto> {
        // Simplesmente retorna a lista de itens que CarrinhoProdutos gerencia internamente.
        // No seu código anterior, você usava 'carrinhoproduto.recuperarItensCarrinho()',
        // então estou assumindo que 'carrinhoProdutos.itens' ou um método similar
        // como 'recuperarItensCarrinho' na sua classe CarrinhoProdutos expõe a lista.
        // Vamos usar carrinhoProdutos.itens diretamente, pois é mais direto.
        return carrinhoProdutos.itens
    }

    /**
     * Limpa todos os itens do carrinho.
     * Esta função não estava no seu código anterior, mas é útil para um carrinho.
     */
    fun limparCarrinho() {
        carrinhoProdutos.itens.clear()
        // Se houver persistência (ex: SharedPreferences), você precisaria salvar o estado vazio aqui
    }
}