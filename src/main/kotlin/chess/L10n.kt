package chess

object L10n {
    val gameTitle: String
        get() = "Pawns-Only Chess"

    fun enterPlayerName(index: Int): String {
        val indexStr = if (index == 0) "First" else "Second"
        return "$indexStr Player's name:"
    }

    fun playerTurn(name: String) = "$name's turn:"

    fun noPieceAt(cell: String, piece: Piece?, isWhite: Boolean): String {
        val color = if (isWhite) "white" else "black"
        val pieceLabel = piece?.label() ?: Pawn.LABEL
        return "No $color $pieceLabel at $cell"
    }

    fun columnLabel(index: Int) = Char(index + 'a'.code).toString()

    fun gameOver(winner: BoardWinner): String {
        if (winner == BoardWinner.STALEMATE) {
            return "Stalemate!"
        }
        val color = if (winner == BoardWinner.WHITE) "White" else "Black"
        return "$color Wins!"
    }

    val whiteSymbol: String
        get() = "W"

    val blackSymbol: String
        get() = "B"

    val pawn: String
        get() = "pawn"

    val invalidMove: String
        get() = "Invalid Input"

    val exitMessage: String
        get() = "Bye!"
}
