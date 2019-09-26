package infrastructure.persistence.mysql

import org.joda.time.DateTime

import domain.core.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

internal class MySqlRankingQuerent(private val db: Database) : RankingQuerent {
    override fun findEarliestUpdated() = transaction(db) {
        /*
        Schema.Charts
            .selectAll()
            .orderBy(Schema.Charts.lastUpdatedAt)
            .first()
        */
        Ranking(
            RankingId(
                0,
                Difficulty.BASIC,
                Mode.NORMAL
            ),
            DateTime.now(),
            listOf()
        )
    }
}
