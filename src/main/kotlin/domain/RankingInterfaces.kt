package domain

import org.joda.time.DateTime

internal interface RankingFetcher {
    fun fetch(chart: Chart): Records
}

internal interface RankingCommand {
    fun pull(fetcher: RankingFetcher, chart: Chart)
}

internal interface RankingQuery {
    fun findChart(mid: MusicId, mode: Mode, diff: Difficulty): ChartWithLastUpdateDateTime
    fun earliestUpdatedChart(): ChartWithLastUpdateDateTime
    fun ranking(chart: Chart): Records
    fun recordsBetween(from: DateTime, to: DateTime): Records
    fun recordsOf(rivalId: RivalId): Records
}
