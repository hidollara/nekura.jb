package application

import domain.core.*
import domain.service.*
import kotlin.concurrent.timer

internal class RankingAutoUpdateService(
    private val rankingQuerent: RankingQuerent,
    private val rankingUpdateManager: RankingUpdateManager,
    private val autoUpdateInterval: Long
) {
    private fun run() = rankingQuerent.findEarliestUpdated()
        .let { rankingUpdateManager.updateIfNeed(it) }

    fun start() {
        timer(name = "Nekura.jb - Ranking Auto Update Service", period = autoUpdateInterval) {
            this@RankingAutoUpdateService.run()
        }
    }
}
