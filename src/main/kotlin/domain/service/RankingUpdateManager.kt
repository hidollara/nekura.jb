package domain.service

import domain.core.*

internal class RankingUpdateManager(
    private val rankingFetcher: RankingFetcher,
    private val rankingCommander: RankingCommander,
    private val intervalMinutes: Int
) {
    fun updateIfNeed(ranking: Ranking) =
        ranking.takeIf { it.needUpdate(intervalMinutes) }
            ?.let { rankingFetcher.fetch(it.id) }
            ?.let { rankingCommander.save(it) }
}
