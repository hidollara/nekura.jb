package application

import domain.*

internal class RankingService(
    private val headerQuery: RecordHeaderQuery,
    private val recordQuery: RecordQuery,
    private val manager: RankingUpdateManager
) {
    fun ranking(mid: MusicId, mode: Mode, diff: Difficulty): Ranking =
        headerQuery.rankingHeader(mid, mode, diff).let { header ->
            manager.updateIfNeed(header)
            Ranking(header, recordQuery.recordsOf(header))
        }
}