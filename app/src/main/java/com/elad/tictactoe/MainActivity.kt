package com.elad.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private lateinit var buttons: Array<Array<Button>>
    private lateinit var textViewStatus: TextView
    private lateinit var buttonPlayAgain: Button
    private var playerXTurn = true
    private var roundCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewStatus = findViewById(R.id.textViewStatus)
        buttonPlayAgain = findViewById(R.id.buttonPlayAgain)

        buttons = Array(3) { row ->
            Array(3) { col ->
                val buttonID = "button_${row}${col}"
                val resID = resources.getIdentifier(buttonID, "id", packageName)
                findViewById<Button>(resID).apply {
                    setOnClickListener { onButtonClick(this) }
                }
            }
        }

        buttonPlayAgain.setOnClickListener { resetGame() }
    }

    private fun onButtonClick(button: Button) {
        if (button.text.isNotEmpty()) return

        button.text = if (playerXTurn) "X" else "O"
        roundCount++

        // Check for win using the first part of the pair (Boolean)
        val (isWinner, winningButtons) = checkForWin()
        if (isWinner) {
            val winner = if (playerXTurn) "Player X wins!" else "Player O wins!"
            showWinner(winner, winningButtons)
        } else if (roundCount == 9) {
            showWinner("It's a Draw!")
        } else {
            playerXTurn = !playerXTurn
            textViewStatus.text = if (playerXTurn) "Player X's Turn" else "Player O's Turn"
        }
    }


    private fun checkForWin(): Pair<Boolean, List<Pair<Int, Int>>> {
        val field = Array(3) { row -> Array(3) { col -> buttons[row][col].text.toString() } }

        // Check rows and columns
        for (i in 0..2) {
            if (field[i][0] == field[i][1] && field[i][0] == field[i][2] && field[i][0].isNotEmpty()) {
                return Pair(true, listOf(Pair(i, 0), Pair(i, 1), Pair(i, 2)))
            }
            if (field[0][i] == field[1][i] && field[0][i] == field[2][i] && field[0][i].isNotEmpty()) {
                return Pair(true, listOf(Pair(0, i), Pair(1, i), Pair(2, i)))
            }
        }

        // Check diagonals
        if (field[0][0] == field[1][1] && field[0][0] == field[2][2] && field[0][0].isNotEmpty()) {
            return Pair(true, listOf(Pair(0, 0), Pair(1, 1), Pair(2, 2)))
        }
        if (field[0][2] == field[1][1] && field[0][2] == field[2][0] && field[0][2].isNotEmpty()) {
            return Pair(true, listOf(Pair(0, 2), Pair(1, 1), Pair(2, 0)))
        }

        return Pair(false, emptyList())
    }


    private fun showWinner(message: String, winningButtons: List<Pair<Int, Int>>? = null) {
        textViewStatus.text = message
        buttonPlayAgain.visibility = View.VISIBLE

        if (winningButtons != null) {
            for ((row, col) in winningButtons) {
                buttons[row][col].setBackgroundColor(ContextCompat.getColor(this, R.color.secondaryColor))
            }
        }
        disableButtons()
    }


    private fun disableButtons() {
        for (row in buttons) {
            for (button in row) {
                button.isEnabled = false
            }
        }
    }

    private fun resetGame() {
        roundCount = 0
        playerXTurn = true
        textViewStatus.text = "Player X's Turn"
        buttonPlayAgain.visibility = View.GONE

        for (row in buttons) {
            for (button in row) {
                button.text = ""
                button.isEnabled = true
            }
        }
    }
}
