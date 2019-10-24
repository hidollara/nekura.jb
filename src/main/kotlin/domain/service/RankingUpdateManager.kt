package domain.service

import domain.core.*

internal class RankingUpdateManager(
    private val rankingFetcher: RankingFetcher,
    private val rankingRepository: RankingRepository,
    private val intervalMinutes: Int
) {
    fun updateIfNeed(ranking: Ranking) =
        ranking.takeIf { it.needUpdate(intervalMinutes) }
            ?.let { rankingFetcher.fetch(it.id) }
            ?.let { rankingRepository.save(it) }
}
