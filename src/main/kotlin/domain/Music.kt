package domain

internal typealias MusicId = Int

internal data class Music(
    val mid: MusicId,
    val title: String
)

internal data class Level(
    private val mainLevel: Int,
    private val subLevel: Int = 0
) {
    init {
        if (mainLevel !in (1..10)) throw IllegalArgumentException()
        if (subLevel !in (1..10)) throw IllegalArgumentException()
        if (mainLevel in (1..8) && subLevel != 0) throw IllegalArgumentException()
    }
    val level = if (mainLevel <= 8) "$mainLevel" else "$mainLevel.$subLevel"
}

internal typealias Musics = List<Music>
