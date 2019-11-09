package infrastructure.persistence.mysql

import domain.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

internal class MySqlRankingService(private val db: Database) : RankingService {
    override fun findEarliestUpdated() = transaction(db) {
        (Schema.RankingHeaders innerJoin Schema.Musics)
            .selectAll()
            .orderBy(Schema.RankingHeaders.lastUpdatedAt)
            .first()
            .let {
                Ranking(
                    RankingId(
                        Music(
                            it[Schema.Musics.mid],
                            it[Schema.Musics.title]
                        ),
                        it[Schema.RankingHeaders.diff],
                        it[Schema.RankingHeaders.mode]
                    ),
                    it[Schema.RankingHeaders.lastUpdatedAt],
                    listOf()
                )
            }
    }
}
