package domain

internal interface MusicService {
    fun all(): List<Music>
    fun find(mid: MusicId): Music
}
