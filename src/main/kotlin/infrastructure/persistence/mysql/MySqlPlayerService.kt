package infrastructure.persistence.mysql

import domain.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

internal class MySqlPlayerService(private val db: Database) : PlayerService {
    override fun all() = transaction(db) {
        Schema.Players
            .selectAll()
            .map { record ->
                Player(
                    RivalId(record[Schema.Players.rivalId]),
                    record[Schema.Players.name]
                )
            }
    }

    override fun find(rivalId: RivalId) = transaction(db) {
        Schema.Players
            .select { Schema.Players.rivalId eq rivalId.rivalId }
            .first()
            .let { record ->
                Player(
                    rivalId,
                    record[Schema.Players.name]
                )
            }
    }

    override fun getRecords(player: Player) = transaction(db) {
        Schema.Records
            .select { Schema.Records.rivalId eq player.rivalId.rivalId }
            .orderBy(Schema.Records.recordedAt to false)
            .map { record ->
                Record(
                    RankingId(
                        record[Schema.Records.mid],
                        record[Schema.Records.diff],
                        record[Schema.Records.mode]
                    ),
                    player,
                    record[Schema.Records.score],
                    record[Schema.Records.recordedAt]
                )
            }
    }
}
