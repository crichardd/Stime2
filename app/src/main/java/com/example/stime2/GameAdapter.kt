import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stime.GameViewHolder
import com.example.stime2.MainActivity
import com.example.stime2.R


class GameAdapter(private val games: List<MainActivity.GameData>) : RecyclerView.Adapter<GameViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_item, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.rank.text = "Rank: ${game.rank}"
        holder.appid.text = "App ID: ${game.appid}"
        holder.last_week_rank.text = "Last Week Rank: ${game.last_week_rank}"
        holder.peak_in_game.text = "Peak In Game: ${game.peak_in_game}"
        holder.name.text = "Nom: ${game.name}"
    }

    override fun getItemCount(): Int {
        return games.size
    }
}