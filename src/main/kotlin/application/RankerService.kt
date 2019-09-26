package application

import domain.core.*

internal class RankerService(
    private val rankerQuerent: RankerQuerent
) {
    fun all() = rankerQuerent.all()

    fun records(rivalId: RivalId) = rankerQuerent.find(rivalId)
}
