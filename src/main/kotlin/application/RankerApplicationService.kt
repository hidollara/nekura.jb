package application

import domain.*

internal class RankerApplicationService(
    private val playerService: PlayerService
) {
    fun all() = playerService.all()

    fun records(rivalId: Long) =
        playerService.find(RivalId(rivalId)).let { player ->
            playerService.getRecords(player)
        }
}
