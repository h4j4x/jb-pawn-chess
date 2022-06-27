package chess

abstract class Piece(val isWhite: Boolean, var row: Int, var col: Int, val code: UInt) {
    abstract fun symbol(): String

    abstract fun label(): String

    abstract fun canMoveTo(nextRow: Int, nextCol: Int, nextPiece: Piece?): Boolean

    abstract fun moveTo(nextRow: Int, nextCol: Int)

    abstract fun copy(): Piece

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is Piece && code == other.code
    }

    override fun hashCode(): Int {
        var result = code.toInt()
        result = 31 * result + isWhite.hashCode()
        return result
    }
}
