package edu.tcu.falarcon.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // create intent to go to MainActivity
        val intent = Intent(this, MainActivity::class.java)

        // get emoji ImageView, message Textview, and score textView created in activity_result.xml
        val iv = findViewById<ImageView>(R.id.result_iv)
        val msgTv = findViewById<TextView>(R.id.msg_tv)
        val scoreTv = findViewById<TextView>(R.id.score_tv)

        // get values from previous activity intent
        val username = this.intent.getStringExtra("username")
        val score = this.intent.getIntExtra("score", 0)
        val numberOfQuestions = this.intent.getIntExtra("numberOfQuestions", 10)


        if (score >= 6) {
            //show trophy
            iv.setImageResource(R.drawable.ic_trophy)
            msgTv.text = String.format("Congratulations, %s!", username)
        }
        else {
            //show sweat face
            iv.setImageResource(R.drawable.ic_sweat_face)
            msgTv.text = String.format("Good luck next time, %s!", username)
        }
        scoreTv.text = String.format("Your score is %s out of %s.", score, numberOfQuestions)

        findViewById<Button>(R.id.try_again_btn).setOnClickListener {
            getIntent().removeExtra("key") // clear values from intent
            startActivity(intent)
            finish()
        }
    }
}