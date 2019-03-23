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

internal enum class Mode(val rankingPage: String) {
    NORMAL("https://p.eagate.573.jp/game/jubeat/festo/ranking/best_score.html"),
    HARD("https://p.eagate.573.jp/game/jubeat/festo/ranking/best_score_hard.html")
}

internal data class RecordHeader(
    val mid: MusicId, val diff: Difficulty, val mode: Mode, val lastUpdatedAt: DateTime
) {
    val sourceUrl = "${mode.rankingPage}?mid=$mid&seq=${diff.seq}"

    fun rankingPage(page: Int) = "$sourceUrl&page=$page"

    fun needUpdate(intervalMinutes: Int) =
        lastUpdatedAt.isBefore(DateTime.now().minusMinutes(intervalMinutes))
}
