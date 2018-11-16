package domain

import org.joda.time.DateTime

internal interface RankingFetcher {
    fun fetch(header: RecordHeader): Records
}

internal interface RankingCommand {
    fun pull(fetcher: RankingFetcher, header: RecordHeader)
}

internal interface RecordHeaderQuery {
    fun rankingHeader(mid: MusicId, mode: Mode, diff: Difficulty): RecordHeader
    fun earliestUpdatedRanking(): RecordHeader
}

internal interface RecordQuery {
    fun recordsOf(header: RecordHeader): Records
    fun recordsOf(rivalId: RivalId): Records
    fun recordsBetween(from: DateTime, to: DateTime): Records
}
