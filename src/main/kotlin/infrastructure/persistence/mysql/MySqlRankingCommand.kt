package infrastructure.persistence.mysql

import domain.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

internal class MySqlRankingCommand(private val db: Database) : RankingCommand {
    override fun pull(fetcher: RankingFetcher, chart: Chart) {
        val records = fetcher.fetch(chart)
        transaction(db) {
            Schema.Players
                .batchInsertOnDuplicateKeyUpdate(records.map { it.player }, Schema.Players.columns) { batch, player ->
                    batch[rivalId] = player.rivalId
                    batch[name] = player.name
                }
            Schema.Records
                .batchInsertOnDuplicateKeyUpdate(records, Schema.Records.columns) { batch, record ->
                    batch[mid] = record.chart.mid
                    batch[mode] = record.chart.mode
                    batch[diff] = record.chart.diff
                    batch[rivalId] = record.player.rivalId
                    batch[bestScore] = record.bestScore
                    batch[recordedAt] = record.recordedAt
                }
            Schema.Charts
                .batchInsertOnDuplicateKeyUpdate(listOf(chart), Schema.Charts.columns) { batch, chart ->
                    batch[mid] = chart.mid
                    batch[mode] = chart.mode
                    batch[diff] = chart.diff
                    batch[lastUpdatedAt] = DateTime.now()
                }
        }
    }
}
