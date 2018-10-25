package infrastructure.persistence.mysql

import domain.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

internal class MySqlMusicCommand(private val db: Database) : MusicCommand {
    override fun pull(fetcher: MusicFetcher) {
        transaction(db) {
            val musics = fetcher.fetch()
            Schema.Musics
                .batchInsertOnDuplicateKeyUpdate(
                    musics.asSequence().withIndex().toList(),
                    Schema.Musics.columns
                ) { batch, music ->
                    batch[mid] = music.value.mid
                    batch[title] = music.value.title
                    batch[order] = music.index
                }
            Schema.Charts
                .batchInsert(
                    musics.map { music ->
                        Mode.values().map { mode ->
                            Difficulty.values().map { diff ->
                                ChartWithLastUpdateDateTime(
                                    music.mid, mode, diff, DateTime.parse("1970-01-01")
                                )
                            }
                        }.flatten()
                    }.flatten(),
                    ignore = true
                ) {
                    this[Schema.Charts.mid] = it.mid
                    this[Schema.Charts.mode] = it.mode
                    this[Schema.Charts.diff] = it.diff
                    this[Schema.Charts.lastUpdatedAt] = it.lastUpdatedAt
                }
        }
    }
}
