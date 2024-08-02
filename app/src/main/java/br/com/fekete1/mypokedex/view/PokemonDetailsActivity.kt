package br.com.fekete1.mypokedex.view

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.fekete1.mypokedex.R
import br.com.fekete1.mypokedex.viewmodel.PokemonDetailsViewModel
import br.com.fekete1.mypokedex.viewmodel.PokemonDetailsViewModelFactory
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import br.com.fekete1.mypokedex.domain.PokemonDetails
import com.bumptech.glide.Glide

class PokemonDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: PokemonDetailsViewModel
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false // Flag para verificar se o som está tocando

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_details)

        val pokemonNumber = intent.getIntExtra("POKEMON_NUMBER", -1)
        if (pokemonNumber == -1) {
            finish()
            return
        }

        viewModel = ViewModelProvider(this, PokemonDetailsViewModelFactory(pokemonNumber))
            .get(PokemonDetailsViewModel::class.java)

        viewModel.pokemon.observe(this, { pokemon ->
            pokemon?.let {
                updateUI(it)
                playPokemonCry(it) // Tocar o som do Pokémon quando a UI é atualizada
            }
        })

        // Configuração do botão "Voltar"
        val btnBack = findViewById<View>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed() // Navega de volta para a tela anterior
        }
    }

    private fun updateUI(pokemon: PokemonDetails) {
        val ivPokemon = findViewById<ImageView>(R.id.ivPokemonDetails)
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvNumber = findViewById<TextView>(R.id.tvNumber)
        val tvType1 = findViewById<TextView>(R.id.tvType1)
        val tvType2 = findViewById<TextView>(R.id.tvType2)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val tvHeight = findViewById<TextView>(R.id.tvHeight)
        val tvWeight = findViewById<TextView>(R.id.tvWeight)
        val tvCategory = findViewById<TextView>(R.id.tvCategory)
        val tvAbilities = findViewById<TextView>(R.id.tvAbilities)

        //STATS

        val tvStatHp = findViewById<TextView>(R.id.tvStatHp)
        val tvStatAttack = findViewById<TextView>(R.id.tvStatAttack)
        val tvStatDefense = findViewById<TextView>(R.id.tvStatDefense)
        val tvStatSpAtk = findViewById<TextView>(R.id.tvStatSpAtk)
        val tvStatSpDef = findViewById<TextView>(R.id.tvStatSpDef)
        val tvStatSpeed = findViewById<TextView>(R.id.tvStatSpeed)
        val tvStatTotal = findViewById<TextView>(R.id.tvStatTotal)

        //PROGRESSBAR STATS
        val pbStatHp = findViewById<ProgressBar>(R.id.pbStatHp)
        val pbStatAttack = findViewById<ProgressBar>(R.id.pbStatAttack)
        val pbStatDefense = findViewById<ProgressBar>(R.id.pbStatDefense)
        val pbStatSpAtk = findViewById<ProgressBar>(R.id.pbStatSpAtk)
        val pbStatSpDef = findViewById<ProgressBar>(R.id.pbStatSpDef)
        val pbStatSpeed = findViewById<ProgressBar>(R.id.pbStatSpeed)


        Glide.with(this).load(pokemon.imageUrl).into(ivPokemon)

        tvNumber.text = "Nº ${pokemon.formattedNumber}"
        tvName.text = pokemon.formattedName
        tvType1.text = pokemon.types[0].name.capitalize()
        tvType1.setBackgroundResource(pokemon.types[0].color)

        if (pokemon.types.size > 1) {
            tvType2.visibility = View.VISIBLE
            tvType2.text = pokemon.types[1].name.capitalize()
            tvType2.setBackgroundResource(pokemon.types[1].color)
        } else {
            tvType2.visibility = View.GONE
        }

        tvDescription.text = pokemon.description

        // Formatação diretamente no bindView
        val heightInMeters = pokemon.height?.toFloatOrNull()?.div(10) ?: 0f
        tvHeight.text = "${String.format("%.1f m", heightInMeters)}"

        val weightInKg = pokemon.weight?.toFloatOrNull()?.div(10) ?: 0f
        tvWeight.text = "${String.format("%.1f Kg", weightInKg)}"

        fun String.capitalizeWords(): String {
            return this.split("-")
                .joinToString(" ") { it.capitalize() }
        }

        tvCategory.text = "${pokemon.category?.name?.capitalizeWords() ?: "Não disponível"}"
        tvAbilities.text = pokemon.abilities?.joinToString(", ") { it.ability.name.capitalizeWords() }

        // Pokemon Status
        val statsList = pokemon.stats ?: emptyList()

        // Atualiza os valores e progressos
        tvStatHp.text = statsList.getOrNull(0)?.base_stat.toString()
        pbStatHp.progress = statsList.getOrNull(0)?.base_stat ?: 0

        tvStatAttack.text = statsList.getOrNull(1)?.base_stat.toString()
        pbStatAttack.progress = statsList.getOrNull(1)?.base_stat ?: 0

        tvStatDefense.text = statsList.getOrNull(2)?.base_stat.toString()
        pbStatDefense.progress = statsList.getOrNull(2)?.base_stat ?: 0

        tvStatSpAtk.text = statsList.getOrNull(3)?.base_stat.toString()
        pbStatSpAtk.progress = statsList.getOrNull(3)?.base_stat ?: 0

        tvStatSpDef.text = statsList.getOrNull(4)?.base_stat.toString()
        pbStatSpDef.progress = statsList.getOrNull(4)?.base_stat ?: 0

        tvStatSpeed.text = statsList.getOrNull(5)?.base_stat.toString()
        pbStatSpeed.progress = statsList.getOrNull(5)?.base_stat ?: 0

        // Calcula o total, tratando possíveis valores nulos
        val totalStats = statsList.filterNotNull().sumOf { it.base_stat ?: 0 }
        tvStatTotal.text = totalStats.toString()

        // Configura o clique na imagem do Pokémon para reproduzir o som com intervalo
        ivPokemon.setOnClickListener {
            if (!isPlaying) {
                isPlaying = true
                playPokemonCry(pokemon)
            }
        }

    }

    private fun playPokemonCry(pokemon: PokemonDetails) {
        val cryUrl = pokemon.cries?.latest

        if (!cryUrl.isNullOrEmpty()) {
            try {
                mediaPlayer?.release() // Libera o MediaPlayer anterior, se houver
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(cryUrl)
                    prepareAsync() // Prepara o MediaPlayer de forma assíncrona
                    setOnPreparedListener {
                        start() // Começa a reprodução quando estiver preparado
                        setOnCompletionListener {
                            this@PokemonDetailsActivity.isPlaying = false // Libera o flag quando a reprodução terminar
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace() // Adiciona tratamento de exceções
            }
        } else {
            Log.e("PokemonDetailsActivity", "A URL do som do Pokémon é nula ou vazia")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Libere o MediaPlayer quando a atividade for destruída
        mediaPlayer?.release()
        mediaPlayer = null
    }
}