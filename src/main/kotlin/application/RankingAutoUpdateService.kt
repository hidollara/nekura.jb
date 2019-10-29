package application

import domain.*
import kotlin.concurrent.timer

internal class RankingAutoUpdateService(
    private val rankingFetcher: RankingFetcher,
    private val rankingRepository: RankingRepository,
    private val rankingService: RankingService,
    private val intervalMinutes: Int,
    private val autoUpdateInterval: Long
) {
    private fun run() = rankingService.findEarliestUpdated()
        .takeIf { it.needUpdate(intervalMinutes) }
        ?.let { rankingFetcher.fetch(it.id) }
        ?.let { rankingRepository.save(it) }

    fun start() {
        timer(name = "Nekura.jb - Ranking Auto Update Service", period = autoUpdateInterval) {
            this@RankingAutoUpdateService.run()
        }
    }
}
