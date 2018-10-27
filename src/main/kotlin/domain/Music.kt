package domain

import org.joda.time.DateTime

internal typealias MusicId = Int

internal data class Music(
    val mid: MusicId,
    val title: String
)

internal typealias Musics = List<Music>

internal enum class Difficulty(val seq: Int) {
    BASIC(0), ADVANCED(1), EXTREME(2)
}

internal data class Level(
    val mainLevel: Int,
    val subLevel: Int = 0
) {
    init {
        if (mainLevel !in (1..10)) throw IllegalArgumentException()
        if (subLevel !in (1..10)) throw IllegalArgumentException()
        if (mainLevel in (1..8) && subLevel != 0) throw IllegalArgumentException()
    }
    val level = if (mainLevel <= 8) "$mainLevel" else "$mainLevel.$subLevel"
}

internal open class Chart(
    val mid: MusicId, val mode: Mode, val diff: Difficulty, val level: Level?
) {
    val rankingPage = "${mode.rankingPage}?mid=$mid&seq=${diff.seq}"
    fun rankingPageWithPage(page: Int) = "$rankingPage&page=$page"

    override fun toString() = "Chart(mid=$mid, mode=$mode, diff=$diff)"
}

internal enum class Mode(val rankingPage: String) {
    NORMAL("https://p.eagate.573.jp/game/jubeat/festo/ranking/best_score.html"),
    HARD("https://p.eagate.573.jp/game/jubeat/festo/ranking/best_score_hard.html")
}

internal class RecordHeader(
    mid: MusicId, mode: Mode, diff: Difficulty, val lastUpdatedAt: DateTime
) : Chart(mid, mode, diff, /* TODO */ null) {
    fun needUpdate(intervalMinutes: Int) =
        lastUpdatedAt.isBefore(DateTime.now().minusMinutes(intervalMinutes))
}
