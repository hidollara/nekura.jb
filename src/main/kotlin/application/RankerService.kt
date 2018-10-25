package application

import domain.RankingQuery
import domain.RivalId

internal class RankerService(
    private val rankingQuery: RankingQuery
) {
    fun rankerRecords(rivalId: RivalId) = rankingQuery.recordsOf(rivalId)
}