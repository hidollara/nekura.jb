package domain.core

import org.joda.time.DateTime

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

internal interface RankingCommander {
    fun save(ranking: Ranking)
}

internal interface RankingQuerent {
    fun findEarliestUpdated(): Ranking
}