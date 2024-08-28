package com.example.pokedex

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.adapters.AdapterPoke
import com.example.pokedex.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var cont: Int = 0
    private var pokemonList = ArrayList<Pokemon>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

            pokeApiRequest()




    }

    private fun pokeApiRequest() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("https://pokeapi.co/api/v2/pokemon")
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    withContext(Dispatchers.Main) {

                        val jsonResponse = JSONObject(response)
                        val count = jsonResponse.getJSONArray("results")
                        Log.d("API Response", response)
                        Log.d("Count", count.length().toString())


                        for (i in 1..count.length()) {
                            fetchPokemonDetails(i)
                        }

                        for(pokemon in pokemonList){
                            Log.d("Pokemon", pokemon.sprite)
                        }

                        recyclerView = findViewById(R.id.pokedex)
                        val layoutManager = LinearLayoutManager(baseContext)
                        binding.pokedex.layoutManager = layoutManager
                        binding.pokedex.adapter = AdapterPoke(pokemonList)

                    }
                    inputStream.close()
                } else {
                    Log.e("API Request", "Error: $responseCode")
                }

                connection.disconnect()
            } catch (e: IOException) {
                // Handle network-related exception here
                Log.e("API Request", "Network exception occurred: ${e.message}")
            }
        }
    }

    private suspend fun fetchPokemonDetails(id: Int){
        withContext(Dispatchers.IO) {
            try {
                val url = URL("https://pokeapi.co/api/v2/pokemon/$id")
                val connection = url.openConnection() as HttpURLConnection



                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    withContext(Dispatchers.Main) {


                        val jsonResponse = JSONObject(response)
                        val name = jsonResponse.getString("name");
                        val height = jsonResponse.getString("height")
                        val weight = jsonResponse.getString("weight")
                        val sprites = jsonResponse.getJSONObject("sprites")
                        val frontDefault = sprites.getString("front_default")
                        val newPokemon = Pokemon(name, height.toInt(), weight.toInt(), frontDefault)
                        pokemonList.add(newPokemon)
                        cont++
                        Log.d("new pokemon", newPokemon.name + cont)

                    }
                    inputStream.close()
                } else {
                    Log.e("Pokemon Details", "Error: $responseCode")
                }

                connection.disconnect()
            } catch (e: IOException) {
                // Handle network-related exception here
                Log.e("Pokemon Details", "Network exception occurred: ${e.message}")
            }
        }
    }

}


data class Pokemon(val name: String, val height: Int, val weight: Int, val sprite: String){

}
