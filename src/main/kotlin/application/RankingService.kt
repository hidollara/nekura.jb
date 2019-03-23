package application

import domain.*

internal class RankingService(
    private val recordHeaderQuery: RecordHeaderQuery,
    private val recordQuery: RecordQuery,
    private val rankingUpdateManager: RankingUpdateManager
) {
    fun ranking(mid: MusicId, diff: Difficulty, mode: Mode): Ranking =
        recordHeaderQuery.find(mid, diff, mode).let { header ->
            rankingUpdateManager.updateIfNeed(header)
            recordQuery.recordsOf(header)
        }
}
