package application

import domain.RankingCommand
import domain.RankingFetcher
import domain.RankingUpdateManager
import kotlin.concurrent.timer

internal class RankingAutoUpdateService(
    private val rankingCommand: RankingCommand,
    private val rankingFetcher: RankingFetcher,
    private val rankingUpdateManager: RankingUpdateManager,
    private val autoUpdateInterval: Long
) {
    private fun run() = rankingUpdateManager.pick().let { rankingCommand.pull(rankingFetcher, it) }

    fun start() {
        timer(name = "Nekura.jb - Ranking Auto Updater", period = autoUpdateInterval) {
            this@RankingAutoUpdateService.run()
        }
    }
}
