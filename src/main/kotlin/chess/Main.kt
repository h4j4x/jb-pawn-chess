package chess

const val DEFAULT_BOARD_SIZE = "8"
const val EXIT_CMD = "exit"

fun main(args: Array<String>) {
    println(L10n.gameTitle)
    val players = Array(2) { fetchPlayerName(it) }
    val board = Board(readArg(args, "size", DEFAULT_BOARD_SIZE)!!.toInt())
    board.print()
    var turn = 0
    var move: String
    var winner = BoardWinner.NONE
    do {
        println(L10n.playerTurn(players[turn]))
        move = readln()
        if (move != EXIT_CMD) {
            try {
                board.move(turn == 0, move)
                board.print()
                turn = (turn + 1) % players.size
                winner = board.findWinner(turn == 0)
            } catch (e: InvalidMoveException) {
                println(e.message)
            }
        }
    } while (move != EXIT_CMD && winner == BoardWinner.NONE)
    if (winner != BoardWinner.NONE) {
        println(L10n.gameOver(winner))
    }
    println(L10n.exitMessage)
}

fun readArg(args: Array<String>, key: String, defaultValue: String?): String? {
    var keyFound = false
    for (arg in args) {
        if (keyFound) {
            return arg
        }
        keyFound = arg == key
    }
    return defaultValue
}

fun fetchPlayerName(index: Int): String {
    println(L10n.enterPlayerName(index))
    return readln()
}
