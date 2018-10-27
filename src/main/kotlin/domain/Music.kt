package domain

internal typealias MusicId = Int

internal data class Music(
    val mid: MusicId,
    val title: String
)

internal typealias Musics = List<Music>

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

internal open class Chart(
    val mid: MusicId, val mode: Mode, val diff: Difficulty
) {
    val rankingPage = "${mode.rankingPage}?mid=$mid&seq=${diff.seq}"
    fun rankingPageWithPage(page: Int) = "$rankingPage&page=$page"

    override fun toString() = "Chart(mid=$mid, mode=$mode, diff=$diff)"
}
