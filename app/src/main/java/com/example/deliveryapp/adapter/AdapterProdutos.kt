package com.example.deliveryapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deliveryapp.Payment.CarrinhoProdutos
import com.example.deliveryapp.R
import com.example.deliveryapp.adapter.AdapterProdutos.ProdutoViewHolder
import com.example.deliveryapp.model.Produto
import com.example.deliveryapp.view.DetalhesProdutos
import de.hdodenhof.circleimageview.CircleImageView

class AdapterProdutos(
    var context: Context,
    var produtoList: List<Produto>,
    private var layoutResourceId: Int = R.layout.produto_item,

) :
    RecyclerView.Adapter<ProdutoViewHolder>() {

    private val itensSelecionados: MutableList<Produto> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val itemLista: View = LayoutInflater.from(context).inflate(layoutResourceId, parent, false)
        return ProdutoViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = produtoList[position]

        val resID = context.resources.getIdentifier(
            produtoList[position].foto,
            "drawable",
            context.packageName
        )
        Glide.with(context)
            .load(produtoList[position].foto).into(holder.foto);
        holder.nome.text = produtoList[position].nome
        holder.preco.text = String.format("R$ %.2f", produtoList[position].preco)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetalhesProdutos::class.java)
            intent.putExtra("foto", produtoList.get(position).foto)
            intent.putExtra("nome", produtoList.get(position).nome)
            intent.putExtra("descricao", produtoList.get(position).descricao)
            intent.putExtra("preco", produtoList.get(position).preco)
            context.startActivity(intent)

        }

    }


    override fun getItemCount(): Int {
        return produtoList.size
    }

    fun setLayoutResourceId(layoutResourceId: Int) {
        this.layoutResourceId = layoutResourceId
        notifyDataSetChanged()
    }

    inner class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foto: CircleImageView
        val nome: TextView
        val preco: TextView

        init {
            foto = itemView.findViewById(R.id.fotoproduto)
            nome = itemView.findViewById(R.id.descriProduto)
            preco = itemView.findViewById(R.id.preçoProduto)

            itemView.setOnClickListener {
                val position = adapterPosition
                val produto = produtoList[position]

            }

        }
    }

    fun atualizarLista(listaAtualizada: List<Produto>) {
        produtoList = listaAtualizada
        for (produto in listaAtualizada) {
            Log.d("ATUALIZAR_LISTA", "Nome: ${produto.nome}, Preço: ${produto.preco}, Descrição: ${produto.descricao}")
        }
        notifyDataSetChanged()
    }





}

