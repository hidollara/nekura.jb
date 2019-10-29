package application

import domain.*

internal class PlayerApplicationService(
    private val playerService: PlayerService
) {
    fun all() = playerService.all()

    fun records(rivalId: Long) =
        playerService.find(rivalId).let { player ->
            playerService.getRecords(player)
        }
}
