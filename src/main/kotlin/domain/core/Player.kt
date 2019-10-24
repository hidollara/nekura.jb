package domain.core

internal data class RivalId(
    val rivalId: Long
) {
    val playdataUrl = "https://p.eagate.573.jp/game/jubeat/festo/playdata/index_other.html?rival_id=${rivalId}"
}

internal data class Player(
    val rivalId: RivalId,
    val name: String
)

internal interface PlayerQuerent {
    fun all(): List<Player>
    fun find(rivalId: RivalId): Player
}
