package infrastructure.persistence.mysql

import domain.core.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

internal class MySqlRankingRepository(private val db: Database) : RankingRepository {
    override fun save(ranking: Ranking) {
        transaction(db) {
            Schema.Players
                .batchInsertOnDuplicateKeyUpdate(ranking.records.map { it.player }, Schema.Players.columns) { batch, player ->
                    batch[rivalId] = player.rivalId.rivalId
                    batch[name] = player.name
                }
            Schema.Records
                .batchInsertOnDuplicateKeyUpdate(ranking.records, Schema.Records.columns) { batch, record ->
                    batch[mid] = record.rankingId.mid
                    batch[mode] = record.rankingId.mode
                    batch[diff] = record.rankingId.diff
                    batch[rivalId] = record.player.rivalId.rivalId
                    batch[score] = record.score
                    batch[recordedAt] = record.recordedAt
                }
            Schema.RankingHeaders
                .batchInsertOnDuplicateKeyUpdate(listOf(ranking), Schema.RankingHeaders.columns) { batch, ranking ->
                    batch[mid] = ranking.id.mid
                    batch[diff] = ranking.id.diff
                    batch[mode] = ranking.id.mode
                    batch[lastUpdatedAt] = DateTime.now()
                }
        }
    }
}
