package application

import domain.RecordQuery
import domain.RivalId

internal class RankerService(
    private val recordQuery: RecordQuery
) {
    fun rankerRecords(rivalId: RivalId) = recordQuery.recordsOf(rivalId)
}