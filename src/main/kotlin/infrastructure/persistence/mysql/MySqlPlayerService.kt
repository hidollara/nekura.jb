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
                    record[Schema.Players.rivalId],
                    record[Schema.Players.name]
                )
            }
    }

    override fun find(rivalId: RivalId) = transaction(db) {
        Schema.Players
            .select { Schema.Players.rivalId eq rivalId }
            .first()
            .let { record ->
                Player(
                    rivalId,
                    record[Schema.Players.name]
                )
            }
    }

    override fun getRecords(player: Player) = transaction(db) {
        (Schema.Records innerJoin Schema.Musics)
            .select { Schema.Records.rivalId eq player.rivalId }
            .orderBy(Schema.Records.recordedAt to false)
            .map { record ->
                Record(
                    RankingId(
                        Music(
                            record[Schema.Musics.mid],
                            record[Schema.Musics.title]
                        ),
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
