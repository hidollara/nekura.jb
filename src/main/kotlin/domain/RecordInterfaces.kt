package domain

import org.joda.time.DateTime

internal interface RankingFetcher {
    fun fetch(header: RecordHeader): Records
}

internal interface RankingCommand {
    fun pull(fetcher: RankingFetcher, header: RecordHeader)
}

internal interface RankerQuery {
    fun all(): Players
}

internal interface RecordQuery {
    fun recordsOf(header: RecordHeader): Ranking
    fun recordsOf(rivalId: RivalId): Records
    fun recordsBetween(from: DateTime, to: DateTime): Records
}
