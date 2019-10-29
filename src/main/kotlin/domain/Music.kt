package domain

internal typealias MusicId = Int

internal data class Music(
    val mid: MusicId,
    val title: String
)

internal interface MusicFetcher {
    fun fetchAll(): List<Music>
}

internal interface MusicRepository {
    fun save(musics: List<Music>)
}
