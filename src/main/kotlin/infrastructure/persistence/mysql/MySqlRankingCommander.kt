package infrastructure.persistence.mysql

import domain.core.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

internal class MySqlRankingCommander(private val db: Database) : RankingCommander {
    override fun save(ranking: Ranking) {
        /*
        val records = fetcher.fetch(header)
        transaction(db) {
            Schema.Players
                .batchInsertOnDuplicateKeyUpdate(records.map { it.player }, Schema.Players.columns) { batch, player ->
                    batch[rivalId] = player.rivalId
                    batch[name] = player.name
                }
            Schema.Records
                .batchInsertOnDuplicateKeyUpdate(records, Schema.Records.columns) { batch, record ->
                    batch[mid] = record.header.mid
                    batch[mode] = record.header.mode
                    batch[diff] = record.header.diff
                    batch[rivalId] = record.player.rivalId
                    batch[bestScore] = record.bestScore
                    batch[recordedAt] = record.recordedAt
                }
            Schema.Charts
                .batchInsertOnDuplicateKeyUpdate(listOf(header), Schema.Charts.columns) { batch, chart ->
                    batch[mid] = chart.mid
                    batch[mode] = chart.mode
                    batch[diff] = chart.diff
                    batch[lastUpdatedAt] = DateTime.now()
                }
        }
        */
    }
}
