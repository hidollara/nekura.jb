package application

import domain.Player
import domain.RankerQuery
import domain.RecordQuery
import domain.RivalId

internal class RankerService(
    private val rankerQuery: RankerQuery,
    private val recordQuery: RecordQuery
) {
    fun all(): List<Player> = rankerQuery.all()

    fun records(rivalId: RivalId) = recordQuery.recordsOf(rivalId)
}
