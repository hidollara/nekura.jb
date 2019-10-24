package domain.core

internal enum class Difficulty(
    val seq: Int
) {
    BASIC(0), ADVANCED(1), EXTREME(2)
}

internal data class Level(
    val majorLevel: Int,
    val minorLevel: Int
) {
    constructor(levelId: Int?): this(
        levelId?.let { it / 10 } ?: 0,
        levelId?.let { it % 10 } ?: 0
    )

    val id = majorLevel * 10 + minorLevel

    init {
        require(
            when (majorLevel) {
                0 -> minorLevel == 0
                in 1..8 -> minorLevel == 0
                in 9..10 -> minorLevel in 0..9
                else -> false
            }
        )
    }

    override fun toString() = when (majorLevel) {
        0 -> "-"
        in 1..8 -> "${majorLevel}"
        in 9..10 -> "${majorLevel}.${minorLevel}"
        else -> "-"
    }
}

internal data class Chart(
    val mid: MusicId,
    val diff: Difficulty,
    val level: Level
) {
    fun getRankingId(mode: Mode) = RankingId(mid, diff, mode)
    fun getRankingIds() = Mode.values().map { getRankingId(it) }
}
