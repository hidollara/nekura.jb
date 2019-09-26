package domain.core

import org.joda.time.DateTime

internal class Ranker(
    val rivalId: RivalId,
    val player: Player,
    val records: Map<RankingId, Record>
)

internal interface RankerQuerent {
    fun all(): List<Ranker>
    fun find(rivalId: RivalId): Ranker
}
