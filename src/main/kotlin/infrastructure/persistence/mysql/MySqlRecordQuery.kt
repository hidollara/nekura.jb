package infrastructure.persistence.mysql

import domain.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

internal class MySqlRecordQuery(private val db: Database) : RecordQuery {
    override fun recordsOf(header: RecordHeader) = transaction(db) {
        (Schema.Records innerJoin Schema.Players)
            .select {
                (Schema.Records.mid eq header.mid) and
                    (Schema.Records.mode eq header.mode) and
                    (Schema.Records.diff eq header.diff)
            }
            .orderBy(Schema.Records.bestScore to false, Schema.Records.recordedAt to false)
            .limit(100)
            .toRecords()
    }

    override fun recordsOf(rivalId: RivalId) = transaction(db) {
        (Schema.Records innerJoin Schema.Players)
            .select { Schema.Records.rivalId eq rivalId }
            .orderBy(Schema.Records.recordedAt to false)
            .toRecords()
    }

    override fun recordsBetween(from: DateTime, to: DateTime) = transaction(db) {
        (Schema.Records innerJoin Schema.Players)
            .select {
                (Schema.Records.recordedAt greaterEq from) and
                    (Schema.Records.recordedAt lessEq to)
            }
            .orderBy(Schema.Records.recordedAt to false)
            .toRecords()
    }

    private fun Query.toRecords(): Records =
        map {
            Record(
                Chart(it[Schema.Records.mid], it[Schema.Records.mode], it[Schema.Records.diff], /* TODO */ null),
                Player(it[Schema.Players.rivalId], it[Schema.Players.name]),
                it[Schema.Records.bestScore], it[Schema.Records.recordedAt]
            )
        }
}
