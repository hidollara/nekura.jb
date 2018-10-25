package application

import domain.*

internal class MusicService(
    private val musicQuery: MusicQuery,
    private val rankingQuery: RankingQuery,
    private val rankingUpdateManager: RankingUpdateManager
) {
    fun all(): Musics = musicQuery.all()

    fun ranking(mid: MusicId, mode: Mode, diff: Difficulty): Records =
        rankingQuery.findChart(mid, mode, diff).let { chart ->
            rankingUpdateManager.updateIfNeed(chart)
            rankingQuery.ranking(chart)
        }
}