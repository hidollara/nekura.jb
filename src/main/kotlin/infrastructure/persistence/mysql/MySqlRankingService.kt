package infrastructure.persistence.mysql

import domain.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

internal class MySqlRankingService(private val db: Database) : RankingService {
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
