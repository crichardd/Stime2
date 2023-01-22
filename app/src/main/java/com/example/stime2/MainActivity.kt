package com.example.stime2

import GameAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.JsonObject
import com.google.gson.Gson
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    val gson = Gson()

    // Déclarer les variables pour RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var gameDataList = ArrayList<GameData>()


    interface SteamChartsAPI {
        @GET("GetMostPlayedGames/v1/")
        fun getMostPlayedGames(): retrofit2.Call<JsonObject>
    }

    // Définir la classe de données pour les jeux
    data class GameData(var rank: Int, var appid: Int, var last_week_rank: Int, var peak_in_game: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialise RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val viewManager = LinearLayoutManager(this)
        val viewAdapter = GameAdapter(gameDataList)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // Initialise Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.steampowered.com/ISteamChartsService/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Obtenir une instance de l'interface de l'API
        val steamChartsAPI = retrofit.create(SteamChartsAPI::class.java)

        // Appeler la méthode getMostPlayedGames() pour récupérer les données
        steamChartsAPI.getMostPlayedGames().enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: retrofit2.Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val json = response.body()
                    val result = json?.getAsJsonObject("result")
                    val dataArray = json?.getAsJsonObject("response")?.getAsJsonArray("ranks") ?: return
                    val gameDataList:List<GameData> = gson.fromJson(dataArray, Array<GameData>::class.java).toList()
                    this@MainActivity.gameDataList.addAll(gameDataList)
                    viewAdapter.notifyDataSetChanged()
                } else {
                    // gérer les erreurs ici
                }
            }
            override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {
                // gérer les erreurs ici
            }
        })
    }
}


