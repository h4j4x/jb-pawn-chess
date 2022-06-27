package chess

import kotlin.math.abs
import kotlin.random.Random
import kotlin.random.nextUInt

class Pawn(
    isWhite: Boolean, row: Int, col: Int,
    code: UInt = Random.nextUInt(999u)
) : Piece(isWhite, row, col, code) {
    private var hasMove = false

    companion object {
        val LABEL = L10n.pawn
    }

    override fun symbol() = if (isWhite) L10n.whiteSymbol else L10n.blackSymbol

    override fun label() = LABEL

    override fun copy() = Pawn(isWhite, row, col, code)

    override fun canMoveTo(nextRow: Int, nextCol: Int, nextPiece: Piece?): Boolean {
        val rowDiff = abs(nextRow - row)
        var maxRowDiff = Int.MAX_VALUE
        if (nextPiece == null && col == nextCol) {
            maxRowDiff = if (hasMove) 1 else 2
        } else if (nextPiece != null && abs(col - nextCol) == 1) {
            maxRowDiff = 1
        }
        return maxRowDiff != Int.MAX_VALUE && rowDiff in 1..maxRowDiff
    }

    override fun moveTo(nextRow: Int, nextCol: Int) {
        hasMove = true
        row = nextRow
        col = nextCol
    }

    // return possible next rows for pawn to move
    fun nextRows(): IntArray {
        val colorCoefficient = if (isWhite) 1 else -1
        if (hasMove) {
            return intArrayOf(row + colorCoefficient)
        }
        return intArrayOf(row + colorCoefficient, row + colorCoefficient * 2)
    }
}
