package edu.tcu.falarcon.quiz

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children


class QuestionActivity : AppCompatActivity(), View.OnClickListener {
    // Initialize all global variables
    private var selected = false // true user has selected an option
    private var answerRevealed = false // true if currently on answer screen for a question
    private lateinit var correctAnswerStr : String // String of the correct answer
    private lateinit var selectedOption : TextView // TextView of current Selected option
    private lateinit var correctOption : TextView // TextView of correct option
    private lateinit var optionLayout : LinearLayout // option LinearLayout
    private var qIndex = 0 // number Current question
    private val numberOfQuestions = 10
    private var score = 0 // user's score based on selections
    private var questions = Constants.getQuestions().shuffled() // shuffle list of questions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        // reset all booleans and counters every time QuestionActivity is created
        qIndex = 0
        score = 0
        selected = false
        answerRevealed = false
        questions = questions.shuffled()

        setQuestion() // Set up first question
    }

    /************** onClick **************
     *  after first question is set up,
     *  onClick method is the main flow.
     *  onClick() is called every time
     *  user selects an option TextView
     *  or the submit button.
     */
    override fun onClick(view: View) {
        // if button is clicked
        if (view == findViewById(R.id.submit_btn)) {
            if (!answerRevealed) {
                if(!selected) { // if button clicked with no selection show toast
                    Toast.makeText(this, "Please make a selection.", Toast.LENGTH_SHORT).show()
                }
                else { // else a selection is picked and button is clicked
                    if (selectedOption == correctOption) {
                        score++
                    }
                    answerView() // move on to phase 2, show answer
                }
            }
            else {
                // if answer is revealed and button is clicked
                qIndex++ //increment current question index
                if(qIndex < 10) { //if not in last question, set up next question
                    setQuestion()
                }
                else { //else if last question, go to show results page.
                    goToResult()
                }
            }
        }
        else { // else an option is selected
            selectedOptionView(view)
        }
    }

    /************** setQuestion() ***************
     * setQuestion adds all the elements to the screen.
     */
    private fun setQuestion() {
        // if index is less than total number of questions
        if (qIndex < numberOfQuestions) {
            answerRevealed = false // when setting up a question, answerRevealed is re-set to false
            selected = false // user has not selected an option for this question yet, set to false
            val layoutParams = setLayoutParams() //specify default layout parameters method
            val pixel = setPixel() // set pixel specifications

            layoutParams.setMargins(pixel, pixel, pixel, pixel) //set margins for layoutParams

            // Add elements in their own methods
            addQuestion()

            addFlag()

            addProgressBar()

            addOptions(pixel, layoutParams)

            addButton()
        }
    }

    /****************** selectedOptionView ************************
     * This method takes a View as a parameter (the element that
     * was clicked by the user), and adds the selected option
     * background, adding the purple border and bold text to the
     * currently selected option.
     */
    private fun selectedOptionView(view : View) {
        // set previously selected option to default option style
        if (selected) {
            selectedOption.setBackgroundResource(R.drawable.default_option_bg)
            selectedOption.typeface = Typeface.DEFAULT
        }
        // set new selected option to selected option style
        selectedOption = findViewById(view.id)
        selectedOption.setBackgroundResource(R.drawable.selected_option_bg)
        selectedOption.typeface = Typeface.DEFAULT_BOLD
        selected = true // user has now selected an option
    }

    /************** answerView() ****************
     * This method shows the correct answer in green,
     * and incorrect answer in red.
     * In addition, this method also changes the text of
     * the button depending on the phase.
     */
    private fun answerView() {
        // if selected option is not correct option, set selected option to wrong answer style
        if (selectedOption != correctOption) {
            selectedOption.setBackgroundResource(R.drawable.wrong_option_bg)
        }
        // set correct option to correct answer style
        correctOption.setBackgroundResource(R.drawable.correct_option_bg)
        correctOption.typeface = Typeface.DEFAULT_BOLD //set text to bold
        val button = findViewById<Button>(R.id.submit_btn)

        // if last question, button text should say "Finish"
        if (qIndex == numberOfQuestions-1) {
            button.setText(R.string.finish_btn_txt)

        }
        else {
            // else button indicates to continue
            button.setText(R.string.next_btn_txt)
        }

        // options will not be clickable in this screen
        for (view in optionLayout.children) {
            view.isClickable = false
        }
        answerRevealed = true
    }


    /******************* setLayoutParams() *****************
     * set layout parameters to match parent and wrap content
     */
    private fun setLayoutParams(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )
    }

    /******************* setPixel() *****************
     * set pixel value
     */
    private fun setPixel() : Int {
        return (TypedValue.applyDimension(
            COMPLEX_UNIT_DIP,
            10F,
            resources.displayMetrics
        ) + 0.5).toInt()
    }

    /******************* addQuestion() ******************/
    private fun addQuestion() {
        val questionTv = findViewById<TextView>(R.id.questionTv)
        questionTv.text = questions[qIndex].question
    }

    /******************* addFlag() ******************/
    private fun addFlag() {
        val flagIv = findViewById<ImageView>(R.id.flag_image_view)
        flagIv.setImageResource(questions[qIndex].image)

        // Set correct answer
        correctAnswerStr = questions[qIndex].correctAnswer
    }

    /******************* addButton() ******************/
    private fun addButton() {
        val button = findViewById<Button>(R.id.submit_btn)
        button.setText(R.string.submit_btn_txt)
        button.setOnClickListener(this)
    }

    /******************* addOptions() *****************
     * This method finds the layout and adds options
     * to the screen by iterating through the list and
     * shuffling the order of options.
     */
    private fun addOptions(pixel: Int, layoutParams: LinearLayout.LayoutParams) {
        optionLayout = findViewById(R.id.option_ll)
        optionLayout.removeAllViews() // remove options from last question

        var optionNum = 0 // int to set for each option id
        val options = questions[qIndex].options.shuffled() // shuffle option order
        for (option in options) {
            val optionTV = TextView(this) // creates a TextView for each option
            optionNum += 1
            optionTV.id = optionNum

            // formatting of option
            optionTV.setPadding(pixel, pixel, pixel, pixel)
            optionTV.gravity = Gravity.CENTER
            optionTV.layoutParams = layoutParams
            optionTV.text = option
            optionTV.setTextColor(ContextCompat.getColor(this, R.color.black))
            optionTV.setBackgroundResource(R.drawable.default_option_bg)
            optionLayout.addView(optionTV)

            optionTV.setOnClickListener(this)

            // set correct answer
            if (option == correctAnswerStr) {
                correctOption = optionTV
            }
        }
    }

    /******************* addProgressBar() ******************/
    private fun addProgressBar() {
        val bar = findViewById<ProgressBar>(R.id.progress_bar)
        bar.max = numberOfQuestions
        bar.progress = qIndex + 1

        val barTextView = findViewById<TextView>(R.id.bar_text_view)
        barTextView.text = String.format("%d/%d", qIndex+1, numberOfQuestions)
    }

    /******************* goToResult() *****************
     * This method creates an intent and sends username,
     * score, and total number of questions to the final
     * result screen.
     */
    private fun goToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("username", this.intent.getStringExtra("username"))
        intent.putExtra("score", score)
        intent.putExtra("numberOfQuestions", numberOfQuestions)

        startActivity(intent)
        finish()
    }
}












//        val optionLl = findViewById<LinearLayout>(R.id.option_ll)
//
//        val o1 = TextView(this)
//        val layoutParams = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//
//        val pixel = (TypedValue.applyDimension(COMPLEX_UNIT_DIP,
//            10F,
//            resources.displayMetrics) + 0.5).toInt()
//
//        o1.setPadding(pixel, pixel, pixel, pixel)
//        o1.gravity = Gravity.CENTER
//        o1.layoutParams = layoutParams
//
//        o1.text = "TCU"
//
//        o1.setTextColor(ContextCompat.getColor(this, R.color.black))
//        o1.setBackgroundResource(R.drawable.default_option_bg)
//        optionLl.addView(o1)
//
//        o1.setOnClickListener(this)