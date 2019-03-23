package infrastructure.persistence.mysql

import domain.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

internal class MySqlRankerQuery(private val db: Database) : RankerQuery {
    override fun all(): Players = transaction(db) {
        Schema.Players
            .selectAll()
            .map { player -> Player(player[Schema.Players.rivalId], player[Schema.Players.name]) }
    }
}
