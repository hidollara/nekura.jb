package infrastructure.persistence.mysql

import domain.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

internal class MySqlRecordHeaderQuery(private val db: Database) : RecordHeaderQuery {
    override fun find(
        mid: MusicId, diff: Difficulty, mode: Mode
    ): RecordHeader = transaction(db) {
        Schema.Charts
            .select {
                (Schema.Charts.mid eq mid) and
                    (Schema.Charts.mode eq mode) and
                    (Schema.Charts.diff eq diff)
            }
            .first()
            .let {
                RecordHeader(
                    it[Schema.Charts.mid], it[Schema.Charts.diff], it[Schema.Charts.level],
                    it[Schema.Charts.mode], it[Schema.Charts.lastUpdatedAt]
                )
            }
    }

    override fun findEarliestUpdated(): RecordHeader = transaction(db) {
        Schema.Charts
            .selectAll()
            .orderBy(Schema.Charts.lastUpdatedAt)
            .first()
            .let {
                RecordHeader(
                    it[Schema.Charts.mid], it[Schema.Charts.diff], it[Schema.Charts.level],
                    it[Schema.Charts.mode], it[Schema.Charts.lastUpdatedAt]
                )
            }
    }
}
