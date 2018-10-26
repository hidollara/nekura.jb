package application

import domain.*

internal class MusicService(private val musicQuery: MusicQuery) {
    fun all(): Musics = musicQuery.all()

    fun detail(mid: MusicId): Music = TODO()
}