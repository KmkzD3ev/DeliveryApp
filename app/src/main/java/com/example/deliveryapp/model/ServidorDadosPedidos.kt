package com.example.deliveryapp.model

import android.view.View

data class ServidorDadosPedidos(
    val endereco: String? = null,
    val celular: String? = null,
    val listaprodutos: MutableList<Produto>, // Alterado de listaProdutos para listaprodutos
    val status_pagamentos: String? = null, // Alterado de status_pagamento para status_pagamentos
    val pontoRefere: String? = null,
    val entregastatus: String? = null,
    val pedidoId:String? = null,
    val UsuarioId:String? = null


) {
    constructor() : this("", "", mutableListOf<Produto>(), "", "", "")


}