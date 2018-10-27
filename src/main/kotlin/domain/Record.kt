package domain

import org.joda.time.DateTime

internal typealias RivalId = Long

internal data class Player(
    val rivalId: RivalId,
    val name: String
) {
    val playdataPage = "https://p.eagate.573.jp/game/jubeat/festo/playdata/index_other.html?rival_id=$rivalId"
}

internal data class Record(
    val chart: Chart, val player: Player,
    val bestScore: Int, val recordedAt: DateTime
)

internal typealias Records = List<Record>

internal data class Ranking(
    val header: RecordHeader, val records: Records
)
