package chess

import kotlin.math.abs

data class Move(val piece: Piece, val nextRow: Int, val nextCol: Int, val nextPiece: Piece?) {
    fun isPawnDoubleStep(col: Int): Boolean = piece is Pawn && col == nextCol && abs(piece.row - nextRow) == 2

    fun pieceMoved(): Piece {
        val movedPiece = piece.copy()
        movedPiece.row = nextRow
        movedPiece.col = nextCol
        return movedPiece
    }
}
