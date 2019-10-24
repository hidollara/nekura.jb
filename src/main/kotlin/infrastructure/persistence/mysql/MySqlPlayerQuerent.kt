package infrastructure.persistence.mysql

import domain.core.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

internal class MySqlPlayerQuerent(private val db: Database) : PlayerQuerent {
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
}
