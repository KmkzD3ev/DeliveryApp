package com.example.deliveryapp.data

import android.util.Log
import com.example.deliveryapp.model.Produto // Certifique-se de que o caminho para sua classe Produto está correto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await // Importante para usar .await()

class ProdutoRepository(private val firestore: FirebaseFirestore) {

    companion object {
        private const val TAG = "ProdutoRepository"
    }

    /**
     * Busca todos os produtos da coleção "Produtos" no Firestore.
     * @return Uma lista de objetos Produto.
     * @throws Exception se ocorrer um erro durante a busca.
     */
    suspend fun getTodosProdutos(): List<Produto> {
        val produtoList = mutableListOf<Produto>()
        try {

            val result = firestore.collection("Produtos").get().await()
            for (document in result) {
                val foto = document.getString("foto")
                val nome = document.getString("nome")
                val precoString = document.getString("preco")
                val descricao = document.getString("descricao")

                if (foto != null && nome != null && precoString != null) {
                    val precoPattern = "([0-9]+[,.][0-9]{1,2})".toRegex()
                    val matchResult = precoPattern.find(precoString)
                    val preco = matchResult?.value?.replace(",", ".")?.toDoubleOrNull()

                    if (preco != null) {
                        val produto = Produto(foto, nome, preco, descricao)
                        produtoList.add(produto)
                    } else {
                        Log.d(TAG, "O valor do campo 'preco' não pôde ser convertido para um número para o documento: ${document.id}.")
                    }
                } else {
                    Log.d(TAG, "Os dados (foto, nome, preco) obtidos do documento estão incompletos para o documento: ${document.id}.")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao buscar todos os produtos: ${e.message}", e)
            // É importante lançar a exceção para que o ViewModel possa lidar com ela
            throw e
        }
        return produtoList
    }

    /**
     * Pesquisa produtos na coleção "Produtos" do Firestore usando um campo 'tag'.
     * @param query O texto da pesquisa.
     * @return Uma lista de objetos Produto que correspondem à pesquisa.
     * @throws Exception se ocorrer um erro durante a pesquisa.
     */
    suspend fun pesquisarProdutos(query: String): List<Produto> {
        val produtosEncontrados = mutableListOf<Produto>()
        try {
            // Usa .await() para suspender a coroutine até que a Task do Firestore seja concluída
            val result = firestore.collection("Produtos")
                .whereEqualTo("tag", query) // **IMPORTANTE**: Certifique-se de ter um campo 'tag' em seus documentos do Firestore para que a pesquisa funcione. Se for outro campo, altere "tag" aqui.
                .get()
                .await()

            for (document in result) {
                val nome = document.getString("nome")
                if (nome != null) {
                    val preco = document.getString("preco")
                    val descricao = document.getString("descricao")
                    val foto = document.getString("foto")

                    if (preco != null) {
                        val precoDouble = preco.toDoubleOrNull()
                        if (precoDouble != null && foto != null) {
                            val produtoEncontrado = Produto(foto, nome, precoDouble, descricao)
                            produtosEncontrados.add(produtoEncontrado)
                        } else {
                            Log.e(TAG, "Erro ao converter o preço para Double ou a URL da imagem está vazia para o documento: ${document.id}.")
                        }
                    } else {
                        Log.e(TAG, "Campo 'preco' não encontrado ou está vazio para o documento: ${document.id}.")
                    }
                } else {
                    Log.e(TAG, "Nome do produto não encontrado no documento: ${document.id}.")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Falha na pesquisa por '$query': ${e.message}", e)
            // É importante lançar a exceção para que o ViewModel possa lidar com ela
            throw e
        }
        return produtosEncontrados
    }
}