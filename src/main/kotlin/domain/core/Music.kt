package domain.core

internal class Music(
    val mid: MusicId,
    val title: MusicTitle,
    val charts: Map<Difficulty, Chart>
)

internal interface MusicFetcher {
    fun fetchAll(): List<Music>
}

internal interface MusicCommander {
    fun save(musics: List<Music>)
}

internal interface MusicQuerent {
    fun all(): List<Music>
    fun find(mid: MusicId): Music
}
