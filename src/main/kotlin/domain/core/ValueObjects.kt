package domain.core

import org.joda.time.DateTime

internal typealias MusicId = Int

internal typealias MusicTitle = String

internal enum class Difficulty(
    val seq: Int
) {
    BASIC(0), ADVANCED(1), EXTREME(2)
}

internal data class Level(
    val majorLevel: Int,
    val minorLevel: Int
) {
    constructor(levelId: Int?): this(levelId?.let { it / 10 } ?: 0, levelId?.let { it % 10 } ?: 0)

    val id = majorLevel * 10 + minorLevel

    init {
        require(
            id == 0
            || (majorLevel in (1..8) && minorLevel == 0)
            || majorLevel in (9..10)
        )
    }
}

internal data class Chart(
    val mid: MusicId,
    val diff: Difficulty,
    val level: Level
)

internal enum class Mode(
    val rankingUrl: String
) {
    NORMAL("https://p.eagate.573.jp/game/jubeat/festo/ranking/best_score.html"),
    HARD("https://p.eagate.573.jp/game/jubeat/festo/ranking/best_score_hard.html")
}

internal data class RankingId(
    val mid: MusicId,
    val diff: Difficulty,
    val mode: Mode
) {
    fun sourceUrl(page: Int = 1) = "${mode.rankingUrl}?mid=${mid}&seq=${diff.seq}&page=${page}"
}

internal data class RivalId(
    val rivalId: Long
) {
    val playdataUrl = "https://p.eagate.573.jp/game/jubeat/festo/playdata/index_other.html?rival_id=${rivalId}"
}

internal data class Player(
    val rivalId: RivalId,
    val name: String
)

internal data class Record(
    val rankingId: RankingId,
    val player: Player,
    val score: Int,
    val recordedAt: DateTime
)
