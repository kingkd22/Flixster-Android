package com.example.flixster_android

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.MaterialToolbar

const val MOVIE_EXTRA = "MOVIE_EXTRA"

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        val mainView = findViewById<View>(R.id.detailMain)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarDetail)

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            toolbar.setPadding(0, systemBars.top, 0, 0)
            mainView.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        toolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }

        val ivBackdrop = findViewById<ImageView>(R.id.ivDetailBackdrop)
        val ivPoster = findViewById<ImageView>(R.id.ivDetailPoster)
        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val tvRating = findViewById<TextView>(R.id.tvDetailRating)
        val tvReleaseDate = findViewById<TextView>(R.id.tvDetailReleaseDate)
        val tvVoteCount = findViewById<TextView>(R.id.tvDetailVoteCount)
        val tvLanguage = findViewById<TextView>(R.id.tvDetailLanguage)
        val tvPopularity = findViewById<TextView>(R.id.tvDetailPopularity)
        val tvOverview = findViewById<TextView>(R.id.tvDetailOverview)

        ViewCompat.setTransitionName(ivPoster, "posterTransition")
        supportPostponeEnterTransition()

        val movie = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(MOVIE_EXTRA, Movie::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(MOVIE_EXTRA) as? Movie
        }

        movie?.let {
            tvTitle.text = it.title
            tvRating.text = "⭐ Rating: ${String.format("%.1f", it.voteAverage)} / 10"
            tvReleaseDate.text = "📅 Release Date: ${it.releaseDate ?: "N/A"}"
            tvVoteCount.text = "👥 Vote Count: ${it.voteCount}"
            tvLanguage.text = "🌐 Language: ${it.originalLanguage?.uppercase() ?: "N/A"}"
            tvPopularity.text = "🔥 Popularity: ${String.format("%.1f", it.popularity)}"
            tvOverview.text = it.overview

            val radiusInPx = (12 * resources.displayMetrics.density).toInt()

            Glide.with(this)
                .load(it.backdropImageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(ivBackdrop)

            Glide.with(this)
                .load(it.posterImageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .transform(RoundedCorners(radiusInPx))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        supportStartPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        supportStartPostponedEnterTransition()
                        return false
                    }
                })
                .into(ivPoster)
        } ?: run {
            supportStartPostponedEnterTransition()
        }
    }
}
