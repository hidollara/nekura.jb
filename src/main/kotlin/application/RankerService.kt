package application

import domain.Player
import domain.RecordQuery
import domain.RivalId

internal class RankerService(
    private val recordQuery: RecordQuery
) {
    fun all(): List<Player> = TODO()

    fun records(rivalId: RivalId) = recordQuery.recordsOf(rivalId)
}