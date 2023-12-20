    package com.example.deliveryapp.model

    import java.io.Serializable

    class Produto(var foto: String?, var nome: String?, var preco: Double?, var descricao: String?) : Serializable {

        override fun toString(): String {
            return "Produto(nome=$nome, preco=$preco, descricao=$descricao)"
        }

        constructor() : this("", "", 0.0, "")

        constructor(foto: String?, nome: String?, preco: String?, descricao: String?) : this() {
            this.foto = foto
            this.nome = nome
            this.preco = preco?.toDoubleOrNull()
            this.descricao = descricao
        }
    }
