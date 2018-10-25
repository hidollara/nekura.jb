package application

import domain.RankingQuery
import org.joda.time.DateTime

internal class LatestUpdateService(private val rankingQuery: RankingQuery) {
    fun latestUpdates(days: Int) =
        rankingQuery.recordsBetween(DateTime.now().minusDays(days), DateTime.now())
}