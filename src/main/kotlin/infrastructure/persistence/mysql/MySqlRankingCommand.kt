package infrastructure.persistence.mysql

import domain.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

internal class MySqlRankingCommand(private val db: Database) : RankingCommand {
    override fun pull(fetcher: RankingFetcher, header: RecordHeader) {
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
    }
}
