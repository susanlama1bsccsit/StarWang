package com.susan.org.gameifydemo.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.susan.org.gameifydemo.R
import kotlinx.android.synthetic.main.activity_correctheadline.*
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_question.*


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

/**
 * @Author Susan Lama on 16/12/2018.
 * @Email susan.invents@gmail.com
 */
class CorrectHeadlineActivity : AppCompatActivity() {
    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreen_content_cha.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }

    private lateinit var currentImage: ImageView
    private lateinit var tvScore: TextView
    private lateinit var tvCorrectHedline: TextView
    private lateinit var tvStandFirst: TextView
    private lateinit var btnReadArticle: TextView
    private lateinit var btnNextQuestion: TextView
    private lateinit var btnLeaderBoard: TextView

    var imageUrl: String? = null
    var score: String? = null
    var headlineTitle: String? = null
    var standFirst: String? = null
    var readArticle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correctheadline)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreen_content_cha.setOnClickListener { toggle() }

        initUI()
        setupDataOnUI()
    }

    private fun setupDataOnUI() {
        val extras = intent.extras
        if (extras != null) {
            score = extras.getString(QuestionActivity.SCORE)
            imageUrl = extras.getString(QuestionActivity.IMAGE_URL)
            headlineTitle = extras.getString(QuestionActivity.HEDLINE_TITLE)
            standFirst = extras.getString(QuestionActivity.STAND_FIRST)
            readArticle = extras.getString(QuestionActivity.READ_ARTICLE)

        }
       
        Glide.with(applicationContext)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .into(currentImage)

        tvScore.text = score
        tvCorrectHedline.text = headlineTitle
        tvStandFirst.text = standFirst
    }

    private fun initUI() {

        currentImage = findViewById(R.id.iv_currentImage)
        tvScore = findViewById(R.id.tv_score)
        tvCorrectHedline = findViewById(R.id.tv_correct_headline)
        tvStandFirst = findViewById(R.id.tv_stand_first)
        btnReadArticle = findViewById(R.id.read_article_btn)
        btnNextQuestion = findViewById(R.id.next_question_btn)
        btnLeaderBoard = findViewById(R.id.leader_board_btn)

        btnReadArticle.setOnClickListener(clickListener)
        btnNextQuestion.setOnClickListener(clickListener)
        btnLeaderBoard.setOnClickListener(clickListener)

    }

    private fun quitPage() {

        val returnIntent = Intent()
        returnIntent.putExtra("result", "SUCCESS")
        setResult(Activity.RESULT_OK, returnIntent)
        finish()

    }

    private val clickListener = View.OnClickListener { view ->

        when (view.id) {
            R.id.read_article_btn -> {

                val i = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(readArticle)
                )
                startActivity(i)

            }
            R.id.next_question_btn -> {

                quitPage()
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        fullscreen_content_cha.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
    }
}
