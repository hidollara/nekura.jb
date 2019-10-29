package domain

internal typealias RivalId = Long

internal data class Player(
    val rivalId: RivalId,
    val name: String
)

internal interface PlayerService {
    fun all(): List<Player>
    fun find(rivalId: RivalId): Player
    fun getRecords(player: Player): List<Record>
}
