package com.susan.org.gameifydemo.view

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.susan.org.gameifydemo.R
import com.susan.org.gameifydemo.model.Question
import com.susan.org.gameifydemo.viewmodel.QuestionViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_question.*
import java.util.*
import android.app.Activity
import com.bumptech.glide.Glide
import android.app.ProgressDialog



/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

/**
 * @Author Susan Lama on 16/12/2018.
 * @Email susan.invents@gmail.com
 */
class QuestionActivity : AppCompatActivity() {

    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }

    private var headlineImage: ImageView? = null
    private var tvRemainingPoints: TextView? = null
    private var scoreProgress: ProgressBar? = null
    private var headlineOne: TextView? = null
    private var headlineTwo: TextView? = null
    private var headlineThree: TextView? = null
    private var skipQuestion: TextView? = null

    private var questionList: List<Question>? = null
    private var questionCounter: Int = 0
    private var questionCountTotal: Int = 0
    private var currentQuestion: Question = Question()
    var correctHeadlineId: Int = -1
    private var correctHeadlineTitle: String? = null
    private var currentHeadlineImage: String? = null

    var score: Int = 0
    var answered: Boolean = false
    private var correctHeadlineActivityClosedSuccess: String? = null

    private var questionViewModel: QuestionViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_question)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        intUI()
        setupDataInUI()


    }

    private fun intUI() {
        headlineImage = findViewById(R.id.headline_image)
        tvRemainingPoints = findViewById(R.id.tv_remaining_points)
        scoreProgress = findViewById(R.id.score_progressbar)
        scoreProgress!!.max = 100
        headlineOne = findViewById(R.id.tv_headline_one)
        headlineTwo = findViewById(R.id.tv_headline_two)
        headlineThree = findViewById(R.id.tv_headline_three)
        skipQuestion = findViewById(R.id.tv_skip_btn)

        headlineOne!!.setOnClickListener(clickListener)
        headlineTwo!!.setOnClickListener(clickListener)
        headlineThree!!.setOnClickListener(clickListener)
        skipQuestion!!.setOnClickListener(clickListener)

    }

    private fun setupDataInUI() {
        // ask system fo view model
        questionViewModel =
                ViewModelProviders.of(this).get(QuestionViewModel::class.java) // .this helps to solve memory leak
        questionViewModel!!.getQuestionList().observe(this, Observer<List<Question>> {

            try {

                questionCountTotal = it!!.size
                questionList = it
                Collections.shuffle(questionList)
            } catch (e: NullPointerException) {
                e.message
            }

            // load question for the first time
            showNextQuestion()

        })
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    private val clickListener = View.OnClickListener { view ->
        if (!answered) {
            when (view.id) {
                R.id.tv_headline_one -> if (0 == correctHeadlineId) {
                    correctHeadlineTitle = currentQuestion.headlines[currentQuestion.correctAnswerIndex!!.toInt()]
                    headlineOne!!.setBackgroundResource(R.drawable.rectangle_shape_green)
                    increaseScore()
                    showcorrectHeadlineIdOnActivity(score, currentQuestion.imageUrl!!.trim(), correctHeadlineTitle!!,
                        currentQuestion.standFirst!!.trim(), currentQuestion.storyUrl!!)

                } else if (score >= 1) {
                    headlineOne!!.setBackgroundResource(R.drawable.rectangle_shape_red)
                    deduceScore()
                    showNextQuestion()

                } else if (score == 0) {
                    Toasty.warning(
                        applicationContext,
                        "Please skip for next question",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                R.id.tv_headline_two -> if (1 == correctHeadlineId) {
                    correctHeadlineTitle = currentQuestion.headlines[currentQuestion.correctAnswerIndex!!.toInt()]
                    headlineTwo!!.setBackgroundResource(R.drawable.rectangle_shape_green)
                    increaseScore()
                    showcorrectHeadlineIdOnActivity(score, currentQuestion.imageUrl!!.trim(), correctHeadlineTitle!!,
                        currentQuestion.standFirst!!.trim(), currentQuestion.storyUrl!!)

                } else if (score >= 1) {
                    headlineTwo!!.setBackgroundResource(R.drawable.rectangle_shape_red)
                    deduceScore()
                    showNextQuestion()

                } else if (score == 0) {
                    Toasty.warning(
                        applicationContext,
                        "Please skip for next question",
                        Toast.LENGTH_SHORT
                    )

                }
                R.id.tv_headline_three -> if (2 == correctHeadlineId) {
                    correctHeadlineTitle = currentQuestion.headlines[currentQuestion.correctAnswerIndex!!.toInt()]
                    headlineThree!!.setBackgroundResource(R.drawable.rectangle_shape_green)
                    increaseScore()
                    showcorrectHeadlineIdOnActivity(score, currentQuestion.imageUrl!!.trim(), correctHeadlineTitle!!,
                        currentQuestion.standFirst!!.trim(), currentQuestion.storyUrl!!)

                } else if (score >= 1) {
                    headlineThree!!.setBackgroundResource(R.drawable.rectangle_shape_red)
                    deduceScore()
                    showNextQuestion()

                } else if (score == 0) {
                    Toasty.warning(
                        applicationContext,
                        "Incorrect! Please skip for next question",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                R.id.tv_skip_btn -> showNextQuestion()
            }
            if (questionCounter < questionCountTotal) {

            } else {
                skipQuestion!!.text = "Finish"
            }
        }
    }

    private fun showcorrectHeadlineIdOnActivity(score: Int, imageUrl: String, title: String, standFirst: String, article: String) {
        val intent = Intent(this, CorrectHeadlineActivity::class.java)
        intent.putExtra(SCORE, score.toString())
        intent.putExtra(IMAGE_URL, imageUrl)
        intent.putExtra(HEDLINE_TITLE, title)
        intent.putExtra(STAND_FIRST, standFirst)
        intent.putExtra(READ_ARTICLE, article)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun showNextQuestion() {

        Handler().postDelayed({

            resetUI()

            if (questionCounter < questionCountTotal) {

                currentQuestion = questionList!![questionCounter]
                correctHeadlineId = questionList!![questionCounter].correctAnswerIndex!!.toInt()

                currentHeadlineImage = currentQuestion.imageUrl!!.trim()
                Log.d("GLIDE", currentHeadlineImage)
                //load image using Glide
                Glide.with(applicationContext)
                    .load(currentHeadlineImage!!)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(headlineImage)

                headlineOne!!.text = currentQuestion.headlines[0]
                headlineTwo!!.text = currentQuestion.headlines[1]
                headlineThree!!.text = currentQuestion.headlines[2]

                questionCounter++
                scoreProgress!!.progress = questionCounter
                var remainingPoints: String = (--questionCountTotal).toString()
                tvRemainingPoints!!.text = "+$remainingPoints comming your way"
                answered = false //lock the answer

            } else {
                finishQuestion()
            }
        }, 500)
    }

    private fun resetUI() {
        headlineOne!!.text = ""
        headlineTwo!!.text = ""
        headlineThree!!.text = ""
        correctHeadlineId = -1

        headlineOne!!.setBackgroundResource(R.drawable.rectangle_shape)
        headlineTwo!!.setBackgroundResource(R.drawable.rectangle_shape)
        headlineThree!!.setBackgroundResource(R.drawable.rectangle_shape)
    }

    private fun finishQuestion() {
        finish()
    }

    private fun increaseScore() {
        ++score
        updateScore()
    }

    private fun deduceScore() {
        --score
        updateScore()

    }

    private fun updateScore() {
        tv_score.text = "Score: $score"

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                correctHeadlineActivityClosedSuccess = data!!.getStringExtra("result")
                if (correctHeadlineActivityClosedSuccess == "SUCCESS")
                    showNextQuestion()
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
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
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

        const val SCORE: String = "score"
        const val IMAGE_URL: String = "imageUrl"
        const val HEDLINE_TITLE: String = "headlineTitle"
        const val STAND_FIRST: String = "standFirst "
        const val READ_ARTICLE: String = "readArticle "

        const val REQUEST_CODE: Int = 1

    }
}
