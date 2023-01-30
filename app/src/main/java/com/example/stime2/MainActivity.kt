package com.example.stime2

import GameAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


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

    interface SteamInfosAPI {
        @GET("appdetails")
        fun getGameInfos(@Query("appids") appid: Int): Call<JsonObject>
    }

    data class SteamInfosData(val name: String, val type: String)

    // Définir la classe de données pour les jeux
    data class GameData(var rank: Int, var appid: Int, var last_week_rank: Int, var peak_in_game: Int, var name: String, var header_image: String)

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

        val retrofit2 = Retrofit.Builder()
            .baseUrl("https://store.steampowered.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Obtenir une instance de l'interface de l'API
        val steamChartsAPI = retrofit.create(SteamChartsAPI::class.java)
        val steamInfosAPI = retrofit2.create(SteamInfosAPI::class.java)

        steamChartsAPI.getMostPlayedGames().enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: retrofit2.Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val json = response.body()
                    val dataArray = json?.getAsJsonObject("response")?.getAsJsonArray("ranks") ?: return
                    val gameDataList:List<GameData> = gson.fromJson(dataArray, Array<GameData>::class.java).toList()

                    for(gameData in gameDataList) {
                        steamInfosAPI.getGameInfos(gameData.appid).enqueue(object : Callback<JsonObject> {
                            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                if (response.isSuccessful) {
                                    val json = response.body()
                                    val data = json?.getAsJsonObject(gameData.appid.toString())
                                    val gameInfos = data?.getAsJsonObject("data")
                                    val gameName = gameInfos?.get("name")?.asString ?: "N/A"
                                    gameData.name = gameName ?: "N/A"
                                    viewAdapter.notifyDataSetChanged()
                                } else {
                                    // gérer les erreurs ici
                                }
                            }
                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                // gérer les erreurs ici
                            }
                        })
                    }
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