package domain.core

internal typealias MusicId = Int

internal class Music(
    val mid: MusicId,
    val title: String,
    val charts: Map<Difficulty, Chart>
) {
    fun getRankingId(diff: Difficulty, mode: Mode) = charts[diff]!!.getRankingId(mode)
    fun getRankingIds() = Difficulty.values().map { charts[it]!!.getRankingIds() }.flatten()
}

internal fun buildMusic(
    mid: MusicId, title: String,
    bscLvId: Int?, advLvId: Int?, extLvId: Int?
) =
    Music(
        mid,
        title,
        mapOf(
            Difficulty.BASIC to Chart(mid, Difficulty.BASIC, Level(bscLvId)),
            Difficulty.ADVANCED to Chart(mid, Difficulty.ADVANCED, Level(advLvId)),
            Difficulty.EXTREME to Chart(mid, Difficulty.EXTREME, Level(extLvId))
        )
    )

internal interface MusicFetcher {
    fun fetchAll(): List<Music>
}

internal interface MusicRepository {
    fun save(musics: List<Music>)
}

internal interface MusicQuerent {
    fun all(): List<Music>
    fun find(mid: MusicId): Music
}
