package infrastructure.persistence.mysql

import domain.core.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

internal class MySqlRankerQuerent(private val db: Database) : RankerQuerent {
    override fun all() = transaction(db) {
        Schema.Players
            .selectAll()
            .map { record ->
                Ranker(
                    RivalId(record[Schema.Players.rivalId]),
                    Player(
                        RivalId(record[Schema.Players.rivalId]),
                        record[Schema.Players.name]
                    ),
                    mapOf()
                )
            }
    }

    override fun find(rivalId: RivalId) = transaction(db) {
        /*
        (Schema.Players innerJoin Schema.Records)
            .select { Schema.Players.rivalId eq rivalId }
            .orderBy(Schema.Records.recordedAt to false)
        */
        Ranker(
            rivalId,
            Player(
                rivalId,
                "HOGE"
            ),
            mapOf()
        )
    }
}
