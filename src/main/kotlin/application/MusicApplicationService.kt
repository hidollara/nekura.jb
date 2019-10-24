package application

import domain.*

internal class MusicApplicationService(
    private val musicService: MusicService
) {
    fun all() = musicService.all()
}
