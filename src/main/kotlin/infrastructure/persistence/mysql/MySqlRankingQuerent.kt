package infrastructure.persistence.mysql

import org.joda.time.DateTime

import domain.core.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

internal class MySqlRankingQuerent(private val db: Database) : RankingQuerent {
    override fun findEarliestUpdated() = transaction(db) {
        Schema.RankingHeaders
            .selectAll()
            .orderBy(Schema.RankingHeaders.lastUpdatedAt)
            .first()
            .let {
                Ranking(
                    RankingId(
                        it[Schema.RankingHeaders.mid],
                        it[Schema.RankingHeaders.diff],
                        it[Schema.RankingHeaders.mode]
                    ),
                    it[Schema.RankingHeaders.lastUpdatedAt],
                    listOf()
                )
            }
    }
}
