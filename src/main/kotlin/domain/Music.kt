package domain

import org.joda.time.DateTime
import java.math.BigDecimal

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
    val level: BigDecimal
) {
    val generalLevel = level.toInt()

    init {
        if (generalLevel !in (1..10)) throw IllegalArgumentException()
        if (generalLevel in (1..8) && level.scale() != 0) throw IllegalArgumentException()
        if (generalLevel in (9..10) && level.scale() != 1) throw IllegalArgumentException()
    }
}

internal open class Chart(
    val mid: MusicId, val diff: Difficulty, level: BigDecimal?
) {
    val level = level?.let { Level(it) }
}

internal enum class Mode(val rankingPage: String) {
    NORMAL("https://p.eagate.573.jp/game/jubeat/festo/ranking/best_score.html"),
    HARD("https://p.eagate.573.jp/game/jubeat/festo/ranking/best_score_hard.html")
}

internal class RecordHeader(
    mid: MusicId, diff: Difficulty, level: BigDecimal?,
    val mode: Mode, val lastUpdatedAt: DateTime
) : Chart(mid, diff, level) {
    val sourceUrl = "${mode.rankingPage}?mid=$mid&seq=${diff.seq}"

    fun rankingPage(page: Int) = "$sourceUrl&page=$page"

    fun needUpdate(intervalMinutes: Int) =
        lastUpdatedAt.isBefore(DateTime.now().minusMinutes(intervalMinutes))
}
