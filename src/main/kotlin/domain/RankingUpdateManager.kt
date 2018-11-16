package domain

internal class RankingUpdateManager(
    private val rankingCommand: RankingCommand,
    private val recordHeaderQuery: RecordHeaderQuery,
    private val rankingFetcher: RankingFetcher,
    private val intervalMinutes: Int
) {
    fun pick(): RecordHeader = recordHeaderQuery.earliestUpdatedRanking()

    fun updateIfNeed(header: RecordHeader) =
        header.takeIf { it.needUpdate(intervalMinutes) }
            ?.let { rankingCommand.pull(rankingFetcher, it) }
}
