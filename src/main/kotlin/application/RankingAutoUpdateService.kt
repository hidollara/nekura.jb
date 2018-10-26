package application

import domain.RankingCommand
import domain.RankingFetcher
import domain.RankingUpdateManager
import kotlin.concurrent.timer

internal class RankingAutoUpdateService(
    private val rankingCommand: RankingCommand,
    private val rankingFetcher: RankingFetcher,
    private val manager: RankingUpdateManager
) {
    private fun run() {
        manager.pick().let { rankingCommand.pull(rankingFetcher, it) }
    }

    fun start() {
        timer(name = "Nekura.jb - Ranking Auto Updater", period = 60000) { this@RankingAutoUpdateService.run() }
    }
}
