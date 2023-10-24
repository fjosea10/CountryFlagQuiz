package edu.tcu.falarcon.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

// Opening Quiz Page
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//      This is the EditText field where user inputs name
        val nameEt = findViewById<TextInputEditText>(R.id.name_et)

        // If user inputs "enter" on keyboard, go to question Activity
        nameEt.setOnEditorActionListener{_,actionID,_ ->
            if(actionID == EditorInfo.IME_ACTION_GO) { // if "enter" button is pressed
                if(!nameEt.text.isNullOrEmpty()) {
                    // if name not empty move on to the next activity
                    goToQuestion(nameEt)
                }
                else enterNameToast(this) //if empty show toast
                true
            } else false
        }

        // find start button
        val startBtn = findViewById<Button>(R.id.start_btn)
        startBtn.setOnClickListener{//if start button is clicked move on to question activity
            if(!nameEt.text.isNullOrEmpty()) {
                goToQuestion(nameEt)
            }
            else enterNameToast(this) // if name is empty show toast
        }
    }

    /***************** goToQuestion() ***************
     * creates an intent to send over the username
     * into the next activity
     */
    private fun goToQuestion(nameEt: TextInputEditText) {
        val intent = Intent(this, QuestionActivity::class.java)
        intent.putExtra("username", nameEt.text.toString())
        startActivity(intent)
        finish()
    }
}

/****************** enterNameToast() **********************
 * Display a toast when user tries to start quiz without
 * entering a name.
 */
    private fun enterNameToast(mainActivity: MainActivity) {
        Toast.makeText(mainActivity, "Please enter your name.", Toast.LENGTH_SHORT).show()
    }