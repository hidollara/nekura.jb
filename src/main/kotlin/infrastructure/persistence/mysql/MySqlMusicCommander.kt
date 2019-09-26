package infrastructure.persistence.mysql

import domain.core.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

internal class MySqlMusicCommander(private val db: Database) : MusicCommander {
    override fun save(musics: List<Music>) {
        transaction(db) {
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
                .batchInsertOnDuplicateKeyUpdate(
                    musics.map { it.charts.values }.flatten(),
                    Schema.Charts.columns
                ) { batch, chart ->
                    batch[mid] = chart.mid
                    batch[diff] = chart.diff
                    batch[level] = chart.level.id
                }
            Schema.RankingHeaders
                .batchInsert(
                    musics.map { music ->
                        Difficulty.values().map { diff ->
                            Mode.values().map { mode ->
                                RankingId(music.mid, diff, mode)
                            }
                        }.flatten()
                    }.flatten(),
                    ignore = true
                ) {
                    this[Schema.RankingHeaders.mid] = it.mid
                    this[Schema.RankingHeaders.mode] = it.mode
                    this[Schema.RankingHeaders.diff] = it.diff
                    this[Schema.RankingHeaders.lastUpdatedAt] = DateTime.parse("1970-01-01")
                }
        }
    }
}
