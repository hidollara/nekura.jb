package domain

import org.joda.time.DateTime

internal enum class Difficulty(
    val seq: Int
) {
    BASIC(0), ADVANCED(1), EXTREME(2)
}

internal enum class Mode(
    val rankingUrl: String
) {
    NORMAL("https://p.eagate.573.jp/game/jubeat/festo/ranking/best_score.html"),
    HARD("https://p.eagate.573.jp/game/jubeat/festo/ranking/best_score_hard.html")
}

internal data class RankingId(
    val music: Music,
    val diff: Difficulty,
    val mode: Mode
) {
    val sourceUrl = "${mode.rankingUrl}?mid=${music.mid}&seq=${diff.seq}"
    fun sourceUrlWith(page: Int = 1) = "${sourceUrl}&page=${page}"
}

internal data class Record(
    val rankingId: RankingId,
    val player: Player,
    val score: Int,
    val recordedAt: DateTime
)

internal class Ranking(
    val id: RankingId,
    val lastUpdatedAt: DateTime,
    val records: List<Record>
) {
    fun needUpdate(intervalMinutes: Int) =
        lastUpdatedAt.isBefore(DateTime.now().minusMinutes(intervalMinutes))
}

internal interface RankingFetcher {
    fun fetch(rankingId: RankingId): Ranking
}

internal interface RankingRepository {
    fun save(ranking: Ranking)
}

internal interface RankingService {
    fun findEarliestUpdated(): Ranking
}
