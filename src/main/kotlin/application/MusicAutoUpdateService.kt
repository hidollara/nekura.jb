package application

import domain.MusicCommand
import domain.MusicFetcher
import kotlin.concurrent.timer


internal class MusicAutoUpdateService(
    private val musicCommand: MusicCommand,
    private val musicFetcher: MusicFetcher,
    private val autoUpdateInterval: Long
) {
    private fun run() = musicCommand.pull(musicFetcher)

    fun start() {
        timer(name = "Nekura.jb - Ranking Auto Updater", period = autoUpdateInterval) {
            this@MusicAutoUpdateService.run()
        }
    }
}
