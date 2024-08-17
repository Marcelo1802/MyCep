package com.example.mycep.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycep.R
import com.example.mycep.model.endereço


class Adapter(
    private var addressList: MutableList<endereço>,
    private val onCardClick: (endereço) -> Unit,
    private val onCardDelete: (endereço) -> Unit
) : RecyclerView.Adapter<Adapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val endereco = addressList[position]
        holder.bind(endereco)
        holder.itemView.setOnClickListener { onCardClick(endereco) }
        holder.deleteButton.setOnClickListener {
            onCardDelete(endereco)
            // Remover item da lista e atualizar a RecyclerView
            addressList.remove(endereco)
            notifyDataSetChanged() // Atualiza todos os itens
        }
    }

    override fun getItemCount() = addressList.size

    fun updateList(newList: List<endereço>) {
        addressList.clear()
        addressList.addAll(newList)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textEnd: TextView = itemView.findViewById(R.id.tv_card)
        private val textCep: TextView = itemView.findViewById(R.id.tv_cep)
        private val textNome: TextView = itemView.findViewById(R.id.tv_name)
        private val img: ImageView = itemView.findViewById(R.id.img_card)
        val deleteButton: ImageView = itemView.findViewById(R.id.img_delete)

        fun bind(endereco: endereço) {
            textEnd.text = endereco.logradouro
            textCep.text = endereco.cep
            textNome.text = endereco.nome
            img.setImageResource(endereco.img)
        }
    }
}