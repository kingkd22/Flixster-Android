package com.example.flixster_android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class MovieAdapter(
    private val context: Context,
    private val movies: List<Movie>,
    private val isHorizontalCard: Boolean = true
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutRes = if (isHorizontalCard) {
            R.layout.item_movie_card
        } else {
            R.layout.item_movie
        }
        val view = LayoutInflater.from(context).inflate(layoutRes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount() = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val ivPoster = itemView.findViewById<ImageView>(R.id.ivPoster)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvOverview = itemView.findViewById<TextView>(R.id.tvOverview)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(movie: Movie) {
            tvTitle.text = movie.title

            if (isHorizontalCard) {
                tvOverview.text = "⭐ ${String.format("%.1f", movie.voteAverage)} / 10"
            } else {
                tvOverview.text = movie.overview
            }

            val orientation = context.resources.configuration.orientation
            val imageUrl = if (!isHorizontalCard && orientation == Configuration.ORIENTATION_LANDSCAPE) {
                movie.backdropImageUrl
            } else {
                movie.posterImageUrl
            }

            val radiusInPx = (16 * context.resources.displayMetrics.density).toInt()

            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .transform(RoundedCorners(radiusInPx))
                .into(ivPoster)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val movie = movies[position]
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(MOVIE_EXTRA, movie)
                }

                val activity = context as? Activity
                if (activity != null) {
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity,
                        ivPoster,
                        "posterTransition"
                    )
                    context.startActivity(intent, options.toBundle())
                } else {
                    context.startActivity(intent)
                }
            }
        }
    }
}
