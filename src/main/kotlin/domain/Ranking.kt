package domain

import org.joda.time.DateTime

internal enum class Mode(val rankingPage: String) {
    NORMAL("https://p.eagate.573.jp/game/jubeat/festo/ranking/best_score.html"),
    HARD("https://p.eagate.573.jp/game/jubeat/festo/ranking/best_score_hard.html")
}

internal enum class Difficulty(val seq: Int) {
    BASIC(0), ADVANCED(1), EXTREME(2)
}

internal typealias RivalId = Long

internal data class Player(
    val rivalId: RivalId,
    val name: String
) {
    val playdataPage = "https://p.eagate.573.jp/game/jubeat/festo/playdata/index_other.html?rival_id=$rivalId"
}

internal open class Chart(
    val mid: MusicId, val mode: Mode, val diff: Difficulty
) {
    val rankingPage = "${mode.rankingPage}?mid=$mid&seq=${diff.seq}"
    fun rankingPageWithPage(page: Int) = "$rankingPage&page=$page"

    override fun toString() = "Chart(mid=$mid, mode=$mode, diff=$diff)"
}

internal class RecordHeader(
    mid: MusicId, mode: Mode, diff: Difficulty, val lastUpdatedAt: DateTime
) : Chart(mid, mode, diff) {
    fun needUpdate(intervalMinutes: Int) =
        lastUpdatedAt.isBefore(DateTime.now().minusMinutes(intervalMinutes))
}

internal data class Record(
    val chart: Chart, val player: Player,
    val bestScore: Int, val recordedAt: DateTime
)

internal typealias Records = List<Record>
