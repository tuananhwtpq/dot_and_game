package com.example.baseproject.activities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.baseproject.R
import java.lang.Exception
import kotlin.coroutines.Continuation

class GameView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    interface GameListener {
        fun onGameUpdate(player1Score: Int, player2Score: Int, currentPlayer: Int)
        fun onGameOver(winner: Int)
    }

    private var gameListener: GameListener? = null

    fun setListener(listener: GameListener) {
        this.gameListener = listener
    }

    private var cols = 4
    private var rows = 4
    private var dotRadius = 25f
    private val strokeWidth = 20f

    private var cellWidth = 0f
    private var cellHeight = 0f

    private val gridPadding = 50f

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val playerOneBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val playerTwoBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private lateinit var horizontalLines: Array<IntArray>
    private lateinit var verticalLines: Array<IntArray>
    private lateinit var boxes: Array<IntArray>

    private var currentPlayer = 1
    private var isPvE = false
    private var difficulty = "hard"

    private var currentPlayerOneScore = 0
    private var currentPlayerTwoScore = 0

    private var TOUCH_SIZE = 50f

    private val handler = Handler(Looper.getMainLooper())

    private var dotPaintColor: Int = resources.getColor(R.color.dotColor)
    private var linePainColor: Int = resources.getColor(R.color.lineColor)

    private var playerOneBoxPaintColor: Int = resources.getColor(R.color.player_one_color)
    private var playerTwoBoxPaintColor: Int = resources.getColor(R.color.player_two_color)


    private val boardBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val cellColorEvenPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val cellColorOddPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var boardBackgroundColor: Int = Color.parseColor("#C39CE2")
    private var cellColorEven: Int = Color.parseColor("#522277")
    private var cellColorOdd: Int = Color.parseColor("#411960")

    private var linePaintColor1: Int = resources.getColor(R.color.lineColor)
    private var linePaintColor2: Int = resources.getColor(R.color.lineColor2)

    private fun initGameData() {
        horizontalLines = Array(rows + 1) { IntArray(cols) { 0 } }
        verticalLines = Array(cols + 1) { IntArray(rows) { 0 } }

        boxes = Array(rows) { IntArray(cols) { 0 } }
        currentPlayer = 1
    }


    init {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.GameView, 0, 0)

        try {

            //setup size of dot with each gridsize

            //setup dot paint color
            dotPaintColor = typedArray.getColor(R.styleable.GameView_dotsColor, Color.WHITE)
            dotPaint.color = dotPaintColor

            //setup Line normal
            linePainColor = typedArray.getColor(R.styleable.GameView_lineColor1, Color.BLUE)
            linePaint.color = linePainColor
            linePaint.strokeWidth = strokeWidth


            //setup right choose box paint of each
            playerOneBoxPaintColor =
                typedArray.getColor(R.styleable.GameView_player_1_color, Color.BLACK)
            playerOneBoxPaint.color = playerOneBoxPaintColor

            playerTwoBoxPaintColor =
                typedArray.getColor(R.styleable.GameView_player_2_color, Color.GREEN)
            playerTwoBoxPaint.color = playerTwoBoxPaintColor

            //setup line paint of player
            linePaintColor1 = typedArray.getColor(R.styleable.GameView_lineColor1, Color.GREEN)
            linePaintColor2 = typedArray.getColor(R.styleable.GameView_lineColor2, Color.GREEN)

            //setup background
            boardBackgroundPaint.color = boardBackgroundColor
            cellColorEvenPaint.color = cellColorEven
            cellColorOddPaint.color = cellColorOdd

        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Loi gameview: ${e.message} - ${e.printStackTrace()}",
                Toast.LENGTH_LONG
            ).show()
        } finally {
            typedArray.recycle()
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateDimensions()
    }

    private fun calculateDimensions() {
        val usableWidth = width - (gridPadding * 2)
        val usableHeight = height - (gridPadding * 2)

        cellWidth = usableWidth / cols.toFloat()
        cellHeight = usableHeight / rows.toFloat()
    }

    fun setGridSize(columns: Int, rows: Int) {
        this.cols = columns
        this.rows = rows
        calculateDimensions()
        setupDotSize(columns)
        initGameData()
        invalidate()
    }

    fun setupDotSize(cols: Int){
        dotRadius = when(cols){
            4 -> 25f
            6 -> 20f
            else -> 15f
        }
    }

    fun setGameMode(playAgainst: String, level: String) {
        this.isPvE = (playAgainst == "computer")
        this.difficulty = level
        initGameData()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cornerRadius = 40f
        canvas.drawRoundRect(
            0f, 0f, width.toFloat(), height.toFloat(),
            cornerRadius, cornerRadius, boardBackgroundPaint
        )

        for (col in 0 until cols) {
            for (row in 0 until rows) {

                val left = gridPadding + (col * cellWidth)
                val top = gridPadding + (row * cellHeight)
                val right = gridPadding + ((col + 1) * cellWidth)
                val bottom = gridPadding + ((row + 1) * cellHeight)

                val status = boxes[row][col]

                if (status == 0) {
                    if ((col + row) % 2 == 0) {
                        canvas.drawRect(left, top, right, bottom, cellColorEvenPaint)
                    } else {
                        canvas.drawRect(left, top, right, bottom, cellColorOddPaint)
                    }
                } else {
                    val paint = if (status == 1) playerOneBoxPaint else playerTwoBoxPaint
                    canvas.drawRect(left, top, right, bottom, paint)
                }
            }
        }

        //draw vertical line
        for (col in 0..cols) {
            for (row in 0 until rows) {
                val lineX = gridPadding + (col * cellWidth)
                val startY = gridPadding + (row * cellHeight)
                val stopY = gridPadding + ((row + 1) * cellHeight)

                val status = verticalLines[col][row]

                if (status != 0) {
                    linePaint.color =
                        if (status == 1) linePaintColor1 else linePaintColor2
                    canvas.drawLine(lineX, startY, lineX, stopY, linePaint)
                }
            }
        }

        //draw horizontal line
        for (row in 0..rows) {
            for (col in 0 until cols) {
                val lineY = gridPadding + (row * cellHeight)
                val startX = gridPadding + (col * cellWidth)
                val stopX = gridPadding + ((col + 1) * cellWidth)

                val status = horizontalLines[row][col]

                if (status != 0) {
                    linePaint.color =
                        if (status == 1) linePaintColor1 else linePaintColor2
                    canvas.drawLine(startX, lineY, stopX, lineY, linePaint)
                }
            }
        }

        //draw dots
        for (i in 0..cols) {
            for (j in 0..rows) {
                val cx = gridPadding + (i * cellWidth)
                val cy = gridPadding + (j * cellHeight)
                canvas.drawCircle(cx, cy, dotRadius, dotPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (isPvE && currentPlayer == 2) return false

        if (event.action == MotionEvent.ACTION_DOWN) {

            //Check touchSize && click ảea
            val rawX = event.x
            val rawY = event.y

            if (rawX < gridPadding - TOUCH_SIZE || rawX > width - (gridPadding - TOUCH_SIZE) ||
                rawY < gridPadding - TOUCH_SIZE || rawY > height - (gridPadding - TOUCH_SIZE)
            ) {
                return false
            }

            val x = rawX.coerceIn(gridPadding, width - gridPadding)
            val y = rawY.coerceIn(gridPadding, height - gridPadding)


            //Check range of top bottom left right
            val c = ((x - gridPadding) / cellWidth).toInt()
            val r = ((y - gridPadding) / cellHeight).toInt()

            val valX = (x - gridPadding) % cellWidth
            val valY = (y - gridPadding) % cellHeight

            val distLeft = valX
            val distRight = cellWidth - valX
            val distTop = valY
            val distBottom = cellHeight - valY

            val minDiff = minOf(distLeft, distRight, distTop, distBottom)

            if (minDiff > TOUCH_SIZE) {
                return false
            }

            var moveMade = false
            var scored = false

            when (minDiff) {
                distTop -> {
                    if (r <= rows && horizontalLines[r][c] == 0) {
                        horizontalLines[r][c] = currentPlayer
                        scored = updateGameLogic(r, c, true)
                        moveMade = true
                    }
                }

                distBottom -> {
                    if (r + 1 <= rows && horizontalLines[r + 1][c] == 0) {
                        horizontalLines[r + 1][c] = currentPlayer
                        scored = updateGameLogic(r + 1, c, true)
                        moveMade = true
                    }
                }

                distLeft -> {
                    if (c <= cols && verticalLines[c][r] == 0) {
                        verticalLines[c][r] = currentPlayer
                        scored = updateGameLogic(r, c, false)
                        moveMade = true
                    }
                }

                distRight -> {
                    if (c + 1 <= cols && verticalLines[c + 1][r] == 0) {
                        verticalLines[c + 1][r] = currentPlayer
                        scored = updateGameLogic(r, c + 1, false)
                        moveMade = true
                    }
                }
            }

            if (moveMade) {
                if (!scored) {
                    currentPlayer = if (currentPlayer == 1) 2 else 1
                }
                invalidate()
                gameListener?.onGameUpdate(
                    currentPlayerOneScore,
                    currentPlayerTwoScore,
                    currentPlayer
                )

                isGameOver()

                if (isPvE && currentPlayer == 2 && !scored) {
                    if (!isGameOver()) {
                        computerTurn()
                    }
                }

            }
        }
        return true
    }

    private fun isBoxCompleted(row: Int, col: Int): Boolean {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return false

        val top = horizontalLines[row][col]
        val bottom = horizontalLines[row + 1][col]
        val left = verticalLines[col][row]
        val right = verticalLines[col + 1][row]

        return top != 0 && bottom != 0 && left != 0 && right != 0
    }

    private fun updateGameLogic(newRow: Int, newCol: Int, isHorizontal: Boolean): Boolean {
        var scored = false

        if (isHorizontal) {
            if (isBoxCompleted(newRow, newCol)) {
                boxes[newRow][newCol] = currentPlayer
                scored = true
                updatePlayerScore(currentPlayer)
            }
            if (isBoxCompleted(newRow - 1, newCol)) {
                boxes[newRow - 1][newCol] = currentPlayer
                scored = true
                updatePlayerScore(currentPlayer)
            }
        } else {
            if (isBoxCompleted(newRow, newCol)) {
                boxes[newRow][newCol] = currentPlayer
                scored = true
                updatePlayerScore(currentPlayer)
            }
            if (isBoxCompleted(newRow, newCol - 1)) {
                boxes[newRow][newCol - 1] = currentPlayer
                scored = true
                updatePlayerScore(currentPlayer)
            }
        }
        return scored
    }

    private fun updatePlayerScore(currentPlayer: Int) {
        if (currentPlayer == 1) {
            currentPlayerOneScore++
        } else {
            currentPlayerTwoScore++
        }
    }

    private fun getLinesCountForBox(r: Int, c: Int): Int {
        var count = 0
        if (horizontalLines[r][c] != 0) count++
        if (horizontalLines[r + 1][c] != 0) count++
        if (verticalLines[c][r] != 0) count++
        if (verticalLines[c + 1][r] != 0) count++
        return count
    }

    private fun computerTurn() {
        //get all available move
        val availableMoves = ArrayList<GameMove>()

        //get all horizontal line available
        for (r in 0..rows) {
            for (c in 0 until cols) {
                if (horizontalLines[r][c] == 0) availableMoves.add(GameMove(r, c, true))
            }
        }
        //get all vertical line available
        for (c in 0..cols) {
            for (r in 0 until rows) {
                if (verticalLines[c][r] == 0) availableMoves.add(GameMove(r, c, false))
            }
        }

        if (availableMoves.isEmpty()) return

        val goodMoves = ArrayList<GameMove>()
        val safeMoves = ArrayList<GameMove>()
        val badMoves = ArrayList<GameMove>()

        for (move in availableMoves) {
            if (isMoveScoring(move)) {
                goodMoves.add(move)
            } else if (isMoveSafe(move)) {
                safeMoves.add(move)
            } else {
                badMoves.add(move)
            }
        }

        val selectedMove: GameMove = when (difficulty) {
            "hard" -> {
                if (goodMoves.isNotEmpty()) goodMoves.random()
                else if (safeMoves.isNotEmpty()) safeMoves.random()
                else badMoves.random()
            }

            "medium" -> {
                if (goodMoves.isNotEmpty()) goodMoves.random()
                else if (safeMoves.isNotEmpty() && (0..10).random() > 3) safeMoves.random()
                else availableMoves.random()
            }

            else -> {
                if (goodMoves.isNotEmpty()) goodMoves.random()
                else availableMoves.random()
            }
        }

        handler.postDelayed({
            applyMove(selectedMove)
        }, 500)
    }

    private fun isMoveScoring(move: GameMove): Boolean {
        return if (move.isHorizontal) {
            val boxBelow = if (move.row < rows) getLinesCountForBox(move.row, move.col) else -1
            val boxAbove = if (move.row > 0) getLinesCountForBox(move.row - 1, move.col) else -1
            boxBelow == 3 || boxAbove == 3
        } else {
            val boxRight = if (move.col < cols) getLinesCountForBox(move.row, move.col) else -1
            val boxLeft = if (move.col > 0) getLinesCountForBox(move.row, move.col - 1) else -1
            boxRight == 3 || boxLeft == 3
        }
    }

    private fun isMoveSafe(move: GameMove): Boolean {
        if (move.isHorizontal) {
            val boxBelow = if (move.row < rows) getLinesCountForBox(move.row, move.col) else -1
            val boxAbove = if (move.row > 0) getLinesCountForBox(move.row - 1, move.col) else -1
            return (boxBelow < 2) && (boxAbove < 2)
        } else {
            val boxRight = if (move.col < cols) getLinesCountForBox(move.row, move.col) else -1
            val boxLeft = if (move.col > 0) getLinesCountForBox(move.row, move.col - 1) else -1
            return (boxRight < 2) && (boxLeft < 2)
        }
    }

    private fun applyMove(move: GameMove) {
        if (move.isHorizontal) {
            horizontalLines[move.row][move.col] = 2
        } else {
            verticalLines[move.col][move.row] = 2
        }

        val scored = updateGameLogic(move.row, move.col, move.isHorizontal)
        invalidate()

        gameListener?.onGameUpdate(currentPlayerOneScore, currentPlayerTwoScore, currentPlayer)

        if (scored) {
            if (!isGameOver()) {
                computerTurn()
            }
        } else {
            currentPlayer = 1
            gameListener?.onGameUpdate(currentPlayerOneScore, currentPlayerTwoScore, currentPlayer)
        }
    }

    private fun isGameOver(): Boolean {
        val isOver = (currentPlayerOneScore + currentPlayerTwoScore) == (rows * cols)
        val scoreHigher = currentPlayerOneScore - currentPlayerTwoScore
        if (isOver && scoreHigher > 0) {
            gameListener?.onGameOver(1)
        } else if (isOver && scoreHigher < 0) {
            gameListener?.onGameOver(2)
        } else if (isOver && scoreHigher == 0) {
            gameListener?.onGameOver(0)
        }
        return (currentPlayerOneScore + currentPlayerTwoScore) == (rows * cols)
    }
}

data class GameMove(val row: Int, val col: Int, val isHorizontal: Boolean)