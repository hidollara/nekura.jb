package domain

import org.joda.time.DateTime

internal interface RankingFetcher {
    fun fetch(chart: Chart): Records
}

internal interface RankingCommand {
    fun pull(fetcher: RankingFetcher, chart: Chart)
}

internal interface RankingQuery {
    fun rankingHeader(mid: MusicId, mode: Mode, diff: Difficulty): RecordHeader
    fun earliestUpdatedRanking(): RecordHeader
    fun ranking(header: RecordHeader): Records
    fun ranking(mid: MusicId, mode: Mode, diff: Difficulty) = ranking(rankingHeader(mid, mode, diff))
    fun recordsBetween(from: DateTime, to: DateTime): Records
    fun recordsOf(rivalId: RivalId): Records
}
