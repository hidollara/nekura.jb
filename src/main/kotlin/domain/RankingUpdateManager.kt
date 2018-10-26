package domain

internal class RankingUpdateManager(
    private val rankingCommand: RankingCommand,
    private val recordHeaderQuery: RecordHeaderQuery,
    private val rankingFetcher: RankingFetcher,
    private val intervalMinutes: Int
) {
    fun pick(): Chart = recordHeaderQuery.earliestUpdatedRanking()

    fun updateIfNeed(chart: RecordHeader) =
        chart.takeIf { it.needUpdate(intervalMinutes) }
            ?.let { rankingCommand.pull(rankingFetcher, chart) }
}
