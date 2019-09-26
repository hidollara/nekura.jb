package application

import domain.core.*

internal class MusicService(
    private val musicQuerent: MusicQuerent
) {
    fun all() = musicQuerent.all()
}
