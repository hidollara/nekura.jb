package application

import domain.*

internal class RankingUpdateManager(
    private val rankingCommand: RankingCommand,
    private val rankingQuery: RankingQuery,
    private val rankingFetcher: RankingFetcher,
    private val intervalMinutes: Int
) {
    fun pick(): Chart = rankingQuery.earliestUpdatedChart()

    fun updateIfNeed(chart: ChartWithLastUpdateDateTime) =
        chart.takeIf { it.needUpdate(intervalMinutes) }
            ?.let { rankingCommand.pull(rankingFetcher, chart) }
}
