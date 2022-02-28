package pt.isec.theforgotten.noactivity

object MyObject {
    private var _valor = 1000

    init {
        _valor = 50000
    }

    val valor : Int
        get() { return --_valor }
}