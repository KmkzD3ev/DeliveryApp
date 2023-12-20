package com.example.deliveryapp.model

import android.net.Uri
import android.util.Log
import com.example.deliveryapp.Payment.EntrEgasPedidos
import com.example.deliveryapp.adapter.PedidosAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class database {


    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun salvarDadosUsuarios(nome: String, foto: Uri) {
        val UsuarioId = auth.currentUser!!.uid
        val nomedoArquivo = UUID.randomUUID().toString()
        val storageReference = storage.getReference("/imagens/$nomedoArquivo")

        storageReference.putFile(foto).addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                val fotoS = uri.toString()
                val fotomap = hashMapOf(
                    "foto" to fotoS
                )

                val documentReference = db.collection("fotos Usuarios").document(UsuarioId)
                documentReference.set(fotomap).addOnCompleteListener { tarefa ->
                    if (tarefa.isSuccessful) {
                        val usuario = hashMapOf(
                            "foto" to fotoS
                        )
                        val usuarios: MutableMap<String, Any> = HashMap()
                        usuarios["nome"] = nome

                        db.collection("usuarios")
                            .document(UsuarioId)
                            .set(usuario)
                            .addOnSuccessListener {
                                Log.d("Database", "Documento adicionado com ID:")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Database", "Erro ao adicionar documento", e)
                            }
                    }

                    Log.d("Database", "Imagem salva com sucesso: $nomedoArquivo")
                }.addOnFailureListener { exception ->
                    Log.e("Database", "Erro ao salvar imagem: $nomedoArquivo", exception)
                }
            }
        }
    }

    fun salvarPedidosUser(
        endereco: String,
        celular: String,
        status_pagamentos : String,
        listaProdutos: MutableList<Produto>,
        pontoRefere : String,
        entregastatus : String,
        pedidoId:String,
        UsuarioId:String



    ){
        var db = FirebaseFirestore.getInstance()
      // var usuarioId = FirebaseAuth.getInstance().currentUser!!.uid
        //var pedidoiD = UUID.randomUUID().toString()


        val listapedidos = hashMapOf(
            "endereco" to endereco,
            "celular" to  celular,
            "listaprodutos" to listaProdutos,
            "pontoRefere" to pontoRefere,
            "status_pagamentos" to status_pagamentos,
            "entregastatus" to entregastatus,
            "usuarioId" to  UsuarioId,
            "pedidoiD" to pedidoId
        )

         val documentReference = db.collection("Usuario_Pedidos").document(UsuarioId)
             .collection("Pedidos").document(pedidoId)

        documentReference.set(listapedidos).addOnSuccessListener {
            Log.d("pedidos","Pedios realizado com sucesso")
        }

    }
    fun salvarPedidoAdm(
        endereco: String,
        celular: String,
        status_pagamentos : String,
        listaProdutos: MutableList<Produto>,
        pontoRefere : String,
        entregastatus : String,
        pedidoId:String,
        UsuarioId:String,
        nomeUsuario: String



    ){
        var db = FirebaseFirestore.getInstance()
        var usuarioId = FirebaseAuth.getInstance().currentUser!!.uid

       // var pedidoiD = UUID.randomUUID().toString()


        val listapedidos = hashMapOf(
            "endereco" to endereco,
            "celular" to  celular,
            "listaprodutos" to listaProdutos,
            "pontoRefere" to pontoRefere,
            "status_pagamentos" to status_pagamentos,
            "entregastatus" to entregastatus,
            "usuarioId" to usuarioId,
            "pedidoiD" to pedidoId,
            "nomeUsuario" to nomeUsuario

        )

        val documentReference = db.collection("Pedidos_Adm").document(pedidoId)

        documentReference.set(listapedidos).addOnSuccessListener {
            Log.d("Database", "Imagem salva com sucesso: $listapedidos")
        }

    }




    fun obterListaPedidos(listapedidos: MutableList<ServidorDadosPedidos>, pedidosAdapter:PedidosAdapter){
        val db = FirebaseFirestore.getInstance()
        val usuarioId = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("Usuario_Pedidos").document(usuarioId).collection("Pedidos")
            .get().addOnCompleteListener { tarefa->
                if (tarefa.isSuccessful){
                    for (documento in tarefa.result){
                        val pedidos = documento.toObject(ServidorDadosPedidos::class.java)
                        listapedidos.add(pedidos)
                       // Log.d("Database", "Pedido obtido DO BANCO DE DADOS: $pedidos") // Adiciona um log para verificar os pedidos recuperados
                        pedidosAdapter.notifyDataSetChanged()

                    }
                  //  Log.d("Database", "Lista de pedidos recebida com sucesso. Tamanho: ${listapedidos.size}")

                }
              //  Log.e("Database", "Erro ao obter pedidos: ${tarefa.exception}")
                tarefa.exception?.printStackTrace()
            }

    }


}

