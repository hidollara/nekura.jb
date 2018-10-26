package application

import domain.RecordQuery
import domain.RivalId

internal class RankerService(
    private val recordQuery: RecordQuery
) {
    fun records(rivalId: RivalId) = recordQuery.recordsOf(rivalId)
}