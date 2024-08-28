package com.example.pokedex.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.Pokemon
import com.example.pokedex.R

class AdapterPoke(private val pokeList: MutableList<Pokemon>): RecyclerView.Adapter<AdapterPoke.PokeViewHolder>() {

    inner class PokeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val nombreText: TextView = itemView.findViewById(R.id.pokemonName)
        val altura: TextView = itemView.findViewById(R.id.height)
        val peso: TextView = itemView.findViewById(R.id.weight)
        val sprite: ImageView = itemView.findViewById(R.id.imagePoke)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPoke.PokeViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.poke_card, parent, false)
        return PokeViewHolder(view)

    }

    override fun getItemCount(): Int {
        return pokeList.size
    }

    override fun onBindViewHolder(holder: PokeViewHolder, position: Int) {
        val pokemon = pokeList[position]
        val name = pokemon.name
        val height = pokemon.height
        val weight = pokemon.weight
        val sprite = pokemon.sprite

        holder.nombreText.text = name
        holder.peso.text = height.toString()
        holder.altura.text = weight.toString()

        Glide.with(holder.sprite.context)
            .load(sprite).override(120, 120).into(holder.sprite)



    }
}