package domain


internal interface PlayerService {
    fun all(): List<Player>
    fun find(rivalId: RivalId): Player
    fun getRecords(player: Player): List<Record>
}
