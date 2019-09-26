package application

import domain.core.*
import kotlin.concurrent.timer

internal class MusicAutoUpdateService(
    private val musicFetcher: MusicFetcher,
    private val musicCommander: MusicCommander,
    private val autoUpdateInterval: Long
) {
    private fun run() = musicFetcher.fetchAll().let { musicCommander.save(it) }

    fun start() {
        timer(name = "Nekura.jb - Music Auto Update Service", period = autoUpdateInterval) {
            this@MusicAutoUpdateService.run()
        }
    }
}
