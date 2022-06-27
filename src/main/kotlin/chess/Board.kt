package chess

class Board(private val size: Int) {
    private val pieces = mutableListOf<Piece>()
    private val moves = mutableListOf<Move>()
    private val columns = Array(size) { L10n.columnLabel(it)[0].code }
    private val moveRegex: Regex

    init {
        // regex
        val columnPattern = "[${columns[0].toChar()}-${columns.last().toChar()}]"
        val rowPattern = "[1-$size]"
        moveRegex = "$columnPattern$rowPattern$columnPattern$rowPattern".toRegex()
        // pawns
        for (position in size until size * 2) {
            pieces.add(Pawn(true, rowOf(position), columnOf(position)))
            var sepRows = size / 2
            if (size % 2 == 0) {
                sepRows++
            }
            val blackPosition = position + (size * sepRows)
            pieces.add(Pawn(false, rowOf(blackPosition), columnOf(blackPosition)))
        }
    }

    fun print() {
        printRowSeparator()
        val row = Array(size) { " " }
        var colIndex = row.lastIndex
        var rowIndex = size - 1
        for (position in (size * size - 1) downTo 0) {
            row[colIndex--] = pieceAt(position)?.symbol() ?: " "
            if (colIndex < 0) {
                println("${rowIndex + 1} | ${row.joinToString(" | ")} |")
                printRowSeparator()
                colIndex = row.lastIndex
                rowIndex--
            }
        }
        print("   ")
        repeat(size) {
            print(" ${columns[it].toChar()}  ")
        }
        println()
    }

    fun move(isWhite: Boolean, move: String) {
        if (!moveRegex.matches(move)) {
            throw InvalidMoveException(L10n.invalidMove)
        }
        val cell = move.substring(0, 2)
        val piece = pieceAt(parsePosition(cell))
        if (piece == null || piece.isWhite != isWhite) {
            throw InvalidMoveException(L10n.noPieceAt(cell, piece, isWhite))
        }
        val nextCell = move.substring(2)
        val nextPosition = parsePosition(nextCell)
        val nextRow = rowOf(nextPosition)
        val nextCol = columnOf(nextPosition)
        val pieceMove = pieceCanMoveTo(piece, nextRow, nextCol)
        if (pieceMove.value) {
            moves.add(Move(piece.copy(), nextRow, nextCol, pieceMove.piece))
            piece.moveTo(nextRow, nextCol)
            if (pieceMove.piece != null) {
                pieces.remove(pieceMove.piece)
            }
        } else {
            throw InvalidMoveException(L10n.invalidMove)
        }
    }

    fun findWinner(nextMoveIsWhite: Boolean): BoardWinner {
        var piecesCanMove = false
        var piecesCount = 0
        for (piece in pieces) {
            if (piece.isWhite == nextMoveIsWhite) {
                piecesCount++
                piecesCanMove = piecesCanMove || pieceCanMove(piece)
            } else if (piece is Pawn) {
                // check if reached final row
                val finalRow = if (piece.isWhite) size - 1 else 0
                if (piece.row == finalRow) {
                    return if (piece.isWhite) BoardWinner.WHITE else BoardWinner.BLACK
                }
            }
        }
        if (piecesCount == 0) {
            return if (nextMoveIsWhite) BoardWinner.BLACK else BoardWinner.WHITE
        }
        if (!piecesCanMove) {
            return BoardWinner.STALEMATE
        }
        return BoardWinner.NONE
    }

    private fun pieceCanMoveTo(piece: Piece, nextRow: Int, nextCol: Int): PieceBoolean {
        var nextPiece = pieceAt(nextRow, nextCol)
        if (nextPiece != null && nextPiece.isWhite == piece.isWhite) {
            return PieceBoolean(false)
        }
        if (piece.canMoveTo(nextRow, nextCol, nextPiece)) {
            return PieceBoolean(true, nextPiece)
        }
        if (piece is Pawn && moves.lastOrNull()?.isPawnDoubleStep(nextCol) == true) {
            // en passant capture
            val lastMove = moves.last()
            val stepRow = (lastMove.piece.row + lastMove.nextRow) / 2
            nextPiece = lastMove.pieceMoved()
            if (piece.canMoveTo(stepRow, nextCol, nextPiece)) {
                return PieceBoolean(true, nextPiece)
            }
        }
        return PieceBoolean(false)
    }

    private fun pieceCanMove(piece: Piece): Boolean {
        if (piece is Pawn) {
            // check can move forward or capture
            val nextRows = piece.nextRows()
            val nextCols = intArrayOf(piece.col - 1, piece.col, piece.col + 1)
            for (nextRow in nextRows) {
                for (column in nextCols) {
                    if (piece.canMoveTo(nextRow, column, pieceAt(nextRow, column))) {
                        return true
                    }
                }
            }
            return false
        }
        return true
    }

    private fun pieceAt(position: Int) = pieceAt(rowOf(position), columnOf(position))

    private fun pieceAt(row: Int, col: Int) = pieces.firstOrNull { it.row == row && it.col == col }

    private fun printRowSeparator() {
        print("  +")
        repeat(size) {
            print("---+")
        }
        println()
    }

    // parse board human coordinates into array position index
    private fun parsePosition(cell: String) = (cell[1].code - 49) * size + columns.indexOf(cell[0].code)

    private fun rowOf(position: Int) = position / size

    private fun columnOf(position: Int) = position % size

    inner class PieceBoolean(val value: Boolean, val piece: Piece? = null)
}
