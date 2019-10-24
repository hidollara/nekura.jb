package application

import domain.core.*
import domain.service.PlayerService

internal class RankerService(
    private val playerQuerent: PlayerQuerent,
    private val playerService: PlayerService
) {
    fun all() = playerQuerent.all()

    fun records(rivalId: Long) =
        playerQuerent.find(RivalId(rivalId)).let { player ->
            playerService.getRecords(player)
        }
}
