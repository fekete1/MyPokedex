package br.com.fekete1.mypokedex.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fekete1.mypokedex.R
import br.com.fekete1.mypokedex.viewmodel.PokemonViewModel
import br.com.fekete1.mypokedex.viewmodel.PokemonViewModelFactory
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter
    private lateinit var viewModel: PokemonViewModel
    private lateinit var loadingImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar o RecyclerView e Adapter
        recyclerView = findViewById(R.id.rvPokemons)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PokemonAdapter() // Passando o contexto
        recyclerView.adapter = adapter

        // Configurar o ViewModel
        viewModel = ViewModelProvider(this, PokemonViewModelFactory())
            .get(PokemonViewModel::class.java)

        // Configurar o ImageView de loading
        loadingImageView = findViewById(R.id.loadingProgress)

        // Carregar o GIF de loading usando Glide
        Glide.with(this).asGif().load(R.drawable.loading).into(loadingImageView)

        // Observar os dados dos Pokémon
        viewModel.pokemons.observe(this, Observer { pokemons ->
            adapter.submitList(pokemons)

            // Verificar se os dados foram carregados para esconder a tela de loading inicial
            if (pokemons.isNotEmpty()) {
                loadingImageView.visibility = View.GONE
            }
        })

        // Adicionar o ScrollListener para carregar mais dados
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount - 10) && firstVisibleItemPosition >= 0) {
                    viewModel.loadMorePokemons()
                }
            }
        })
    }
}