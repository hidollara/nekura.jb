package domain

internal typealias RivalId = Long

internal data class Player(
    val rivalId: RivalId,
    val name: String
)
